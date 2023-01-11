package com.example.uberapp_tim6.services;

import com.example.uberapp_tim6.DTOS.RideDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RideService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("ride/driver/{driverId}/active")
    Call<RideDTO> getDriverActiveRide(@Path("driverId") String driverId);

    @PUT("ride/{rideId}/end")
    Call<RideDTO> endRide(@Path("rideId") String rideId);
}

