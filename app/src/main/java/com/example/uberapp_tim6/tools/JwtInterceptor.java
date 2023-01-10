package com.example.uberapp_tim6.tools;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtInterceptor implements Interceptor {
    private String jwt;

    public JwtInterceptor() {
        this.jwt = TokenHolder.getInstance().getJwtToken();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder()
                .header("Authorization", "Bearer " + jwt);
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}