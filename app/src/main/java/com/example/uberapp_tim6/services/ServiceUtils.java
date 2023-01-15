package com.example.uberapp_tim6.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.uberapp_tim6.tools.JwtInterceptor;
import com.example.uberapp_tim6.tools.LocalDateTimeDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceUtils {



    public static final String SERVICE_API_PATH = "http://172.21.240.1:8000/api/";



    public static OkHttpClient test(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor);
        clientBuilder.addInterceptor(new JwtInterceptor());
        OkHttpClient client = clientBuilder.build();

        return client;
    }




    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .client(test())
            .build();

    private static Gson getGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
        return gson;
    }


    public static AuthService authService = retrofit.create(AuthService.class);
    public static DriverService driverService = retrofit.create(DriverService.class);
    public static RideService rideService = retrofit.create(RideService.class);
    public static PassengerService passengerService = retrofit.create(PassengerService.class);



}