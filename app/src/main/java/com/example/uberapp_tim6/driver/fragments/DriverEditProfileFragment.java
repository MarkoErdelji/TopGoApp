package com.example.uberapp_tim6.driver.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.driver.DriverMainActivity;
import com.example.uberapp_tim6.tools.FragmentTransition;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverEditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverEditProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private UserInfoDTO driver;

    private static final String ARG_DRIVER = "arg_driver";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverEditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverEditProfileFragment newInstance(UserInfoDTO d) {
        DriverEditProfileFragment fragment = new DriverEditProfileFragment();
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
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button back = view.findViewById(R.id.edit_driver_back);
        DriverMainActivity dvm = (DriverMainActivity) this.getActivity();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(DriverProfileFragment.newInstance(driver),dvm,false,R.id.mainContent);
            }
        });
        ImageView img = view.findViewById(R.id.profileIcon);
        Glide.with(getContext()).load(driver.getProfilePicture()).into(img);
        TextView firstName = view.findViewById(R.id.nameValue);
        TextView lastName = view.findViewById(R.id.surnameValue);
        TextView Username = view.findViewById(R.id.usernameValue);
        TextView PhoneNumber = view.findViewById(R.id.phoneNumberValue);
        TextView Address = view.findViewById(R.id.addressValue);

        firstName.setText(driver.getName());
        lastName.setText(driver.getSurname());
        Username.setText(driver.getEmail());
        PhoneNumber.setText(driver.getTelephoneNumber());
        Address.setText(driver.getAddress());

        TextView changePasswordText = view.findViewById(R.id.changePasswordText);
        LayoutInflater inflater = getLayoutInflater();
        View changePasswordDialogView = inflater.inflate(R.layout.change_password_dialog, null);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this.getContext());
        builder2.setView(changePasswordDialogView);
        AlertDialog Dialog = builder2.create();
        Dialog.setCancelable(false);
        Button cancelButton = changePasswordDialogView.findViewById(R.id.btn_cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog.dismiss();
            }
        });

        changePasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog.show();
            }
        });
    }
}