package com.example.uberapp_tim6.driver.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserRidesListDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.adapters.DriverRideHistoryAdapter;
import com.example.uberapp_tim6.driver.CertainRideFromHistory;
import com.example.uberapp_tim6.models.Ride;
import com.example.uberapp_tim6.models.RideHistory;
import com.example.uberapp_tim6.models.User;
import com.example.uberapp_tim6.models.enumerations.Status;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.example.uberapp_tim6.tools.Mokap;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverRideHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverRideHistoryFragment extends ListFragment {

    private static final String ARG_DRIVER = "arg_driver";
    private DriverRideHistoryFragment fragmet;

    private UserInfoDTO driver;
    private final List<RideDTO> finishedRides = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverRideHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DriverRideHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverRideHistoryFragment newInstance(UserInfoDTO d) {
        DriverRideHistoryFragment fragment = new DriverRideHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DRIVER, d);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driver = (UserInfoDTO) getArguments().getSerializable(ARG_DRIVER);

        Call<UserRidesListDTO> call = ServiceUtils.driverService.getDriverRides(driver.getId(),0,10000);
        call.enqueue(new Callback<UserRidesListDTO>() {
            @Override
            public void onResponse(Call<UserRidesListDTO> call, Response<UserRidesListDTO> response) {
                finishedRides.clear();
                if (response.body() != null) {
                    response.body().getResults().forEach(result->{
                        if(result.status == Status.FINISHED){
                            finishedRides.add(result);
                        }
                    });
                    DriverRideHistoryAdapter adapter = new DriverRideHistoryAdapter(getActivity(), finishedRides);
                    setListAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "No rides", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<UserRidesListDTO> call, Throwable t) {
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_ride_history, container, false);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        System.out.println("##########################");
        System.out.println(finishedRides.get(position).getId());
        RideDTO ride = finishedRides.get(position);


        Intent intent = new Intent(getActivity(), CertainRideFromHistory.class);
        intent.putExtra("rideId", finishedRides.get(position).getId());
        intent.putExtra("currentUser", driver);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);

        System.out.println(ride.getId());
    }
}