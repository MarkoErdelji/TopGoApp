package com.example.uberapp_tim6.driver.fragments;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uberapp_tim6.DTOS.GeoLocationDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserRef;
import com.example.uberapp_tim6.DTOS.VehicleInfoDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.example.uberapp_tim6.tools.RouteOverlay;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverMainFragment extends Fragment {

    private static final String ARG_DRIVER = "arg_driver";


    private View view;

    private UserInfoDTO driver;
    private DriverMainFragment fragment;
    private MapView map;
    private AppBarLayout appBarLayout;
    private Button panicButton;
    private Button endButton;
    private RideDTO activeRide;

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
        appBarLayout = view.findViewById(R.id.app_bar);
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

        checkForActiveRide();

        map = view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(45.2517, 19.8369);

        map.getController().setZoom(15.0);
        map.getController().animateTo(startPoint);

        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<RideDTO> call = ServiceUtils.rideService.endRide(activeRide.getId().toString());
                call.enqueue(new Callback<RideDTO>() {
                    @Override
                    public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                        Log.d("Status: " ,response.body().getStatus().toString());
                    }

                    @Override
                    public void onFailure(Call<RideDTO> call, Throwable t) {

                    }
                });


            }
        });
    }

    private void checkForActiveRide() {
        Call<RideDTO> call = ServiceUtils.rideService.getDriverActiveRide(driver.getId().toString());
        call.enqueue(new Callback<RideDTO>() {
            @Override
            public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                if (response.body() != null) {
                    activeRide = response.body();
                    ShowActiveRide(response.body());
                    TextView destination = view.findViewById(R.id.destination_text_view);
                    TextView departure = view.findViewById(R.id.departure_text_view);
                    destination.setText(response.body().getLocations().get(0).getDestination().getAddress());
                    departure.setText(response.body().getLocations().get(0).getDeparture().getAddress());
                    GeoLocationDTO departureLocation= response.body().getLocations().get(0).getDeparture();
                    GeoLocationDTO destinationLocation= response.body().getLocations().get(0).getDestination();
                    getRoute(departureLocation,destinationLocation);
                    Call<VehicleInfoDTO> vehicleCall = ServiceUtils.driverService.getDriverVehicle(driver.getId().toString());
                    TextView time = view.findViewById(R.id.start_time_text);
                    time.setText("Starting Time: " + response.body().getStartTime().getHour()+":" + response.body().getStartTime().getMinute());
                    TextView status = view.findViewById(R.id.status_text);
                    status.setText(response.body().getStatus().toString());

                    vehicleCall.enqueue(new Callback<VehicleInfoDTO>() {
                        @Override
                        public void onResponse(Call<VehicleInfoDTO> call, Response<VehicleInfoDTO> response) {
                            DrawMarker(response.body().currentLocation,R.drawable.car_icon);
                        }

                        @Override
                        public void onFailure(Call<VehicleInfoDTO> call, Throwable t) {

                        }
                    });
                }
                else
                {
                    appBarLayout.setVisibility(View.GONE);
                    Log.d("Cao majstore", "Driver has no active rides");
                }

            }

            @Override
            public void onFailure(Call<RideDTO> call, Throwable t) {
                Log.d("Cao majstore",t.getMessage());



            }
        });
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

    private void getRoute(GeoLocationDTO departure, GeoLocationDTO destination) {

        String apiKey = "5b3ce3597851110001cf624865f18297bb26459a9f779c015d573b96";
        String baseUrl = "https://api.openrouteservice.org/v2/directions/driving-car";
        String start = departure.getLongitude() + "," + departure.getLatitude();
        String end = destination.getLongitude() + "," + destination.getLatitude();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.openrouteservice.org")
                .addPathSegment("v2")
                .addPathSegment("directions")
                .addPathSegment("driving-car")
                .addQueryParameter("api_key", apiKey)
                .addQueryParameter("start", start)
                .addQueryParameter("end", end)
                .build();

        // Send the API request and parse the response
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d("REZ",e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.d("REZ", "okokoko");
                if (response.isSuccessful()) {

                    String responseString = response.body().string();
                    Log.d("REZ", responseString);
                    try {
                        // Parse the JSON response

                        JSONObject json = new JSONObject(responseString);
                        JSONArray features = json.getJSONArray("features");
                        JSONObject firstFeature = features.getJSONObject(0);
                        JSONObject geometry = firstFeature.getJSONObject("geometry");
                        JSONArray coordinates = geometry.getJSONArray("coordinates");
                        List<GeoPoint> routePoints = new ArrayList<>();
                        for (int i = 0; i < coordinates.length(); i++) {
                            JSONArray coord = coordinates.getJSONArray(i);

                            double lon = coord.getDouble(0);
                            double lat = coord.getDouble(1);
                            routePoints.add(new GeoPoint(lat, lon));
                        }
                        // Add the route overlay to the map
                        DrawRoute(routePoints,departure,destination);
                    } catch (JSONException e) {
                        Log.d("ERRROOOR", e.getMessage());
                    }
                } else {
                    Log.d("REZ", "Request failed with code: " + response.code());
                    Log.d("REZ", "Response message: " + response.message());
                    String responseString = response.body().string();
                    Log.d("REZ", "Response Body: " + responseString);
                }
            }
        });
    }


    private void DrawMarker(GeoLocationDTO location,int icon)
    {
        Bitmap customIcon = BitmapFactory.decodeResource(getResources(),icon);
        customIcon = Bitmap.createScaledBitmap(customIcon, 100, 100, false);
        BitmapDrawable customIconDrawable = new BitmapDrawable(getResources(), customIcon);

        Marker marker = new Marker(map);

        marker.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
        marker.setIcon(customIconDrawable);
        marker.setAnchor(0.5f,1f);
        map.getOverlays().add(marker);
        map.invalidate();

    }

    private void DrawRoute(List<GeoPoint> routePoints, GeoLocationDTO departure, GeoLocationDTO destination) {



        map.getOverlays().add(new RouteOverlay(routePoints));
        DrawMarker(departure,R.drawable.destination_marker);
        DrawMarker(destination,R.drawable.destination_marker);
        map.invalidate();
    }
}