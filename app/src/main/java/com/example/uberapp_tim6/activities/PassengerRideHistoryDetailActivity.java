package com.example.uberapp_tim6.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uberapp_tim6.DTOS.CreateReviewResponseDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.VehicleInfoDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.adapters.MessageListAdapter;
import com.example.uberapp_tim6.adapters.PassengerRideHistoryAdapter;
import com.example.uberapp_tim6.models.Message;
import com.example.uberapp_tim6.models.Ride;
import com.example.uberapp_tim6.models.User;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.example.uberapp_tim6.tools.Mokap;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerRideHistoryDetailActivity  extends AppCompatActivity  {

    @SuppressLint("SetTextI18n")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger_ride_history_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        RideDTO ride = (RideDTO)getIntent().getSerializableExtra("ride");
        UserInfoDTO passenger = (UserInfoDTO)getIntent().getSerializableExtra("passenger");
        TextView departure = findViewById(R.id.departure_text_view);
        TextView destination = findViewById(R.id.destination_text_view);
        TextView driver_name = findViewById(R.id.driver_name);
        TextView driver_email = findViewById(R.id.driver_email);
        TextView car_name = findViewById(R.id.vehicle_name);
        TextView car_type = findViewById(R.id.vehicle_type);
        ImageView driver_image = findViewById(R.id.driver_icon);
        TextView price = findViewById(R.id.price);
        TextView date = findViewById(R.id.date);
        TextView time = findViewById(R.id.time_text);
        TextView estimated = findViewById(R.id.estimated_time);



        Call<UserInfoDTO> call = ServiceUtils.driverService.getDriverById(ride.getDriver().getId().toString());
        call.enqueue(new Callback<UserInfoDTO>() {
            @Override
            public void onResponse(Call<UserInfoDTO> call, Response<UserInfoDTO> response) {
                UserInfoDTO driver =response.body();
                Call<VehicleInfoDTO> vehicleCall = ServiceUtils.driverService.getDriverVehicle(driver.getId().toString());
                vehicleCall.enqueue(new Callback<VehicleInfoDTO>() {
                    @Override
                    public void onResponse(Call<VehicleInfoDTO> call, Response<VehicleInfoDTO> response) {
                        VehicleInfoDTO vehicle = response.body();
                        departure.setText(ride.getLocations().get(0).getDeparture().getAddress());
                        destination.setText(ride.getLocations().get(0).getDestination().getAddress());
                        driver_name.setText(driver.getName() + " " + driver.getSurname());
                        driver_email.setText(driver.getEmail());
                        car_name.setText(vehicle.getModel());
                        car_type.setText(vehicle.getVehicleType());
                        date.setText("Date: " + ride.getStartTime().toLocalDate());
                        price.setText("Price: "+ ride.getTotalCost() + "RSD");
                        time.setText("Time: " + ride.getStartTime().getHour()+":"+ride.getStartTime().getMinute() +"-" + ride.getEndTime().getHour()+":"+ride.getEndTime().getMinute());
                        estimated.setText("Estimated: "+ ride.getEstimatedTimeInMinutes() +"Min");
                        Glide.with(getApplicationContext()).load(driver.getProfilePicture()).into(driver_image);






                    }

                    @Override
                    public void onFailure(Call<VehicleInfoDTO> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onFailure(Call<UserInfoDTO> call, Throwable t) {

            }
        });









    }

    public boolean onOptionsItemSelected(MenuItem item) {

        this.finish();
        return true;
    }

}

