package com.example.uberapp_tim6;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.uberapp_tim6.driver.DriverMainActivity;
import com.example.uberapp_tim6.passenger.PassengerMainActivity;
import com.example.uberapp_tim6.passenger.PassengerRegisterActivity;

public class UserLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginButton = findViewById(R.id.loginButton);
        Button reserveLoginButton = findViewById(R.id.reserveLoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLoginActivity.this, DriverMainActivity.class));
                finish();
            }
        });

        reserveLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLoginActivity.this, PassengerMainActivity.class));
                finish();
            }
        });
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) //ne znam koji je ovo vrag...
        TextView registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLoginActivity.this, PassengerRegisterActivity.class));
                finish();
            }
        });


    }
}