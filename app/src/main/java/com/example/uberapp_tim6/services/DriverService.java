package com.example.uberapp_tim6.services;

import com.example.uberapp_tim6.DTOS.CreateReviewResponseDTO;
import com.example.uberapp_tim6.DTOS.DocumentInfoDTO;
import com.example.uberapp_tim6.DTOS.DriverReviewListDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.VehicleInfoDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface DriverService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("driver/{id}")
    Call<UserInfoDTO> getDriverById(@Path("id") String id);

    @GET("review/driver/{id}")
    Call<DriverReviewListDTO> getDriverReviews(@Path("id") String id);

    @GET("driver/{id}/vehicle")
    Call<VehicleInfoDTO> getDriverVehicle(@Path("id") String id);

    @GET("driver/{id}/documents")
    Call<List<DocumentInfoDTO>> getDriverDocuments(@Path("id") String id);
}
