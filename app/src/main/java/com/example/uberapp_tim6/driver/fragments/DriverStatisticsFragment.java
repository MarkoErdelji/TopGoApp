package com.example.uberapp_tim6.driver.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uberapp_tim6.DTOS.DriverWorkHoursDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserRidesListDTO;
import com.example.uberapp_tim6.DTOS.WorkHoursDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.models.enumerations.Status;
import com.example.uberapp_tim6.services.ServiceUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverStatisticsFragment extends Fragment {


    private static final String ARG_DRIVER = "arg_driver";

    UserInfoDTO driver;
    private EditText beginDateInput;
    private EditText endDateInput;
    private Button submitButton;
    private ImageView image;

    private TextView rejectedDrivesValue;
    private TextView acceptedDrivesValue;
    private TextView workingHoursValue;
    private TextView totalEarnedValue;

    public DriverStatisticsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DriverStatisticsFragment newInstance(UserInfoDTO d) {
        DriverStatisticsFragment fragment = new DriverStatisticsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DRIVER, d);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driver = (UserInfoDTO) getArguments().getSerializable(ARG_DRIVER);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View statisticsView = inflater.inflate(R.layout.fragment_driver_statistics, container, false);
        beginDateInput = statisticsView.findViewById(R.id.editText_start_date);
        endDateInput = statisticsView.findViewById(R.id.editText_end_date);
        submitButton = statisticsView.findViewById(R.id.button_submit);

        rejectedDrivesValue = statisticsView.findViewById(R.id.tv_rejected_drives_value);
        acceptedDrivesValue = statisticsView.findViewById(R.id.tv_accepted_drives_value);
        workingHoursValue = statisticsView.findViewById(R.id.tv_working_hours_value);
        totalEarnedValue = statisticsView.findViewById(R.id.tv_total_money_value);

        image = statisticsView.findViewById(R.id.profileIcon);

        Glide.with(requireContext()).load(driver.getProfilePicture()).into(image);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String beginDateString = beginDateInput.getText().toString();
                String endDateString = endDateInput.getText().toString();

                if (validateInput(beginDateString, endDateString)) {
                    submitForm(beginDateString, endDateString);
                }
            }
        });

        ServiceUtils.driverService.getDriverRides(driver.getId(), 0, 100000).enqueue(new Callback<UserRidesListDTO>() {
            @Override
            public void onResponse(Call<UserRidesListDTO> call, Response<UserRidesListDTO> response) {
                if (response.isSuccessful()) {
                    AtomicInteger rejected = new AtomicInteger();
                    AtomicInteger accepted = new AtomicInteger();
                    AtomicReference<Float> totalEarned = new AtomicReference<>((float) 0);
                    AtomicInteger workHoursValue = new AtomicInteger();
                    List<RideDTO> rides = response.body().getResults();
                    Collections.reverse(rides);

                    rides.forEach(ride-> {
                        if(ride.status == Status.REJECTED){
                            rejected.getAndIncrement();
                        };
                        if(ride.status == Status.FINISHED || ride.status == Status.ACCEPTED || ride.status == Status.ACTIVE){
                            accepted.getAndIncrement();
                        }
                        if(ride.status!=Status.PENDING && ride.status != Status.REJECTED){
                            totalEarned.updateAndGet(v -> v+ride.getTotalCost());
                        }
                    });
                    ServiceUtils.driverService.getDriverWorkingHours(driver.getId(), 0, 100000).enqueue(new Callback<DriverWorkHoursDTO>() {
                        @Override
                        public void onResponse(Call<DriverWorkHoursDTO> call, Response<DriverWorkHoursDTO> response) {
                            if(response.isSuccessful()){
                                assert response.body() != null;
                                List<WorkHoursDTO> workHours = response.body().getResults();
                                workHours.forEach(workHour->{
                                    if(workHour.end != null){
                                        workHoursValue.addAndGet((int) workHour.getDifferenceInSeconds());
                                    }
                                });
                                rejectedDrivesValue.setText(String.valueOf(rejected.get()));
                                acceptedDrivesValue.setText(String.valueOf(accepted.get()));
                                totalEarnedValue.setText(String.valueOf(totalEarned.get()));
                                workingHoursValue.setText(String.valueOf(Math.floor(workHoursValue.get()/3600.0)));


                            }
                        }

                        @Override
                        public void onFailure(Call<DriverWorkHoursDTO> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UserRidesListDTO> call, Throwable t) {

            }
        });


        return statisticsView;

    }


    private boolean validateInput(String beginDateString, String endDateString) {
        if (TextUtils.isEmpty(beginDateString)) {
            beginDateInput.setError("Begin date is required");
            return false;
        }
        if (TextUtils.isEmpty(endDateString)) {
            endDateInput.setError("End date is required");
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime beginDate = LocalDateTime.parse(beginDateString, formatter);
            LocalDateTime endDate = LocalDateTime.parse(endDateString, formatter);

            Log.d("BEGIN", beginDate.toString());
            Log.d("END", endDate.toString());
            if (beginDate.isAfter(endDate)) {
                beginDateInput.setError("Begin date must be before end date");
                endDateInput.setError("End date must be after begin date");
                return false;
            }
        } catch (DateTimeParseException e) {
            beginDateInput.setError("Invalid date format. Use ISO format: yyyy-MM-dd'T'HH:mm:ss");
            endDateInput.setError("Invalid date format. Use ISO format: yyyy-MM-dd'T'HH:mm:ss");
            return false;
        }
        return true;
    }

    private void submitForm(String beginDateString, String endDateString) {
        ServiceUtils.driverService.getDriverRidesWithInterval(driver.getId(), 0, 100000,beginDateString,endDateString).enqueue(new Callback<UserRidesListDTO>() {
            @Override
            public void onResponse(Call<UserRidesListDTO> call, Response<UserRidesListDTO> response) {
                if (response.isSuccessful()) {
                    AtomicInteger rejected = new AtomicInteger();
                    AtomicInteger accepted = new AtomicInteger();
                    AtomicReference<Float> totalEarned = new AtomicReference<>((float) 0);
                    AtomicInteger workHoursValue = new AtomicInteger();
                    List<RideDTO> rides = response.body().getResults();
                    Collections.reverse(rides);

                    rides.forEach(ride-> {
                        if(ride.status == Status.REJECTED){
                            rejected.getAndIncrement();
                        };
                        if(ride.status == Status.FINISHED || ride.status == Status.ACCEPTED || ride.status == Status.ACTIVE){
                            accepted.getAndIncrement();
                        }
                        if(ride.status!=Status.PENDING && ride.status != Status.REJECTED){
                            totalEarned.updateAndGet(v -> v+ride.getTotalCost());
                        }
                    });
                    ServiceUtils.driverService.getDriverWorkingHoursWithInterval(driver.getId(), 0, 100000,beginDateString,endDateString).enqueue(new Callback<DriverWorkHoursDTO>() {
                        @Override
                        public void onResponse(Call<DriverWorkHoursDTO> call, Response<DriverWorkHoursDTO> response) {
                            if(response.isSuccessful()){
                                assert response.body() != null;
                                List<WorkHoursDTO> workHours = response.body().getResults();
                                workHours.forEach(workHour->{
                                    if(workHour.end != null){
                                        workHoursValue.addAndGet((int) workHour.getDifferenceInSeconds());
                                    }
                                });
                                rejectedDrivesValue.setText(String.valueOf(rejected.get()));
                                acceptedDrivesValue.setText(String.valueOf(accepted.get()));
                                totalEarnedValue.setText(String.valueOf(totalEarned.get()));
                                workingHoursValue.setText(String.valueOf(Math.floor(workHoursValue.get()/3600.0)));


                            }
                        }

                        @Override
                        public void onFailure(Call<DriverWorkHoursDTO> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UserRidesListDTO> call, Throwable t) {

            }
        });

    }
}