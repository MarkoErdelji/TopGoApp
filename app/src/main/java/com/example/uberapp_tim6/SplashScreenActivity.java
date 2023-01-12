package com.example.uberapp_tim6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if(!isConnected(this)){
            Toast.makeText(this, "You are not connected to the internet!", Toast.LENGTH_SHORT).show();
        }
        else{
            int SPLASH_TIME_OUT = 3000;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, UserLoginActivity.class));
                    finish();
                }
            }, SPLASH_TIME_OUT);
            Toast.makeText(this, "You are connected to the internet!", Toast.LENGTH_SHORT).show();
        }




    }

    private boolean isConnected(SplashScreenActivity splashScreenActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) || (capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)));

    }
}