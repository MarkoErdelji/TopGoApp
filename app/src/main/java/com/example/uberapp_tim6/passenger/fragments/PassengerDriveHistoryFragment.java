package com.example.uberapp_tim6.passenger.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserRidesListDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.activities.PassengerRideHistoryDetailActivity;
import com.example.uberapp_tim6.adapters.PassengerRideHistoryAdapter;
import com.example.uberapp_tim6.models.Ride;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.example.uberapp_tim6.tools.Mokap;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerDriveHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerDriveHistoryFragment extends ListFragment {

    private static final String ARG_PASSENGER = "arg_passenger";

    UserInfoDTO passenger;
    private List<RideDTO> rides;

    public PassengerDriveHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DriverRideHistoryFragment.
     * @param passenger
     */
    // TODO: Rename and change types and number of parameters
    public static PassengerDriveHistoryFragment newInstance(UserInfoDTO passenger) {
        PassengerDriveHistoryFragment fragment = new PassengerDriveHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PASSENGER, passenger);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        passenger = (UserInfoDTO) getArguments().getSerializable(ARG_PASSENGER);
        super.onCreate(savedInstanceState);

        Call<List<RideDTO>> call = ServiceUtils.passengerService.getPassengerRides();
        call.enqueue(new Callback<List<RideDTO>>() {

            @Override
            public void onResponse(Call<List<RideDTO>> call, Response<List<RideDTO>> response) {
                rides = response.body();
                PassengerRideHistoryAdapter adapter = new PassengerRideHistoryAdapter(getActivity(), response.body());
                setListAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<RideDTO>> call, Throwable t) {

            }
        });



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passenger_drive_history, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        RideDTO ride = rides.get(position);

        Intent intent = new Intent(getActivity(), PassengerRideHistoryDetailActivity.class);

        intent.putExtra("ride", ride);
        intent.putExtra("passenger", passenger);
        startActivityForResult(intent, 0);

    }
    }