package com.example.uberapp_tim6.services;

import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserMessagesListDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserService {

    @GET("user/{id}/message")
    Call<UserMessagesListDTO> getUserMessages(@Path("id") String id);

    @GET("user/{id}/messages")
    Call<UserMessagesListDTO> getUserConversation(@Path("id") String id);
}
