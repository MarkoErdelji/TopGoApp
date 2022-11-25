package com.example.uberapp_tim6.driver.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.models.User;
import com.example.uberapp_tim6.tools.Mokap;

import java.time.format.DateTimeFormatter;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView phoneNumber;
    private TextView dateOfBirth;


    // TODO: Rename and change types and number of parameters
    public static DriverProfileFragment newInstance() {
        return new DriverProfileFragment();

    }

    public DriverProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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


        firstName = getView().findViewById(R.id.nameValue);
        lastName = getView().findViewById(R.id.surnameValue);
        email = getView().findViewById(R.id.usernameValue);
        phoneNumber = getView().findViewById(R.id.phoneNumberValue);
        dateOfBirth = getView().findViewById(R.id.dateValue);

        User passenger = Mokap.getPassengerProfile();

        firstName.setText(passenger.getFirstName());
        lastName.setText(passenger.getLastName());
        email.setText(passenger.getEmail());
        phoneNumber.setText(passenger.getPhoneNumber());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = passenger.getDateOfBirth().format(formatter);
        dateOfBirth.setText(formattedString);

    }
}