package com.example.uberapp_tim6.services;

import com.example.uberapp_tim6.DTOS.CreatePassengerDTO;
import com.example.uberapp_tim6.DTOS.CreatePassengerResponseDTO;
import com.example.uberapp_tim6.DTOS.CreateReviewDTO;
import com.example.uberapp_tim6.DTOS.CreateReviewResponseDTO;
import com.example.uberapp_tim6.DTOS.CreateRideDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PassengerService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("passenger/{id}")
    Call<UserInfoDTO> getPassengerById(@Path("id") String id);

    @POST("passenger")
    Call<CreatePassengerResponseDTO> register(@Body CreatePassengerDTO createPassengerDTO);
    @GET("passenger/ride/finished")
    Call<List<RideDTO>> getPassengerRides();

    @POST("review/{rideId}/driver")
    Call<CreateReviewResponseDTO> createDriverReview(@Path("rideId") String rideId,@Body CreateReviewDTO createReviewDTO);

    @POST("review/{rideId}/vehicle")
    Call<CreateReviewResponseDTO> createVehicleReview(@Path("rideId") String rideId,@Body CreateReviewDTO createReviewDTO);

}

