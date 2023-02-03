package com.example.uberapp_tim6.driver.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverStatisticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverStatisticsFragment extends Fragment {


    private static final String ARG_DRIVER = "arg_driver";



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
        UserInfoDTO driver = (UserInfoDTO) getArguments().getSerializable(ARG_DRIVER);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_statistics, container, false);
    }
}