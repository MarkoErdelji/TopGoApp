package com.example.uberapp_tim6.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uberapp_tim6.DTOS.CreateReviewDTO;
import com.example.uberapp_tim6.DTOS.CreateReviewResponseDTO;
import com.example.uberapp_tim6.DTOS.FavouriteRideDTO;
import com.example.uberapp_tim6.DTOS.FavouriteRideInfoDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.RideReviewsDTO;
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

public class PassengerRideHistoryDetailActivity extends AppCompatActivity {
    RatingBar vehicleRatingBar;
    RatingBar driverRatingBar;
    TextView driverComment;
    TextView vehicleComment;
    RelativeLayout rating;
    RelativeLayout rateNow;
    boolean added = false;

    @SuppressLint("SetTextI18n")
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger_ride_history_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        RideDTO ride = (RideDTO) getIntent().getSerializableExtra("ride");
        UserInfoDTO passenger = (UserInfoDTO) getIntent().getSerializableExtra("passenger");
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
        vehicleRatingBar = findViewById(R.id.simpleRatingBarVehicle);
        driverRatingBar = findViewById(R.id.simpleRatingBarDriver);
        driverComment = findViewById(R.id.driver_comment_text);
        vehicleComment = findViewById(R.id.vehicle_comment_text);
        rating = findViewById(R.id.reviews_info);
        rateNow = findViewById(R.id.rate_layout);
        Button rateBtn = findViewById(R.id.rate_button);
        ImageView fav = findViewById(R.id.fav_button);


        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!added) {
                    fav.setImageResource(R.drawable.star_icon_full);
                    FavouriteRideDTO favouriteRide = new FavouriteRideDTO();
                    favouriteRide.setVehicleType(ride.getVehicleType());
                    favouriteRide.setPassengers(ride.getPassengers());
                    favouriteRide.setBabyTransport(ride.isBabyTransport());
                    favouriteRide.setLocations(ride.getLocations());
                    favouriteRide.setPetTransport(ride.isPetTransport());
                    favouriteRide.setFavoriteName(ride.getLocations().get(0).getDestination().getAddress());
                    added = true;
                    ServiceUtils.rideService.addFavouriteRides(favouriteRide).enqueue(new Callback<List<FavouriteRideInfoDTO>>() {
                        @Override
                        public void onResponse(Call<List<FavouriteRideInfoDTO>> call, Response<List<FavouriteRideInfoDTO>> response) {
                        }

                        @Override
                        public void onFailure(Call<List<FavouriteRideInfoDTO>> call, Throwable t) {

                        }
                    });

                }
            }

        });
        ServiceUtils.rideService.getFavouriteRides().enqueue(new Callback<List<FavouriteRideInfoDTO>>() {
            @Override
            public void onResponse(Call<List<FavouriteRideInfoDTO>> call, Response<List<FavouriteRideInfoDTO>> response) {
                for (FavouriteRideInfoDTO favRide : response.body()
                ) {
                    if (favRide.getLocations().get(0).getDestination().getAddress().equals(ride.getLocations().get(0).getDestination().getAddress()))
                        if (favRide.getLocations().get(0).getDeparture().getAddress().equals(ride.getLocations().get(0).getDeparture().getAddress())) {
                            added = true;
                            fav.setImageResource(R.drawable.star_icon_full);

                        }

                }
            }

            @Override
            public void onFailure(Call<List<FavouriteRideInfoDTO>> call, Throwable t) {

            }
        });


        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showReviewPopUp(ride);
                    }
                });

            }
        });


        Call<UserInfoDTO> call = ServiceUtils.driverService.getDriverById(ride.getDriver().getId().toString());
        call.enqueue(new Callback<UserInfoDTO>() {
            @Override
            public void onResponse(Call<UserInfoDTO> call, Response<UserInfoDTO> response) {
                UserInfoDTO driver = response.body();
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
                        price.setText("Price: " + ride.getTotalCost() + "RSD");
                        time.setText("Time: " + ride.getStartTime().getHour() + ":" + ride.getStartTime().getMinute() + "-" + ride.getEndTime().getHour() + ":" + ride.getEndTime().getMinute());
                        estimated.setText("Estimated: " + ride.getEstimatedTimeInMinutes() + "Min");
                        Glide.with(getApplicationContext()).load(driver.getProfilePicture()).into(driver_image);
                        ServiceUtils.rideService.getRideReviews(ride.getId().toString()).enqueue(new Callback<List<RideReviewsDTO>>() {
                            @Override
                            public void onResponse(Call<List<RideReviewsDTO>> call, Response<List<RideReviewsDTO>> response) {
                                {

                                    if (response.body().get(0).getDriverReview().getId() != 0) {
                                        for (RideReviewsDTO rew : response.body()
                                        ) {
                                            if (rew.getVehicleReview().getPassenger().getId().equals(passenger.getId())) {
                                                vehicleComment.setText(rew.getVehicleReview().getComment());
                                                vehicleRatingBar.setRating(rew.getVehicleReview().getRating());

                                            }
                                            if (rew.getDriverReview().getPassenger().getId().equals(passenger.getId())) {
                                                driverComment.setText(rew.getDriverReview().getComment());
                                                driverRatingBar.setRating(rew.getDriverReview().getRating());
                                            }

                                        }
                                    } else {
                                        rating.setVisibility(View.GONE);
                                        rateNow.setVisibility(View.VISIBLE);
                                    }


                                }
                            }

                            @Override
                            public void onFailure(Call<List<RideReviewsDTO>> call, Throwable t) {

                            }
                        });


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

    private void showReviewPopUp(RideDTO rideDTO) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.review_dialog, null);
        builder1.setView(dialogLayout);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CreateReviewDTO createVehicleReview = new CreateReviewDTO();
                TextView textView = dialogLayout.findViewById(R.id.vehicleComment);
                RatingBar ratingBar = dialogLayout.findViewById(R.id.vehicleRatingBar);
                createVehicleReview.setComment(textView.getText().toString());
                createVehicleReview.setRating(ratingBar.getRating());
                Log.d("vehicle comment", createVehicleReview.getComment());
                Call<CreateReviewResponseDTO> call = ServiceUtils.passengerService.createVehicleReview(Integer.toString(rideDTO.getId()), createVehicleReview);
                call.enqueue(new Callback<CreateReviewResponseDTO>() {

                    @Override
                    public void onResponse(Call<CreateReviewResponseDTO> call, Response<CreateReviewResponseDTO> vehicleResponse) {
                        CreateReviewDTO createDriverReview = new CreateReviewDTO();
                        TextView driverComment2 = dialogLayout.findViewById(R.id.driverComment);
                        RatingBar ratingBar = dialogLayout.findViewById(R.id.driverRatingBar);
                        createDriverReview.setComment(driverComment2.getText().toString());
                        createDriverReview.setRating(ratingBar.getRating());
                        Log.d("driver comment", createDriverReview.getComment());
                        rating.setVisibility(View.VISIBLE);
                        rateNow.setVisibility(View.GONE);
                        vehicleComment.setText(vehicleResponse.body().getComment());
                        vehicleRatingBar.setRating(vehicleResponse.body().getRating());


                        Call<CreateReviewResponseDTO> call2 = ServiceUtils.passengerService.createDriverReview(Integer.toString(rideDTO.getId()), createDriverReview);
                        call2.enqueue(new Callback<CreateReviewResponseDTO>() {
                            @Override
                            public void onResponse(Call<CreateReviewResponseDTO> call, Response<CreateReviewResponseDTO> driverResponse) {
                                driverComment.setText(driverResponse.body().getComment());
                                driverRatingBar.setRating(driverResponse.body().getRating());

                            }

                            @Override
                            public void onFailure(Call<CreateReviewResponseDTO> call, Throwable t) {

                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<CreateReviewResponseDTO> call, Throwable t) {

                    }
                });

            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}

