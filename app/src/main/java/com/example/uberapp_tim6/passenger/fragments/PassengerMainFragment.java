package com.example.uberapp_tim6.passenger.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uberapp_tim6.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerMainFragment extends Fragment {

    public PassengerMainFragment() {
        // Required empty public constructor
    }

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View v = view.findViewById(R.id.bottom_navigation_container);
        View appBar = view.findViewById(R.id.app_bar);
        BottomSheetBehavior<View> bsb = BottomSheetBehavior.from(v);
        bsb.setPeekHeight(appBar.getHeight()+100);

        bsb.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // do stuff when the drawer is expanded
                }

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // do stuff when the drawer is collapsed
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // do stuff during the actual drag event for example
                // animating a background color change based on the offset

                // or for example hidding or showing a fab
                if (slideOffset > 0.2) {
                } else if (slideOffset < 0.15) {
                }
            }

        });

        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}