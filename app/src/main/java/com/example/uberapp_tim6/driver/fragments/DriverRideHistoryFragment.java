package com.example.uberapp_tim6.driver.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.adapters.DriverRideHistoryAdapter;
import com.example.uberapp_tim6.driver.CertainRideFromHistory;
import com.example.uberapp_tim6.models.Ride;
import com.example.uberapp_tim6.models.RideHistory;
import com.example.uberapp_tim6.tools.Mokap;

import java.time.Clock;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverRideHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverRideHistoryFragment extends ListFragment {

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
    public static DriverRideHistoryFragment newInstance() {
        return new DriverRideHistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getActivity(), "Istorija", Toast.LENGTH_SHORT).show();
        List<Ride> rh = Mokap.getAllRides();

        DriverRideHistoryAdapter adapter = new DriverRideHistoryAdapter(getActivity(), Mokap.getAllRides());
        setListAdapter(adapter);

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
        Ride ride = Mokap.getAllRides().get(position);


        Intent intent = new Intent(getActivity(), CertainRideFromHistory.class);
        intent.putExtra("ride", ride);
        startActivityForResult(intent, 0);

        System.out.println(ride.getId());
    }
}