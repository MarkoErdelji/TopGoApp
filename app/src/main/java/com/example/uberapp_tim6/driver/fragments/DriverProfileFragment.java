package com.example.uberapp_tim6.driver.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uberapp_tim6.DTOS.UserInfoDTO;
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

    private static final String ARG_DRIVER = "arg_driver";

    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView phoneNumber;
    private TextView dateOfBirth;
    private UserInfoDTO driver;
    private ImageView image;


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

        image = getView().findViewById(R.id.profileIcon);
        firstName = getView().findViewById(R.id.nameValue);
        lastName = getView().findViewById(R.id.surnameValue);
        email = getView().findViewById(R.id.usernameValue);
        phoneNumber = getView().findViewById(R.id.phoneNumberValue);
        dateOfBirth = getView().findViewById(R.id.dateValue);

        User passenger = Mokap.getPassengerProfile();

        firstName.setText(driver.getName());
        lastName.setText(driver.getSurname());
        email.setText(driver.getEmail());
        phoneNumber.setText(driver.getTelephoneNumber());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = passenger.getDateOfBirth().format(formatter);
        dateOfBirth.setText(formattedString);

        int index = driver.getProfilePicture().indexOf(",") + 1;
        String base64 = driver.getProfilePicture().substring(index);
        byte[] imageBytes = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        image.setImageBitmap(bitmap);


    }
}