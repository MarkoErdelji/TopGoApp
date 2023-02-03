package com.example.uberapp_tim6.activities;

import static com.example.uberapp_tim6.services.ServiceUtils.LOCALHOST;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim6.DTOS.MessageType;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.SendMessageDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserMessagesDTO;
import com.example.uberapp_tim6.DTOS.UserMessagesListDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.adapters.MessageListAdapter;
import com.example.uberapp_tim6.models.Message;
import com.example.uberapp_tim6.models.User;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.example.uberapp_tim6.tools.DateTimeDeserializer;
import com.example.uberapp_tim6.tools.DateTimeSerializer;
import com.example.uberapp_tim6.tools.Mokap;
import com.example.uberapp_tim6.tools.TokenHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.gusavila92.websocketclient.WebSocketClient;

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private WebSocketClient webSocketClient;
    private SharedPreferences userPrefs;
    private Context contex ;
    private Button send_btn;
    private EditText msgText;
    private RideDTO ride;
    UserInfoDTO user;
    UserMessagesListDTO messages;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_test);

        Toolbar toolbar = findViewById(R.id.toolbar_gchannel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        contex = this;
        userPrefs = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        createUserMessageSession();
        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        send_btn = findViewById(R.id.button_gchat_send);
        msgText = findViewById(R.id.edit_gchat_message);
        ride = (RideDTO) getIntent().getSerializableExtra("Ride");

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageDTO msg = new SendMessageDTO();
                msg.setMessage(msgText.getText().toString());
                if (!(ride == null))
                    msg.setRideId(ride.id);
                else
                {
                    msg.setRideId(1);
                }
                msg.setType(MessageType.RIDE);
                if (!msgText.getText().toString().equals("")) {

                Call<UserMessagesDTO> call = ServiceUtils.userService.sendUserMessage(user.getId().toString(), msg);
                call.enqueue(new Callback<UserMessagesDTO>() {
                    @Override
                    public void onResponse(Call<UserMessagesDTO> call, Response<UserMessagesDTO> response) {
                        Log.d("res", response.body().message);
                    }

                    @Override
                    public void onFailure(Call<UserMessagesDTO> call, Throwable t) {

                    }
                });
            }
                msgText.setText("");
            }
        });




        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        Call<UserMessagesListDTO> call = ServiceUtils.userService.getUserConversation(getIntent().getStringExtra("Sender"));
        call.enqueue(new Callback<UserMessagesListDTO>() {
            @Override
            public void onResponse(Call<UserMessagesListDTO> call, Response<UserMessagesListDTO> response) {
                messages = response.body();
                Call<UserInfoDTO> call2 = ServiceUtils.userService.getUserById(getIntent().getStringExtra("Sender"));
                call2.enqueue(new Callback<UserInfoDTO>() {
                    @Override
                    public void onResponse(Call<UserInfoDTO> call, Response<UserInfoDTO> response) {
                        user = response.body();
                        mMessageAdapter = new MessageListAdapter(contex,messages,(UserInfoDTO)getIntent().getSerializableExtra("currentUser"),user);
                        mMessageRecycler.setLayoutManager(new LinearLayoutManager(contex));
                        mMessageRecycler.scrollToPosition(messages.getResults().size() - 1);
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

    private void createUserMessageSession(){
        Log.d("KURCAAAA","CCCC");
        URI uri;
        try {
            // Connect to local host


            uri = new URI("ws://"+LOCALHOST+"/messages");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.d("WebSocket", "Session is starting on messages");
                webSocketClient.send("Hello World!");
            }

            @Override
            public void onTextReceived(String s) {
                Log.d("WebSocket", "Message received");
                final String message = s;
                Log.d("message", message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new DateTimeDeserializer());
                        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new DateTimeSerializer());
                        Gson gson = gsonBuilder.create();
                        UserMessagesDTO msg = gson.fromJson(message, UserMessagesDTO.class);
                        Log.d("MSG", msg.toString());
                        messages.getResults().add(msg);
                        mMessageAdapter = new MessageListAdapter(contex,messages,(UserInfoDTO)getIntent().getSerializableExtra("currentUser"),user);
                        mMessageRecycler.setLayoutManager(new LinearLayoutManager(contex));
                        mMessageRecycler.scrollToPosition(messages.getResults().size() - 1);
                        mMessageRecycler.setAdapter(mMessageAdapter);


                    }
                });
            }

            @Override
            public void onBinaryReceived(byte[] data) {
            }

            @Override
            public void onPingReceived(byte[] data) {
            }

            @Override
            public void onPongReceived(byte[] data) {
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };

        webSocketClient.enableAutomaticReconnection(1000);
        webSocketClient.addHeader("Authorization", "Bearer " + TokenHolder.getInstance().getJwtToken());
        webSocketClient.addHeader("id",userPrefs.getString("id","0"));
        webSocketClient.addHeader("role",userPrefs.getString("role","0"));
        webSocketClient.connect();
    }

}