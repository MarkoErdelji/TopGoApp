package com.example.uberapp_tim6.passenger.fragments;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.uberapp_tim6.DTOS.CreateRideDTO;
import com.example.uberapp_tim6.DTOS.JWTTokenDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.RidePassengerDTO;
import com.example.uberapp_tim6.DTOS.RouteForCreateRideDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.models.enumerations.VehicleName;
import com.example.uberapp_tim6.services.MapService;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerMainFragment extends Fragment {

    private EditText departureEditText;

    private EditText destinationEditText;

    private Button step1Order;

    private Spinner spinnerVehicleType;

    MapView map;

    private View step1;
    private View step2;
    private View step3;
    private View step4;

    private CheckBox babyCheckbox;
    private CheckBox petCheckbox;
    private EditText numOfSeatsInput;

    private Button step2Next;
    private Button step3Next;
    private Button step4Order;

    private AppCompatButton step3Back;
    private AppCompatButton step4Back;
    private AppCompatButton backBtn;

    public PassengerMainFragment() {
        // Required empty public constructor
    }

    public static PassengerMainFragment newInstance() {
        return new PassengerMainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://192.168.0.197:8000/websocket");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        Context ctx = this.getContext();
        Configuration.getInstance().load(ctx, getDefaultSharedPreferences(ctx));



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passenger_main, container, false);



    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        map = view.findViewById(R.id.map);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(45.2517, 19.8369);

        map.getController().setZoom(15.0);
        map.getController().animateTo(startPoint);

        View v = view.findViewById(R.id.bottom_navigation_container);
        View appBar = view.findViewById(R.id.app_bar);
        BottomSheetBehavior<View> bsb = BottomSheetBehavior.from(v);
        bsb.setPeekHeight(appBar.getHeight()+100);
        ImageView arrow = view.findViewById(R.id.bottom_drawer_show_icon);
        departureEditText = view.findViewById(R.id.departure_edit_text);
        destinationEditText = view.findViewById(R.id.destination_edit_text);
        step1Order = view.findViewById(R.id.create_ride_button);
        step1 = view.findViewById(R.id.route_creation_layout);
        step2 = view.findViewById(R.id.create_ride_spinner);
        step3 = view.findViewById(R.id.create_ride_referral);
        step4 = view.findViewById(R.id.create_ride_option_select);
        spinnerVehicleType = view.findViewById(R.id.spinner_vehicle_type);
        babyCheckbox = view.findViewById(R.id.checkbox_babies);
        petCheckbox = view.findViewById(R.id.checkbox_pets);
        numOfSeatsInput = view.findViewById(R.id.sets_input);
        step4Order = view.findViewById(R.id.order_ride_button);
        backBtn = view.findViewById(R.id.step2_back_button);
        step3Back = view.findViewById(R.id.step3_back_button);
        step4Back = view.findViewById(R.id.step4_back_button);
        step2Next = view.findViewById(R.id.step2_button);
        step3Next = view.findViewById(R.id.step4_button);

        ArrayAdapter ad
                = ArrayAdapter.createFromResource(
                this.getContext(),
                R.array.vehicle_type,
                android.R.layout.simple_spinner_item
        );
        numOfSeatsInput.setText("2");
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicleType.setAdapter(ad);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bsb.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if(bsb.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        CreateRideDTO createRideDTO = new CreateRideDTO();
        step1Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.getOverlays().clear();
                List<RouteForCreateRideDTO> routeForCreateRideDTOS = new ArrayList<>();
                RouteForCreateRideDTO routeForCreateRideDTO = new RouteForCreateRideDTO();
                MapService.getLocation(String.valueOf(departureEditText.getText()), geolocationDto ->{
                    MapService.getLocation(String.valueOf(destinationEditText.getText()),geolocationDto2->{
                        routeForCreateRideDTO.setDeparture(geolocationDto2);
                        routeForCreateRideDTO.setDestination(geolocationDto);
                        routeForCreateRideDTOS.add(routeForCreateRideDTO);
                        createRideDTO.setLocations(routeForCreateRideDTOS);
                        Log.d("HHH",routeForCreateRideDTO.getDeparture().toString());
                        Log.d("HHH",routeForCreateRideDTO.getDestination().toString());
                        MapService.getRoute(routeForCreateRideDTO.getDeparture(),routeForCreateRideDTO.getDestination(),R.drawable.destination_marker,R.drawable.destination_marker,map,getContext());
                    });
                });
                step1.setVisibility(View.GONE);
                step2.setVisibility(View.VISIBLE);
            }
        });

        step2Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step2.setVisibility(View.GONE);
                step3.setVisibility(View.VISIBLE);
            }
        });

        step3Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step3.setVisibility(View.GONE);
                step4.setVisibility(View.VISIBLE);
            }
        });

        step3Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step2.setVisibility(View.VISIBLE);
                step3.setVisibility(View.GONE);
            }
        });
        step4Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step3.setVisibility(View.VISIBLE);
                step4.setVisibility(View.GONE);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step1.setVisibility(View.VISIBLE);
                step2.setVisibility(View.GONE);
            }
        });
        SharedPreferences userPrefs = getContext().getSharedPreferences("userPrefs",Context.MODE_PRIVATE);

        step4Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<RidePassengerDTO> ridePassengerDTOS = new ArrayList<>();
                RidePassengerDTO ridePassengerDTO = new RidePassengerDTO();
                ridePassengerDTO.setEmail(userPrefs.getString("email","no mail"));
                ridePassengerDTO.setId(Integer.valueOf(userPrefs.getString("id","0")));
                ridePassengerDTOS.add(ridePassengerDTO);
                createRideDTO.setPassengers(ridePassengerDTOS);
                createRideDTO.setBabyTransport(babyCheckbox.isChecked());
                createRideDTO.setPetTransport(petCheckbox.isChecked());
                createRideDTO.setVehicleType(VehicleName.valueOf(spinnerVehicleType.getSelectedItem().toString()));
                Call<RideDTO> createRideCall = ServiceUtils.rideService.createRide(createRideDTO);
                createRideCall.enqueue(new Callback<RideDTO>() {
                    @Override
                    public void onResponse(Call<RideDTO> call, retrofit2.Response<RideDTO> response) {
                        if (response.code() == 200) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                            builder1.setMessage("Success!");
                            builder1.setCancelable(true);
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        } else if (response.code() == 400) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                            builder1.setMessage("Something went wrong,please make sure to wait for the route to load on the map before ordering ride");
                            builder1.setCancelable(true);
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RideDTO> call, Throwable t) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setMessage("Something went wrong,please make sure to wait for the route to load on the map before ordering ride");
                        builder1.setCancelable(true);
                        AlertDialog alert11 = builder1.create();
                        alert11.show();

                    }
                });
                Log.d("RideDTO",createRideDTO.toString());
                step2.setVisibility(View.GONE);
                step1.setVisibility(View.VISIBLE);
            }
        });
        bsb.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    // do stuff when the drawer is expanded
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
        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}