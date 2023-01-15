package com.example.uberapp_tim6.driver.fragments;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim6.DTOS.GeoLocationDTO;
import com.example.uberapp_tim6.DTOS.PanicDTO;
import com.example.uberapp_tim6.DTOS.ReasonDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserRef;
import com.example.uberapp_tim6.DTOS.VehicleInfoDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.models.enumerations.Status;
import com.example.uberapp_tim6.services.MapService;
import com.example.uberapp_tim6.services.RideService;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.example.uberapp_tim6.tools.DateTimeDeserializer;
import com.example.uberapp_tim6.tools.DateTimeSerializer;
import com.example.uberapp_tim6.tools.TokenHolder;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.gusavila92.websocketclient.WebSocketClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverMainFragment extends Fragment {

    private static final String ARG_DRIVER = "arg_driver";
    private WebSocketClient webSocketClient;

    private View view;

    private Marker carMarker;

    private UserInfoDTO driver;
    private DriverMainFragment fragment;
    private MapView map;
    private AppBarLayout appBarLayout;
    private Button panicButton;
    private Button endButton;
    private Button startButton;
    private RideDTO activeRide;
    private boolean validActive;
    private boolean validAccepted;
    private TextView status;
    private VehicleInfoDTO vehicle;
    private GeoLocationDTO center;

    SharedPreferences userPrefs;

    public static DriverMainFragment newInstance(UserInfoDTO d) {
        DriverMainFragment fragment = new DriverMainFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DRIVER, d);
        fragment.setArguments(bundle);
        return fragment;
    }


    public DriverMainFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        driver = (UserInfoDTO) getArguments().getSerializable(ARG_DRIVER);
        fragment = this;
        userPrefs = getContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        createDriverVehicleLocationNotifSession();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Context ctx = this.getContext();
        Configuration.getInstance().load(ctx, getDefaultSharedPreferences(ctx));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_driver_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        panicButton = view.findViewById(R.id.panic_button);
        endButton = view.findViewById(R.id.end_button);
        startButton = view.findViewById(R.id.start_button);
        appBarLayout = view.findViewById(R.id.app_bar);
        status = view.findViewById(R.id.status_text);
        View v = view.findViewById(R.id.bottom_navigation_container);
        View appBar = view.findViewById(R.id.app_bar);
        ImageView arrow = view.findViewById(R.id.bottom_drawer_show_icon);
        BottomSheetBehavior<View> bsb = BottomSheetBehavior.from(v);
        bsb.setPeekHeight(appBar.getHeight()+100);

        bsb.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                }

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // do stuff when the drawer is collapsed
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // do stuff during the actual drag event for example
                // animating a background color change based on the offset

                // or for example hidding or showing a fab
                if (slideOffset > 0.2) {
                } else if (slideOffset < 0.15) {
                }
            }

        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bsb.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if(bsb.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
        checkForCurrentRide();

        map = view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        center = new GeoLocationDTO(45.2396f,19.8227f);
        MapService.ZoomTo(center,14.0,map);



        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PanicDialog();

            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<RideDTO> call = ServiceUtils.rideService.startRide(activeRide.getId().toString());
                call.enqueue(new Callback<RideDTO>() {
                    @Override
                    public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                        SetCurrentRide(response.body());
                        carMarker = MapService.DrawMarker(vehicle.getCurrentLocation(),R.drawable.car_icon,map,getContext());
                        simulate(response.body());
                    }

                    @Override
                    public void onFailure(Call<RideDTO> call, Throwable t) {

                    }
                });

            }
        });
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<RideDTO> call = ServiceUtils.rideService.endRide(activeRide.getId().toString());
                call.enqueue(new Callback<RideDTO>() {
                    @Override
                    public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                        SetCurrentRide(response.body());
                        MapService.ZoomTo(center,14.0,map);
                        endRideDialog(response.body());
                    }

                    @Override
                    public void onFailure(Call<RideDTO> call, Throwable t) {

                    }
                });


            }
        });
    }

    private void endRideDialog(RideDTO body) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.end_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        Button okButton = dialogView.findViewById(R.id.btn_ok);
        TextView price = dialogView.findViewById(R.id.price_txt);
        TextView endTime = dialogView.findViewById(R.id.end_time_txt);
        price.setText("Price: " + (int) body.getTotalCost());
        endTime.setText("End time: " + body.getEndTime().getHour() + ":" + body.getEndTime().getMinute());
        final AlertDialog dialog = builder.create();
        dialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }

    private void PanicDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.panic_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        Button okButton = dialogView.findViewById(R.id.btn_ok);
        Button cancelButton = dialogView.findViewById(R.id.btn_cancel);
        EditText reason = dialogView.findViewById(R.id.et_message);

        final AlertDialog dialog = builder.create();
        dialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<PanicDTO> call = ServiceUtils.rideService.panicRide(new ReasonDTO(reason.getText().toString()),activeRide.getId().toString());
                call.enqueue(new Callback<PanicDTO>() {

                    @Override
                    public void onResponse(Call<PanicDTO> call, Response<PanicDTO> response) {

                        response.body().getRide().setStatus(Status.PANIC);
                        SetCurrentRide(response.body().getRide());
                        carMarker = MapService.DrawMarker(vehicle.getCurrentLocation(),R.drawable.car_icon,map,getContext());
                        MapService.ZoomTo(vehicle.getCurrentLocation(),20.0,map);



                    }

                    @Override
                    public void onFailure(Call<PanicDTO> call, Throwable t) {

                    }
                });
                dialog.dismiss();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


            }
        });



    }

    private void setButtonsVisibility(int end, int panic, int start) {
        endButton.setVisibility(end);
        panicButton.setVisibility(panic);
        startButton.setVisibility(start);
    }

    private void checkForCurrentRide() {
        checkForActiveRide();

    }
    private boolean checkForAcceptedRide() {

        Call<RideDTO> call = ServiceUtils.rideService.getDriverAcceptedRide(driver.getId().toString());
        call.enqueue(new Callback<RideDTO>(){
            @Override
            public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                if (response.body() != null) {
                    SetCurrentRide(response.body());


                    Call<VehicleInfoDTO> vehicleCall = ServiceUtils.driverService.getDriverVehicle(driver.getId().toString());
                    vehicleCall.enqueue(new Callback<VehicleInfoDTO>() {
                        @Override
                        public void onResponse(Call<VehicleInfoDTO> call, Response<VehicleInfoDTO> response1) {
                            vehicle = response1.body();
                            GeoLocationDTO departureLocation= response1.body().getCurrentLocation();
                            GeoLocationDTO destinationLocation= activeRide.getLocations().get(0).getDeparture();
                            MapService.getRoute(departureLocation,destinationLocation,R.drawable.destination_marker,R.drawable.destination_marker,map,getContext());
                            carMarker = MapService.DrawMarker(response1.body().currentLocation,R.drawable.car_icon,map,getContext());
                            MapService.ZoomTo(response1.body().currentLocation,16.0,map);
                            simulate(response.body());

                        }

                        @Override
                        public void onFailure(Call<VehicleInfoDTO> call, Throwable t) {

                        }
                    });



                }
                else
                {
                    appBarLayout.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<RideDTO> call, Throwable t) {

            }
        });
    return false;
    }

    private boolean checkForActiveRide() {
        Call<RideDTO> call = ServiceUtils.rideService.getDriverActiveRide(driver.getId().toString());
        call.enqueue(new Callback<RideDTO>() {
            @Override
            public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                if (response.body() != null) {


                    SetCurrentRide(response.body());

                    Call<VehicleInfoDTO> vehicleCall = ServiceUtils.driverService.getDriverVehicle(driver.getId().toString());
                    vehicleCall.enqueue(new Callback<VehicleInfoDTO>() {
                        @Override
                        public void onResponse(Call<VehicleInfoDTO> call, Response<VehicleInfoDTO> response) {
                            carMarker = MapService.DrawMarker(response.body().currentLocation,R.drawable.car_icon,map,getContext());
                            MapService.ZoomTo(response.body().currentLocation,16.0,map);


                        }
                        @Override
                        public void onFailure(Call<VehicleInfoDTO> call, Throwable t) {

                        }

                    });


                }
                else
                {
                    checkForAcceptedRide();
                }
            }

            @Override
            public void onFailure(Call<RideDTO> call, Throwable t) {

            }
        });
        return false;
    }

    private void SetCurrentRide(RideDTO response) {
        activeRide = response;
        ShowActiveRide(activeRide);
        TextView destination = view.findViewById(R.id.destination_text_view);
        TextView departure = view.findViewById(R.id.departure_text_view);
        destination.setText(activeRide.getLocations().get(0).getDestination().getAddress());
        departure.setText(activeRide.getLocations().get(0).getDeparture().getAddress());
        TextView time = view.findViewById(R.id.start_time_text);
        time.setText("Starting Time: " + activeRide.getStartTime().getHour()+":" + activeRide.getStartTime().getMinute());
        status.setText(activeRide.getStatus().toString());
        GeoLocationDTO departureLocation= activeRide.getLocations().get(0).getDeparture();
        GeoLocationDTO destinationLocation= activeRide.getLocations().get(0).getDestination();

        if(activeRide.getStatus().toString().equals("ACTIVE"))
        {
            map.getOverlays().clear();
            setButtonsVisibility(View.VISIBLE,View.VISIBLE,View.GONE);
            MapService.getRoute(departureLocation,destinationLocation,R.drawable.destination_marker,R.drawable.destination_marker,map,getContext());
        }
        if(activeRide.getStatus().toString().equals("ACCEPTED"))
        {
            map.getOverlays().clear();
            setButtonsVisibility(View.GONE,View.GONE,View.VISIBLE);
        }
        if(activeRide.getStatus().toString().equals("FINISHED"))
        {
            map.getOverlays().clear();
            setButtonsVisibility(View.GONE,View.GONE,View.GONE);
        }
        if(activeRide.getStatus().toString().equals("PANIC"))
        {
            map.getOverlays().clear();
            setButtonsVisibility(View.GONE,View.GONE,View.GONE);
        }
    }

    private void ShowActiveRide(RideDTO body) {
        RelativeLayout passengerIcons = view.findViewById(R.id.passengerIcons);
        List<UserRef> passengers = body.getPassengers();
        final int[] previousId = {0};
        for (int i = 0; i < passengers.size(); i++) {
            UserRef passenger = passengers.get(i);
            Call<UserInfoDTO> call = ServiceUtils.passengerService.getPassengerById(passenger.getId().toString());
            call.enqueue(new Callback<UserInfoDTO>() {
                @Override
                public void onResponse(Call<UserInfoDTO> call, Response<UserInfoDTO> response) {
                    UserInfoDTO user = response.body();
                    RelativeLayout passengerLayout = new RelativeLayout(fragment.getContext());
                    passengerLayout.setId(View.generateViewId());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                    if(previousId[0] !=0){
                        layoutParams.addRule(RelativeLayout.RIGHT_OF, previousId[0]);
                    }
                    layoutParams.setMargins(10,10,10,10);
                    passengerLayout.setLayoutParams(layoutParams);
                    // Create new passenger icon
                    CircleImageView passengerIcon = new CircleImageView(fragment.getContext());
                    passengerIcon.setId(View.generateViewId());
                    passengerIcon.setLayoutParams(new RelativeLayout.LayoutParams(100, 100));
                    passengerIcon.setImageResource(R.drawable.tate);
                    layoutParams = (RelativeLayout.LayoutParams) passengerIcon.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                    passengerIcon.setLayoutParams(layoutParams);
                    passengerLayout.addView(passengerIcon);
                    // Create new passenger name TextView
                    TextView passengerName = new TextView(fragment.getContext());
                    passengerName.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT));
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

    private void createDriverVehicleLocationNotifSession(){
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://192.168.0.197:8000/simulation");
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new DateTimeDeserializer());
                        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new DateTimeSerializer());
                        Gson gson = gsonBuilder.create();
                        GeoLocationDTO geoLocationDTO = gson.fromJson(message, GeoLocationDTO.class);
                        if(carMarker != null) {
                            carMarker.remove(map);
                            carMarker = MapService.DrawMarker(geoLocationDTO,R.drawable.car_icon,map,getContext());
                        }}
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

    public void simulate(RideDTO rideDTO){
        ServiceUtils.rideService.simulate(Integer.toString(rideDTO.getId())).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("RESPONSE",response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}