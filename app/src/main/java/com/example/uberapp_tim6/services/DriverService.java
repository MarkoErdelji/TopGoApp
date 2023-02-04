package com.example.uberapp_tim6.services;

import com.example.uberapp_tim6.DTOS.CreateReviewResponseDTO;
import com.example.uberapp_tim6.DTOS.DocumentInfoDTO;
import com.example.uberapp_tim6.DTOS.DriverActivityDTO;
import com.example.uberapp_tim6.DTOS.DriverInfoDTO;
import com.example.uberapp_tim6.DTOS.DriverReviewListDTO;
import com.example.uberapp_tim6.DTOS.DriverWorkHoursDTO;
import com.example.uberapp_tim6.DTOS.EndTimeDTO;
import com.example.uberapp_tim6.DTOS.StartTimeDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserRidesListDTO;
import com.example.uberapp_tim6.DTOS.VehicleInfoDTO;
import com.example.uberapp_tim6.DTOS.WorkHoursDTO;

import java.time.LocalDateTime;
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

    @POST("profileChangesRequest")
    Call<ResponseBody> postDriverProfileChanges(@Body DriverInfoDTO driverInfoDTO);


    @GET("driver/{id}/ride")
    Call<UserRidesListDTO> getDriverRides(@Path("id") Integer id,
                                          @Query("page") Integer page,
                                          @Query("size") Integer size);


    @GET("driver/{id}/ride")
    Call<UserRidesListDTO> getDriverRidesWithInterval(@Path("id") Integer id,
                                                      @Query("page") Integer page,
                                                      @Query("size") Integer size,
                                                      @Query("beginDateInterval") String beginDateInterval,
                                                      @Query("endDateInterval") String endDateInterval);

    @GET("driver/{id}/working-hour")
    Call<DriverWorkHoursDTO> getDriverWorkingHours(@Path("id") Integer id,
                                                   @Query("page") Integer page,
                                                   @Query("size") Integer size);

    @GET("driver/{id}/working-hour")
    Call<DriverWorkHoursDTO> getDriverWorkingHoursWithInterval(@Path("id") Integer id,
                                                 @Query("page") Integer page,
                                                 @Query("size") Integer size,
                                                 @Query("beginDateInterval") String beginDateInterval,
                                                 @Query("endDateInterval") String endDateInterval);

    @PUT("driver/{id}/activity")
    Call<DriverInfoDTO> changeDriverActivity(@Path("id") Integer id, @Body DriverActivityDTO driver);

    @POST("driver/{id}/working-hour")
    Call<WorkHoursDTO> addDriverWorkHour(@Path("id") Integer id, @Body StartTimeDTO start);

    @PUT("driver/working-hour/{id}")
    Call<WorkHoursDTO> putDriverWorkHour(@Path("id") Integer id, @Body EndTimeDTO endTimeDTO);



}
