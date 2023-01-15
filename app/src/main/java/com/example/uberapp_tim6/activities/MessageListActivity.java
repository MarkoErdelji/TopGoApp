package com.example.uberapp_tim6.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserMessagesDTO;
import com.example.uberapp_tim6.DTOS.UserMessagesListDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.adapters.MessageListAdapter;
import com.example.uberapp_tim6.models.Message;
import com.example.uberapp_tim6.models.User;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.example.uberapp_tim6.tools.Mokap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_test);

        Toolbar toolbar = findViewById(R.id.toolbar_gchannel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Context contex = this;

        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        Call<UserMessagesListDTO> call = ServiceUtils.userService.getUserConversation(getIntent().getStringExtra("Sender"));
        call.enqueue(new Callback<UserMessagesListDTO>() {
            @Override
            public void onResponse(Call<UserMessagesListDTO> call, Response<UserMessagesListDTO> response) {
                UserMessagesListDTO messages = response.body();
                Call<UserInfoDTO> call2 = ServiceUtils.userService.getUserById(getIntent().getStringExtra("Sender"));
                call2.enqueue(new Callback<UserInfoDTO>() {
                    @Override
                    public void onResponse(Call<UserInfoDTO> call, Response<UserInfoDTO> response) {
                        mMessageAdapter = new MessageListAdapter(contex,messages,(UserInfoDTO)getIntent().getSerializableExtra("currentUser"),response.body());
                        mMessageRecycler.setLayoutManager(new LinearLayoutManager(contex));
                        mMessageRecycler.setAdapter(mMessageAdapter);

                    }

                    @Override
                    public void onFailure(Call<UserInfoDTO> call, Throwable t) {

                    }
                });

            }

            @Override
            public void onFailure(Call<UserMessagesListDTO> call, Throwable t) {

            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {

        this.finish();
        return true;
    }

}