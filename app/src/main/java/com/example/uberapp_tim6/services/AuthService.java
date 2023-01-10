package com.example.uberapp_tim6.services;

import com.example.uberapp_tim6.DTOS.JWTTokenDTO;
import com.example.uberapp_tim6.DTOS.LoginCredentialDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("user/login")
    Call<JWTTokenDTO> login(@Body LoginCredentialDTO loginCredentialDTO);
}
