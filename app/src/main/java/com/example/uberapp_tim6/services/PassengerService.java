package com.example.uberapp_tim6.services;

import com.example.uberapp_tim6.DTOS.CreatePassengerDTO;
import com.example.uberapp_tim6.DTOS.CreatePassengerResponseDTO;
import com.example.uberapp_tim6.DTOS.CreateReviewDTO;
import com.example.uberapp_tim6.DTOS.CreateReviewResponseDTO;
import com.example.uberapp_tim6.DTOS.CreateRideDTO;
import com.example.uberapp_tim6.DTOS.DriverInfoDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UpdatePassengerDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserRidesListDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @PUT("passenger/{id}")
    Call<UserInfoDTO> changePassengerInfo(@Path("id") String id,@Body UpdatePassengerDTO passenger);


    @POST("review/{rideId}/driver")
    Call<CreateReviewResponseDTO> createDriverReview(@Path("rideId") String rideId,@Body CreateReviewDTO createReviewDTO);

    @POST("review/{rideId}/vehicle")
    Call<CreateReviewResponseDTO> createVehicleReview(@Path("rideId") String rideId,@Body CreateReviewDTO createReviewDTO);

    @GET("passenger/{id}/ride")
    Call<UserRidesListDTO> getPassengerRides(@Path("id") Integer id,
                                          @Query("page") Integer page,
                                          @Query("size") Integer size);


    @GET("passenger/{id}/ride")
    Call<UserRidesListDTO> getPassengerRidesWithInterval(@Path("id") Integer id,
                                                      @Query("page") Integer page,
                                                      @Query("size") Integer size,
                                                      @Query("beginDateInterval") String beginDateInterval,
                                                      @Query("endDateInterval") String endDateInterval);



}

