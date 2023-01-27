package com.example.uberapp_tim6;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.uberapp_tim6.DTOS.JWTTokenDTO;
import com.example.uberapp_tim6.DTOS.LoginCredentialDTO;
import com.example.uberapp_tim6.driver.DriverMainActivity;
import com.example.uberapp_tim6.passenger.PassengerMainActivity;
import com.example.uberapp_tim6.passenger.PassengerRegisterActivity;
import com.example.uberapp_tim6.services.AuthService;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.example.uberapp_tim6.tools.JwtDecoder;
import com.example.uberapp_tim6.tools.TokenHolder;

import java.security.KeyStore;

import javax.crypto.KeyGenerator;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginActivity extends AppCompatActivity {

    private EditText usernameEditText;

    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameEditText = findViewById(R.id.usernameTextBox);
        passwordEditText = findViewById(R.id.paswordTextBox);


        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginCredentialDTO loginCredentialDTO = new LoginCredentialDTO();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loginCredentialDTO.setEmail(username);
                loginCredentialDTO.setPassword(password);
                Log.d("Password",password);
                Log.d("Email",username);
                Call<JWTTokenDTO> loginCall = ServiceUtils.authService.login(loginCredentialDTO);
                loginCall.enqueue(new Callback<JWTTokenDTO>() {
                    @Override
                    public void onResponse(Call<JWTTokenDTO> call, Response<JWTTokenDTO> response) {
                        if (response.code() == 200){
                            String jwtToken = response.body().getAccessToken();
                            String refreshToken = response.body().getRefreshToken();
                            TokenHolder.getInstance().setJwtToken(jwtToken);
                            TokenHolder.getInstance().setRefreshToken(refreshToken);
                            SharedPreferences userPrefs = getSharedPreferences("userPrefs",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 = userPrefs.edit();
                            editor1.putString("id", JwtDecoder.getIdFromToken(jwtToken).toString());
                            String role = JwtDecoder.getRoleFromToken(jwtToken);
                            editor1.putString("role",role);
                            editor1.putString("email", JwtDecoder.getEmailFromToken(jwtToken));
                            editor1.apply();
                            if(role.equals("DRIVER")) {
                                startActivity(new Intent(UserLoginActivity.this, DriverMainActivity.class));
                                finish();
                            }
                            else if(role.equals("USER")){
                                startActivity(new Intent(UserLoginActivity.this, PassengerMainActivity.class));
                                finish();
                            }
                            else{
                                Log.d("REZ","Wrong credentials men!");
                            }
                        }else{
                            Log.d("REZ","Meesage recieved: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<JWTTokenDTO> call, Throwable t) {
                        Log.d("REZ", t.getMessage() != null?t.getMessage():"error");
                    }
                });
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