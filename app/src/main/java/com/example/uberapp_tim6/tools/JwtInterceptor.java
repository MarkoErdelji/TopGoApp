package com.example.uberapp_tim6.tools;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtInterceptor implements Interceptor {
    private String jwt;

    public JwtInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        this.jwt = TokenHolder.getInstance().getJwtToken();
        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer " + jwt);
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}