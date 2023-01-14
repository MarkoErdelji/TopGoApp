package com.example.uberapp_tim6.passenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.UserLoginActivity;
import com.example.uberapp_tim6.driver.DriverMainActivity;


public class PassengerRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_register);
        Button registerBtn = findViewById(R.id.registerButton);
        Button backBtn = findViewById(R.id.registerBack);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PassengerRegisterActivity.this, PassengerMainActivity.class));
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PassengerRegisterActivity.this, UserLoginActivity.class));
                finish();
            }
        });

    }

}