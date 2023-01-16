package com.example.uberapp_tim6.services;


import com.example.uberapp_tim6.DTOS.CreateRideDTO;
import com.example.uberapp_tim6.DTOS.CreateReviewResponseDTO;
import com.example.uberapp_tim6.DTOS.PanicDTO;
import com.example.uberapp_tim6.DTOS.ReasonDTO;
import com.example.uberapp_tim6.DTOS.RejectionTextDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RideService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("ride/driver/{driverId}/active")
    Call<RideDTO> getDriverActiveRide(@Path("driverId") String driverId);

    @GET("ride/driver/{driverId}/accepted")
    Call<RideDTO> getDriverAcceptedRide(@Path("driverId") String driverId);

    @GET("ride/driver/{driverId}/finished")
    Call<List<RideDTO>> getDriverFinishedRides(@Path("driverId") String driverId);

    @GET("ride/{id}")
    Call<RideDTO> getRide(@Path("id") String id);

    @GET("ride/passenger/{passengerId}/active")
    Call<RideDTO> getPassengerActiveRide(@Path("passengerId") String passengerId);

    @GET("review/ride/{id}")
    Call<List<CreateReviewResponseDTO>> getAllRideReviews(@Path("id") String id);

    @PUT("ride/{rideId}/end")
    Call<RideDTO> endRide(@Path("rideId") String rideId);

    @PUT("ride/{rideId}/start")
    Call<RideDTO> startRide(@Path("rideId") String rideId);

    @PUT("ride/{rideId}/accept")
    Call<RideDTO> acceptRide(@Path("rideId") String rideId);

    @PUT("ride/{rideId}/cancel")
    Call<RideDTO> cancelRide(@Path("rideId") String rideId, @Body RejectionTextDTO rejectionTextDTO);

    @PUT("ride/{rideId}/decline")
    Call<RideDTO> declineRide(@Path("rideId") String rideId, @Body RejectionTextDTO rejectionTextDTO);

    @PUT("ride/{rideId}/panic")
    Call<PanicDTO> panicRide(@Body ReasonDTO reason,@Path("rideId") String rideId);

    @POST("ride")
    Call<RideDTO> createRide(@Body CreateRideDTO createRideDto);
}

