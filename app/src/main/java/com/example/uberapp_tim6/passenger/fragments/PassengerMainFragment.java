package com.example.uberapp_tim6.passenger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uberapp_tim6.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerMainFragment extends Fragment {

    public PassengerMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PassengerMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassengerMainFragment newInstance() {
        return new PassengerMainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passenger_main, container, false);
    }
}