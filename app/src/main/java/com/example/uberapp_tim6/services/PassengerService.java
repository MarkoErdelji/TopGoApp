package com.example.uberapp_tim6.services;

import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface PassengerService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("passenger/{id}")
    Call<UserInfoDTO> getPassengerById(@Path("id") String id);

    @GET("passenger/ride/finished")
    Call<List<RideDTO>> getPassengerRides();
}

