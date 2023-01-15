package com.example.uberapp_tim6.passenger.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.driver.fragments.DriverProfileFragment;
import com.example.uberapp_tim6.models.User;
import com.example.uberapp_tim6.tools.Mokap;

import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ARG_PASSENGER = "arg_passenger";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView phoneNumber;
    private TextView dateOfBirth;
    private TextView address;
    private UserInfoDTO passenger;

    private ImageView image;


    public PassengerProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassengerProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassengerProfileFragment newInstance(UserInfoDTO p) {
        PassengerProfileFragment fragment = new PassengerProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PASSENGER, p);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passenger = (UserInfoDTO) getArguments().getSerializable(ARG_PASSENGER);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passenger_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView image = getView().findViewById(R.id.profileIcon);
        Glide.with(getContext()).load(passenger.getProfilePicture()).into(image);

        firstName = getView().findViewById(R.id.nameValue);
        lastName = getView().findViewById(R.id.surnameValue);
        email = getView().findViewById(R.id.usernameValue);
        phoneNumber = getView().findViewById(R.id.phoneNumberValue);
        address = getView().findViewById(R.id.addressValue);


        firstName.setText(passenger.getName());
        lastName.setText(passenger.getSurname());
        email.setText(passenger.getEmail());
        phoneNumber.setText(passenger.getTelephoneNumber());
        address.setText(passenger.getAddress());

    }
}