package com.example.uberapp_tim6.passenger.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.adapters.DriverRideHistoryAdapter;
import com.example.uberapp_tim6.adapters.PassengerRideHistoryAdapter;
import com.example.uberapp_tim6.driver.fragments.DriverRideHistoryFragment;
import com.example.uberapp_tim6.models.RideHistory;
import com.example.uberapp_tim6.tools.Mokap;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerDriveHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerDriveHistoryFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassengerDriveHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DriverRideHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassengerDriveHistoryFragment newInstance() {
        return new PassengerDriveHistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getActivity(), "Istorija", Toast.LENGTH_SHORT).show();
        List<RideHistory> rh = Mokap.getDriverRideHistory();

        PassengerRideHistoryAdapter adapter = new PassengerRideHistoryAdapter(getActivity(), Mokap.getDriverRideHistory());
        setListAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passenger_drive_history, container, false);
    }
}