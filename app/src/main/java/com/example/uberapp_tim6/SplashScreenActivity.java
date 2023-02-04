package com.example.uberapp_tim6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.example.uberapp_tim6.driver.DriverMainActivity;
import com.example.uberapp_tim6.passenger.PassengerMainActivity;
import com.example.uberapp_tim6.tools.TokenHolder;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if(!isConnected(this)){
            Toast.makeText(this, "You are not connected to the internet!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            if(!isLocationEnabled()){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    int SPLASH_TIME_OUT = 3000;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            SharedPreferences userPrefs = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
                            String role = userPrefs.getString("role", "notLogged");
                            if (role.equals("notLogged")) {
                                startActivity(new Intent(SplashScreenActivity.this, UserLoginActivity.class));
                                finish();
                            }
                            else {
                                TokenHolder.getInstance().setRefreshToken(userPrefs.getString("refreshToken", ""));
                                TokenHolder.getInstance().setJwtToken(userPrefs.getString("JWToken", ""));
                                if (role.equals("DRIVER")) {
                                    startActivity(new Intent(SplashScreenActivity.this, DriverMainActivity.class));
                                    finish();
                                } else if (role.equals("USER")) {
                                    startActivity(new Intent(SplashScreenActivity.this, PassengerMainActivity.class));
                                    finish();
                                }
                            }
                        }
                    }, SPLASH_TIME_OUT);
                } else {
                    // Request permission
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Toast.makeText(this, "You need enabled location to use this app!", Toast.LENGTH_SHORT).show();
                    }
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                }
            }
            else{
                int SPLASH_TIME_OUT = 3000;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        SharedPreferences userPrefs = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
                        String role = userPrefs.getString("role", "notLogged");
                        if (role.equals("notLogged")) {
                            startActivity(new Intent(SplashScreenActivity.this, UserLoginActivity.class));
                            finish();
                        }
                        else {
                            TokenHolder.getInstance().setRefreshToken(userPrefs.getString("refreshToken", ""));
                            TokenHolder.getInstance().setJwtToken(userPrefs.getString("JWToken", ""));
                            if (role.equals("DRIVER")) {
                                startActivity(new Intent(SplashScreenActivity.this, DriverMainActivity.class));
                                finish();
                            } else if (role.equals("USER")) {
                                startActivity(new Intent(SplashScreenActivity.this, PassengerMainActivity.class));
                                finish();
                            }
                        }
                    }
                }, SPLASH_TIME_OUT);
            }
        }
     }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                int SPLASH_TIME_OUT = 3000;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        SharedPreferences userPrefs = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
                        String role = userPrefs.getString("role", "notLogged");
                        if (role.equals("notLogged")) {
                            startActivity(new Intent(SplashScreenActivity.this, UserLoginActivity.class));
                            finish();
                        }
                        else {
                            TokenHolder.getInstance().setRefreshToken(userPrefs.getString("refreshToken", ""));
                            TokenHolder.getInstance().setJwtToken(userPrefs.getString("JWToken", ""));
                            if (role.equals("DRIVER")) {
                                startActivity(new Intent(SplashScreenActivity.this, DriverMainActivity.class));
                                finish();
                            } else if (role.equals("USER")) {
                                startActivity(new Intent(SplashScreenActivity.this, PassengerMainActivity.class));
                                finish();
                            }
                        }
                    }
                }, SPLASH_TIME_OUT);
            }
            else {
                Toast.makeText(this, "You cannot use this app with location disabled!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean isConnected(SplashScreenActivity splashScreenActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) || (capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)));

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}