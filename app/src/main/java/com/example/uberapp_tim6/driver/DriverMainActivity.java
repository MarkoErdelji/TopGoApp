package com.example.uberapp_tim6.driver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim6.DTOS.CreateReviewResponseDTO;
import com.example.uberapp_tim6.DTOS.RejectionTextDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserRef;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.UserLoginActivity;
import com.example.uberapp_tim6.activities.MessageListActivity;
import com.example.uberapp_tim6.adapters.DrawerListAdapter;
import com.example.uberapp_tim6.adapters.ReviewAdapter;
import com.example.uberapp_tim6.driver.fragments.DriverInboxFragment;
import com.example.uberapp_tim6.driver.fragments.DriverMainFragment;
import com.example.uberapp_tim6.driver.fragments.DriverProfileFragment;
import com.example.uberapp_tim6.driver.fragments.DriverRideHistoryFragment;

import com.example.uberapp_tim6.models.NavItem;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.example.uberapp_tim6.tools.DateTimeDeserializer;
import com.example.uberapp_tim6.tools.DateTimeSerializer;
import com.example.uberapp_tim6.tools.FragmentTransition;
import com.example.uberapp_tim6.tools.LocalDateTimeDeserializer;
import com.example.uberapp_tim6.tools.TokenHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.gusavila92.websocketclient.WebSocketClient;

public class DriverMainActivity extends AppCompatActivity {

    private WebSocketClient webSocketClient;


    private DriverMainActivity dvm;
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    private View mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private RelativeLayout profileLayout;
    private ListView mDrawerList;
    private UserInfoDTO driver;
    SharedPreferences userPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dvm = this;
        userPrefs = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);


        Call<UserInfoDTO> driverInfoDTOCall = ServiceUtils.driverService.getDriverById(userPrefs.getString("id","nema id"));
        driverInfoDTOCall.enqueue(new Callback<UserInfoDTO>() {
            @Override
            public void onResponse(Call<UserInfoDTO> call, Response<UserInfoDTO> response) {
                driver = response.body();
                Log.d("Cao majstore",driver.getName() + " " + driver.getSurname());

                SetDriverInfo(driver);

                FragmentTransition.to(DriverMainFragment.newInstance(driver), dvm, false,R.id.mainContent);


            }

            @Override
            public void onFailure(Call<UserInfoDTO> call, Throwable t) {

            }
        });

        createDriverNotifSession();



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);





        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_topgo_logo);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        View logoView = toolbar.getChildAt(0);

        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragManager = dvm.getSupportFragmentManager();
                int count = dvm.getSupportFragmentManager().getBackStackEntryCount();
                Fragment frag = fragManager.getFragments().get(count>0?count-1:count);
                if (frag.getClass().equals(DriverMainFragment.class)){

                }
                else{
                    FragmentTransition.to(DriverMainFragment.newInstance(driver), dvm, false,R.id.mainContent);
                }
            }
        });



        mTitle = getTitle();
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerList = findViewById(R.id.navList);

        mDrawerPane = findViewById(R.id.drawerPane);

        mNavItems.add(new NavItem("Inbox", "Driver inbox", R.drawable.ic_action_mail));
        mNavItems.add(new NavItem("History", "Ride history", R.drawable.history_icon));
        mNavItems.add(new NavItem("Test", "Test", R.drawable.history_icon));
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

    private void SetDriverInfo(UserInfoDTO driver) {

        TextView driverInfo = findViewById(R.id.driverInfoTextView);
        driverInfo.setText(driver.getName() + " " + driver.getSurname());

        int index = driver.getProfilePicture().indexOf(",") + 1;
        String base64 = driver.getProfilePicture().substring(index);
        byte[] imageBytes = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        ImageView image = findViewById(R.id.profileIcon);
        image.setImageBitmap(bitmap);

        profileLayout = findViewById(R.id.profileBox);

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(DriverProfileFragment.newInstance(driver), dvm, false,R.id.mainContent);
                mDrawerLayout.closeDrawers();
            }
        });
    }

    private void createDriverNotifSession(){
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://192.168.0.197:8000/websocket");
        }
        catch (URISyntaxException e) {
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
                        RideDTO rideDTO = gson.fromJson(message, RideDTO.class);
                        Log.d("Ride", rideDTO.toString());
                        showAcceptancePopUp(rideDTO);
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

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.addHeader("Authorization", "Bearer " + TokenHolder.getInstance().getJwtToken());
        webSocketClient.addHeader("id",userPrefs.getString("id","0"));
        webSocketClient.addHeader("role",userPrefs.getString("role","0"));
        webSocketClient.connect();
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
                startActivity(new Intent(DriverMainActivity.this, UserLoginActivity.class));
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
        if(position == 0){
            FragmentTransition.to(DriverInboxFragment.newInstance(), this, false,R.id.mainContent);
        }else if(position == 1){
            FragmentTransition.to(DriverRideHistoryFragment.newInstance(driver), this, false,R.id.mainContent);

        }else if(position == 2){
            startActivity(new Intent(DriverMainActivity.this, MessageListActivity.class));

        }else if(position == 3){

        }else if(position == 4){
            //..
        }else if(position == 5){
            //...
        }else{
            Log.e("DRAWER", "Nesto van opsega!");
        }

        mDrawerList.setItemChecked(position, true);
        if(position != 5) // za sve osim za sync
        {
            setTitle(mNavItems.get(position).getmTitle());
        }
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    private void showRejectionPopUp(int rideId){
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverMainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.custom_dialog_rejection_of_ride, null);
        builder.setView(dialogLayout);
        builder.setCancelable(false);
        builder.setTitle("Enter a reason for rejecting ride");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Do something when the "OK" button is clicked
                EditText reason = dialogLayout.findViewById(R.id.reason);
                // Add a "OK" button to the dialog
                RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
                rejectionTextDTO.setReason(reason.getText().toString());
                Call<RideDTO> call = ServiceUtils.rideService.cancelRide(Integer.toString(rideId), rejectionTextDTO);
                call.enqueue(new Callback<RideDTO>() {
                    @Override
                    public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                        if(response.body() != null) {
                            FragmentTransition.to(DriverMainFragment.newInstance(driver), dvm, false,R.id.mainContent);
                        }
                    }
                    @Override
                    public void onFailure(Call<RideDTO> call, Throwable t) {

                    }
                });
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void showAcceptancePopUp(RideDTO rideDTO){

            // Create an AlertDialog.Builder object
            AlertDialog.Builder builder = new AlertDialog.Builder(DriverMainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogLayout = inflater.inflate(R.layout.custom_dialog_acceptance_of_ride, null);
            builder.setView(dialogLayout);
            builder.setCancelable(false);
            TextView destination = dialogLayout.findViewById(R.id.destination_text_view);
            TextView departure = dialogLayout.findViewById(R.id.departure_text_view);
            destination.setText(rideDTO.getLocations().get(0).getDestination().getAddress());
            departure.setText(rideDTO.getLocations().get(0).getDeparture().getAddress());
            showPassengers(rideDTO, dialogLayout);

            // Add a "OK" button to the dialog
            builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Do something when the "OK" button is clicked
                    Call<RideDTO> call = ServiceUtils.rideService.acceptRide(Integer.toString(rideDTO.getId()));
                    call.enqueue(new Callback<RideDTO>() {
                        @Override
                        public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                            if(response.body() != null) {
                                FragmentTransition.to(DriverMainFragment.newInstance(driver), dvm, false,R.id.mainContent);
                            }
                        }
                        @Override
                        public void onFailure(Call<RideDTO> call, Throwable t) {

                        }
                    });
                }
            });
            builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    showRejectionPopUp(rideDTO.getId());
                }
            });

            // Create and show the dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            }

    private void showPassengers(RideDTO rideDTO, View dialogView) {
        RelativeLayout passengerIcons = dialogView.findViewById(R.id.passengerIcons);
        List<UserRef> passengers = rideDTO.getPassengers();
        final int[] previousId = {0};
        for (int i = 0; i < passengers.size(); i++) {
            UserRef passenger = passengers.get(i);
            Call<UserInfoDTO> call = ServiceUtils.passengerService.getPassengerById(passenger.getId().toString());
            call.enqueue(new Callback<UserInfoDTO>() {
                @Override
                public void onResponse(Call<UserInfoDTO> call, Response<UserInfoDTO> response) {
                    UserInfoDTO user = response.body();
                    RelativeLayout passengerLayout = new RelativeLayout(dialogView.getContext());
                    passengerLayout.setId(View.generateViewId());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    if (previousId[0] != 0) {
                        layoutParams.addRule(RelativeLayout.RIGHT_OF, previousId[0]);
                    }
                    layoutParams.setMargins(10, 10, 10, 10);
                    passengerLayout.setLayoutParams(layoutParams);
                    // Create new passenger icon
                    CircleImageView passengerIcon = new CircleImageView(dvm.getApplicationContext());
                    passengerIcon.setId(View.generateViewId());
                    passengerIcon.setLayoutParams(new RelativeLayout.LayoutParams(100, 100));
                    passengerIcon.setImageResource(R.drawable.tate);
                    layoutParams = (RelativeLayout.LayoutParams) passengerIcon.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                    passengerIcon.setLayoutParams(layoutParams);
                    passengerLayout.addView(passengerIcon);
                    // Create new passenger name TextView
                    TextView passengerName = new TextView(dvm.getApplicationContext());
                    passengerName.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    passengerName.setText(user.getName() + " " + user.getSurname());
                    layoutParams = (RelativeLayout.LayoutParams) passengerName.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    layoutParams.addRule(RelativeLayout.BELOW, passengerIcon.getId());
                    passengerName.setLayoutParams(layoutParams);
                    passengerLayout.addView(passengerName);
                    passengerIcons.addView(passengerLayout);
                    previousId[0] = passengerLayout.getId();

                }

                @Override
                public void onFailure(Call<UserInfoDTO> call, Throwable t) {

                }
            });

        }

    }

}
