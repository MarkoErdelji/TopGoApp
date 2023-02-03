package com.example.uberapp_tim6.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uberapp_tim6.DTOS.CreateRideDTO;
import com.example.uberapp_tim6.DTOS.DocumentInfoDTO;
import com.example.uberapp_tim6.DTOS.FavouriteRideInfoDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.services.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerRoutesDialogAdapter extends BaseAdapter {
    Context mContext;
    List<FavouriteRideInfoDTO> mNavItems;


    public PassengerRoutesDialogAdapter(Context context, List<FavouriteRideInfoDTO> navItems){
        mContext = context;
        mNavItems = navItems;
    }
    @Override
    public int getCount() {
        return mNavItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mNavItems.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.favourite_ride_list_item, null);
        }
        else {
            view = convertView;
        }

        TextView titleView = (TextView) view.findViewById(R.id.route_name);
        Button orderButton = (Button) view.findViewById(R.id.order_again_button);
        Button deleteButton = (Button) view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ServiceUtils.rideService.deleteRide(String.valueOf(mNavItems.get(i).getId())).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateRideDTO createRide = new CreateRideDTO();
                FavouriteRideInfoDTO fav = mNavItems.get(i);
                createRide.setPassengers(fav.getPassengers());
                createRide.setBabyTransport(fav.isBabyTransport());
                createRide.setPetTransport(fav.isPetTransport());
                createRide.setLocations(fav.getLocations());
                createRide.setVehicleType(fav.getVehicleType());
                ServiceUtils.rideService.createRide(createRide).enqueue(new Callback<RideDTO>() {
                    @Override
                    public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {

                    }

                    @Override
                    public void onFailure(Call<RideDTO> call, Throwable t) {

                    }
                });
            }
        });

        titleView.setText( mNavItems.get(i).getFavoriteName());


        return view;
    }
}
