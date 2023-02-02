package com.example.uberapp_tim6.services;

import com.example.uberapp_tim6.DTOS.ChangePasswordDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.SendMessageDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserMessagesDTO;
import com.example.uberapp_tim6.DTOS.UserMessagesListDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    @GET("user/{id}/message")
    Call<UserMessagesListDTO> getUserMessages(@Path("id") String id);

    @GET("user/{id}/messages")
    Call<UserMessagesListDTO> getUserConversation(@Path("id") String id);

    @GET("user/id/{id}")
    Call<UserInfoDTO> getUserById(@Path("id") String id);

    @PUT("user/{id}/changePassword")
    Call<ResponseBody> changeUserPassword(@Path("id") String id, @Body ChangePasswordDTO changePasswordDTO);

    @POST("user/{id}/message")
    Call<UserMessagesDTO> sendUserMessage(@Path("id") String id, @Body SendMessageDTO message);

}
