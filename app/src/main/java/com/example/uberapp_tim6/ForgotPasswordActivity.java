package com.example.uberapp_tim6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.uberapp_tim6.DTOS.UserRef;
import com.example.uberapp_tim6.passenger.PassengerRegisterActivity;
import com.example.uberapp_tim6.services.ServiceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        EditText emailEditText = findViewById(R.id.usernameTextBox);
        Button sendMailButton = findViewById(R.id.sendMailButton);
        sendMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                Call<UserRef> userRefCall = ServiceUtils.userService.getUserRefByEmail(email);
                userRefCall.enqueue(new Callback<UserRef>() {
                    @Override
                    public void onResponse(Call<UserRef> call, Response<UserRef> response) {
                        if(response.isSuccessful()){
                            Call<String> sendEmailCall = ServiceUtils.userService.sendMail(response.body().getId().toString());
                            sendEmailCall.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                                    builder.setMessage("We sent you reset token. Check your email");
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do something
                                            finish();
                                        }
                                    });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                            builder.setMessage("User with that email does not exist!");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // do something
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                    }

                    @Override
                    public void onFailure(Call<UserRef> call, Throwable t) {


                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}