package com.example.uberapp_tim6.services;


import com.example.uberapp_tim6.DTOS.CreateRideDTO;
import com.example.uberapp_tim6.DTOS.CreateReviewResponseDTO;
import com.example.uberapp_tim6.DTOS.FavouriteRideDTO;
import com.example.uberapp_tim6.DTOS.FavouriteRideInfoDTO;
import com.example.uberapp_tim6.DTOS.PanicDTO;
import com.example.uberapp_tim6.DTOS.ReasonDTO;
import com.example.uberapp_tim6.DTOS.RejectionTextDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.RideReviewsDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @GET("review/{id}")
    Call<List<RideReviewsDTO>> getRideReviews(@Path("id") String id);

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

    @PUT("ride/simulate/{rideId}")
    Call<ResponseBody> simulate(@Path("rideId") String rideId);

    @POST("ride")
    Call<RideDTO> createRide(@Body CreateRideDTO createRideDto);

    @GET("ride/favorites")
    Call<List<FavouriteRideInfoDTO>> getFavouriteRides();

    @POST("ride/favorites")
    Call<List<FavouriteRideInfoDTO>> addFavouriteRides(@Body FavouriteRideDTO fav);

    @DELETE("ride/favorites/{rideId}")
    Call<String> deleteRide(@Path("rideId") String rideId);
}

