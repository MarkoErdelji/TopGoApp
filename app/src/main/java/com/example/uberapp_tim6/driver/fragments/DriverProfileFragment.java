package com.example.uberapp_tim6.driver.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uberapp_tim6.DTOS.DocumentInfoDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.VehicleInfoDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.adapters.DriverDocumentDialogAdapter;
import com.example.uberapp_tim6.driver.DriverMainActivity;
import com.example.uberapp_tim6.models.User;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.example.uberapp_tim6.tools.FragmentTransition;
import com.example.uberapp_tim6.tools.Mokap;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class DriverProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ARG_DRIVER = "arg_driver";

    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView phoneNumber;
    private TextView adress;
    private UserInfoDTO driver;
    private ImageView image;
    private View iconDocument;
    private View iconVehicle;

    private Button editProfile;

    private AlertDialog vehicleDialog;
    private View documentDialogView;

    private AlertDialog documentDialog;
    private View vehicleDialogView;


    // TODO: Rename and change types and number of parameters
    public static DriverProfileFragment newInstance(UserInfoDTO d) {
        DriverProfileFragment fragment = new DriverProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DRIVER, d);
        fragment.setArguments(bundle);
        return fragment;

    }

    public DriverProfileFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_driver_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();
        vehicleDialogView = inflater.inflate(R.layout.driver_vehicle_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setView(vehicleDialogView);
        vehicleDialog = builder.create();

        documentDialogView = inflater.inflate(R.layout.driver_document_dialog, null);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this.getContext());
        builder2.setView(documentDialogView);
        documentDialog = builder2.create();

        iconDocument = getView().findViewById(R.id.imageDocumentLayout);
        iconVehicle = getView().findViewById(R.id.imageVehicleLayout);
        iconDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDocumentDialogAndShow(documentDialogView,documentDialog,driver.getId());
            }
        });
        iconVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createVehicleDialogAndShow(vehicleDialogView,vehicleDialog,driver.getId());
            }
        });
        DriverMainActivity DriverMainActivity = (com.example.uberapp_tim6.driver.DriverMainActivity) this.getActivity();
        editProfile = view.findViewById(R.id.edit_profile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(DriverEditProfileFragment.newInstance(driver),DriverMainActivity,false,R.id.mainContent);
            }
        });
        image = getView().findViewById(R.id.profileIcon);
        firstName = getView().findViewById(R.id.nameValue);
        lastName = getView().findViewById(R.id.surnameValue);
        email = getView().findViewById(R.id.usernameValue);
        phoneNumber = getView().findViewById(R.id.phoneNumberValue);
        adress = getView().findViewById(R.id.addressValue);

        User passenger = Mokap.getPassengerProfile();

        firstName.setText(driver.getName());
        lastName.setText(driver.getSurname());
        email.setText(driver.getEmail());
        phoneNumber.setText(driver.getTelephoneNumber());
        adress.setText(driver.getAddress());

        Glide.with(getContext()).load(driver.getProfilePicture()).into(image);




    }

    private void createVehicleDialogAndShow(View vehicleDialogView, AlertDialog vehicleDialog, Integer driverId) {
        TextView carModelValue = vehicleDialogView.findViewById(R.id.carModelValue);
        TextView carLicenseValue = vehicleDialogView.findViewById(R.id.licensePlateValue);
        TextView numOfSeatsValue = vehicleDialogView.findViewById(R.id.numOfSeatsValue);
        TextView typeValue = vehicleDialogView.findViewById(R.id.typeValue);
        CheckBox petsCheckbox = vehicleDialogView.findViewById(R.id.petCheckbox);
        CheckBox babyCheckbox = vehicleDialogView.findViewById(R.id.babyCheckbox);

        ServiceUtils.driverService.getDriverVehicle(String.valueOf(driverId)).enqueue(new Callback<VehicleInfoDTO>() {
            @Override
            public void onResponse(Call<VehicleInfoDTO> call, Response<VehicleInfoDTO> response) {
                if(response.isSuccessful()) {
                    carModelValue.setText(response.body().getModel());
                    carLicenseValue.setText(response.body().getLicenseNumber());
                    numOfSeatsValue.setText(String.valueOf(response.body().getPassengerSeats()));
                    typeValue.setText(response.body().getVehicleType());
                    petsCheckbox.setChecked(response.body().petTransport);
                    babyCheckbox.setChecked(response.body().babyTransport);
                    petsCheckbox.setClickable(false);
                    babyCheckbox.setClickable(false);
                    vehicleDialog.show();
                }
            }

            @Override
            public void onFailure(Call<VehicleInfoDTO> call, Throwable t) {

            }
        });


    }


    private void createDocumentDialogAndShow(View documentDialogView, AlertDialog documentDialog, Integer driverId) {


        ServiceUtils.driverService.getDriverDocuments(String.valueOf(driverId)).enqueue(new Callback<List<DocumentInfoDTO>>() {
            @Override
            public void onResponse(Call<List<DocumentInfoDTO>> call, Response<List<DocumentInfoDTO>> response) {
                if(response.isSuccessful()){
                    ListView listView = documentDialogView.findViewById(R.id.documentList);
                    DriverDocumentDialogAdapter adapter = new DriverDocumentDialogAdapter(getContext(), (ArrayList<DocumentInfoDTO>) response.body());
                    listView.setAdapter(adapter);
                    documentDialog.show();

                }
            }

            @Override
            public void onFailure(Call<List<DocumentInfoDTO>> call, Throwable t) {

            }
        });


    }
}