package com.example.uberapp_tim6.passenger.fragments;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
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
import com.example.uberapp_tim6.DTOS.RouteForCreateRideDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.services.MapService;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

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

    private CheckBox babyCheckbox;
    private CheckBox petCheckbox;
    private EditText numOfSeatsInput;

    private Button step2Order;
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
        step2 = view.findViewById(R.id.create_ride_option_select);
        spinnerVehicleType = view.findViewById(R.id.spinner_vehicle_type);
        babyCheckbox = view.findViewById(R.id.checkbox_babies);
        petCheckbox = view.findViewById(R.id.checkbox_pets);
        numOfSeatsInput = view.findViewById(R.id.sets_input);
        step2Order = view.findViewById(R.id.order_ride_button);
        backBtn = view.findViewById(R.id.step2_back_button);

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
                routeForCreateRideDTO.setDeparture(MapService.getLocation("Strazilovska 12,Novi Sad"));
                routeForCreateRideDTO.setDestination(MapService.getLocation("Bulevara cara lazara 48,Novi Sad"));
                routeForCreateRideDTOS.add(routeForCreateRideDTO);
                createRideDTO.setLocations(routeForCreateRideDTOS);
                List<GeoPoint> geoPoints = new ArrayList<>();
                GeoPoint geoPointdeparture = new GeoPoint(routeForCreateRideDTO.getDeparture().getLatitude(),routeForCreateRideDTO.getDeparture().getLongitude());
                GeoPoint geoPointDestionation = new GeoPoint(routeForCreateRideDTO.getDestination().getLatitude(),routeForCreateRideDTO.getDestination().getLongitude());
                geoPoints.add(geoPointdeparture);
                geoPoints.add(geoPointDestionation);
                MapService.DrawRoute(geoPoints,routeForCreateRideDTO.getDeparture(),routeForCreateRideDTO.getDestination(),R.drawable.destination_marker,R.drawable.destination_marker,map,getContext());
                step1.setVisibility(View.GONE);
                step2.setVisibility(View.VISIBLE);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step1.setVisibility(View.VISIBLE);
                step2.setVisibility(View.GONE);
            }
        });

        step2Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
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