package com.example.uberapp_tim6.passenger;

import static com.example.uberapp_tim6.services.ServiceUtils.LOCALHOST;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.VehicleInfoDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.UserLoginActivity;
import com.example.uberapp_tim6.adapters.DrawerListAdapter;
import com.example.uberapp_tim6.models.NavItem;
import com.example.uberapp_tim6.models.enumerations.Status;
import com.example.uberapp_tim6.passenger.fragments.PassengerDriveHistoryFragment;
import com.example.uberapp_tim6.passenger.fragments.PassengerInboxFragment;
import com.example.uberapp_tim6.passenger.fragments.PassengerMainFragment;
import com.example.uberapp_tim6.passenger.fragments.PassengerProfileFragment;
import com.example.uberapp_tim6.services.MapService;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.example.uberapp_tim6.tools.DateTimeDeserializer;
import com.example.uberapp_tim6.tools.DateTimeSerializer;
import com.example.uberapp_tim6.tools.FragmentTransition;
import com.example.uberapp_tim6.tools.TokenHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.gusavila92.websocketclient.WebSocketClient;

public class PassengerMainActivity extends AppCompatActivity {

    private WebSocketClient webSocketClient;

    private PassengerMainActivity pvm;
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    private View mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private RelativeLayout profileLayout;
    private UserInfoDTO passenger;
    SharedPreferences userPrefs;
    NotificationManager notificationManager;

    private int notificationIdNum = 1;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main);
        String channelId = "scheduled_ride_channel";
        String channelName = "Scheduled Ride";

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This channel is for scheduled ride notifications");
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
        userPrefs = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        pvm = this;
        Call<UserInfoDTO> driverInfoDTOCall = ServiceUtils.passengerService.getPassengerById(userPrefs.getString("id","nema id"));
        driverInfoDTOCall.enqueue(new Callback<UserInfoDTO>() {
            @Override
            public void onResponse(Call<UserInfoDTO> call, Response<UserInfoDTO> response) {
                passenger = response.body();
                setPassengerInfo(passenger);

                FragmentTransition.to(PassengerMainFragment.newInstance(passenger), pvm, true,R.id.mainContent);


            }

            @Override
            public void onFailure(Call<UserInfoDTO> call, Throwable t) {

            }
        });



        createPassengerNotifSession();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_topgo_logo);
        toolbar.setTitle("");

        View logoView = toolbar.getChildAt(0);


        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragManager = pvm.getSupportFragmentManager();
                int count = pvm.getSupportFragmentManager().getBackStackEntryCount();
                Fragment frag = fragManager.getFragments().get(count > 0 ? count - 1 : count);
                if (frag.getClass().equals(PassengerMainFragment.class)) {

                } else {
                    FragmentTransition.to(PassengerMainFragment.newInstance(passenger), pvm, false, R.id.mainContent);
                }
            }
        });
        setSupportActionBar(toolbar);

        profileLayout = findViewById(R.id.profileBox);
        mTitle = getTitle();
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerList = findViewById(R.id.navList);

        mDrawerPane = findViewById(R.id.drawerPane);

        mNavItems.add(new NavItem("Inbox", "This is your inbox", R.drawable.ic_action_mail));
        mNavItems.add(new NavItem("History", "Ride history", R.drawable.ic_action_history));
        DrawerListAdapter DLA = new DrawerListAdapter(this, mNavItems);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        //mDrawerLayout.setDrawerShadow(androidx.constraintlayout.widget.R.drawable.abc_ic_star_black_48dp, GravityCompat.START);

        mDrawerList.setAdapter(DLA);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("iReviewer");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };


    }


    private void setPassengerInfo(UserInfoDTO passenger) {

        TextView driverInfo = findViewById(R.id.passengerInfoTextView);
        driverInfo.setText(passenger.getName() + " " + passenger.getSurname());

        int index = passenger.getProfilePicture().indexOf(",") + 1;
        String base64 = passenger.getProfilePicture().substring(index);
        byte[] imageBytes = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        ImageView image = findViewById(R.id.profileIcon);
        image.setImageBitmap(bitmap);

        profileLayout = findViewById(R.id.profileBox);

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(PassengerProfileFragment.newInstance(passenger), pvm, false,R.id.mainContent);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                SharedPreferences userPrefs = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userPrefs.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(PassengerMainActivity.this, UserLoginActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
            Log.i("Pisi", String.valueOf(position));

        }
    }


    private void selectItemFromDrawer(int position) {
        if (position == 0) {
            FragmentTransition.to(PassengerInboxFragment.newInstance(passenger), this, false, R.id.mainContent);
        } else if (position == 1) {
            FragmentTransition.to(PassengerDriveHistoryFragment.newInstance(passenger), this, false, R.id.mainContent);
        } else if (position == 2) {
            //..
        } else if (position == 3) {
            //..
        } else if (position == 4) {
            //..
        } else if (position == 5) {
            //...
        } else {
            Log.e("DRAWER", "Nesto van opsega!");
        }

        mDrawerList.setItemChecked(position, true);
        if (position != 5) // za sve osim za sync
        {
            setTitle(mNavItems.get(position).getmTitle());
        }
        mDrawerLayout.closeDrawer(mDrawerPane);
    }


    private void createPassengerNotifSession() {
        URI uri;
        try {
            // Connect to local host

            uri = new URI("ws://"+LOCALHOST+"/notificationSocket");

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.d("WebSocket", "Session is starting");
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
                        try {

                            message.replace("\"","");
                            Intent intent = new Intent(PassengerMainActivity.this, PassengerMainActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(PassengerMainActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(PassengerMainActivity.this, "scheduled_ride_channel")
                                    .setSmallIcon(R.drawable.topgologo)
                                    .setContentTitle("Scheduled Ride")
                                    .setContentText(message)
                                    .setContentIntent(pendingIntent)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setAutoCancel(true);

                            notificationManager.notify(notificationIdNum, builder.build());
                            notificationIdNum++;
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(PassengerMainActivity.this);
                            builder1.setMessage(message);
                            builder1.setCancelable(true);
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                        catch (Exception e){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(PassengerMainActivity.this);
                            builder1.setMessage("Something went wrong while notifying you about your scheduled ride!");
                            builder1.setCancelable(true);
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
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
        webSocketClient.addHeader("id", userPrefs.getString("id", "0"));
        webSocketClient.addHeader("role", userPrefs.getString("role", "0"));

        webSocketClient.connect();
    }


    }
