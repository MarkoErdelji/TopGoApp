package com.example.uberapp_tim6.passenger.fragments;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import static com.example.uberapp_tim6.services.ServiceUtils.LOCALHOST;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.util.Pair;
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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uberapp_tim6.DTOS.CreateReviewDTO;
import com.example.uberapp_tim6.DTOS.CreateReviewResponseDTO;
import com.example.uberapp_tim6.DTOS.CreateRideDTO;

import com.example.uberapp_tim6.DTOS.DriverInfoDTO;
import com.example.uberapp_tim6.DTOS.GeoLocationDTO;
import com.example.uberapp_tim6.DTOS.JWTTokenDTO;
import com.example.uberapp_tim6.DTOS.PanicDTO;
import com.example.uberapp_tim6.DTOS.ReasonDTO;
import com.example.uberapp_tim6.DTOS.RejectionTextDTO;

import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.RidePassengerDTO;
import com.example.uberapp_tim6.DTOS.RouteForCreateRideDTO;
import com.example.uberapp_tim6.DTOS.TimeAndDistanceDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.VehicleInfoDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.activities.MessageListActivity;
import com.example.uberapp_tim6.driver.fragments.DriverMainFragment;
import com.example.uberapp_tim6.models.Ride;
import com.example.uberapp_tim6.models.enumerations.Status;
import com.example.uberapp_tim6.models.enumerations.VehicleName;
import com.example.uberapp_tim6.services.MapService;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.example.uberapp_tim6.tools.DateTimeDeserializer;
import com.example.uberapp_tim6.tools.DateTimeSerializer;
import com.example.uberapp_tim6.tools.TokenHolder;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.TileStates;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.w3c.dom.Text;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifDrawable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.gusavila92.websocketclient.WebSocketClient;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerMainFragment extends Fragment implements DatePickerDialog.OnDateSetListener, com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {

    private WebSocketClient webSocketClient;

    private static final String ARG_PASSENGER = "arg_PASSENGER";

    private UserInfoDTO passenger;

    private Context context;
    private PassengerMainFragment fragment;

    private EditText departureEditText;

    private EditText destinationEditText;

    private Button step1Order;

    private Spinner spinnerVehicleType;

    MapView map;


    private View step1;
    private View step2;
    private View step3;
    private View step4;
    private View waiting;

    private View latestActiveView;
    private View currentRideView;
    private CheckBox babyCheckbox;
    private CheckBox petCheckbox;
    private EditText numOfSeatsInput;

    private Button step2Next;
    private Button step4Order;


    private AppCompatButton step4Back;
    AlertDialog dialog;
    private AppCompatButton backBtn;
    private View dialogView;
    private Marker carMarker;


    private SharedPreferences userPrefs;
    Calendar now = Calendar.getInstance();

    public PassengerMainFragment() {
        // Required empty public constructor
    }

    public static PassengerMainFragment newInstance(UserInfoDTO passenger) {
        PassengerMainFragment fragment = new PassengerMainFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PASSENGER, passenger);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passenger = (UserInfoDTO) getArguments().getSerializable(ARG_PASSENGER);
        fragment = this;
        userPrefs = getContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
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
    Calendar setDate = Calendar.getInstance();



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setDate.clear();
        now.add(Calendar.HOUR,1);
        super.onViewCreated(view, savedInstanceState);
        userPrefs = getContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        createDriverVehicleLocationNotifSession();
        createPassengerNotifSession();
        currentRideView = view.findViewById(R.id.passengerCurrentRide);

        map = (MapView) view.findViewById(R.id.map);
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
        latestActiveView = step1;
        step2 = view.findViewById(R.id.create_ride_spinner);
        step4 = view.findViewById(R.id.create_ride_option_select);

        waiting = view.findViewById(R.id.passengerWaitForDriver);
        ImageView gifLoading = waiting.findViewById(R.id.loadingGif);
        GifDrawable gifDrawable = null;
        try {
            gifDrawable = new GifDrawable(getContext().getResources(), R.drawable.loading_gif);
        } catch (IOException e) {
            e.printStackTrace();
        }

        gifLoading.setImageDrawable(gifDrawable);
        spinnerVehicleType = view.findViewById(R.id.spinner_vehicle_type);
        babyCheckbox = view.findViewById(R.id.checkbox_babies);
        petCheckbox = view.findViewById(R.id.checkbox_pets);
        numOfSeatsInput = view.findViewById(R.id.sets_input);
        step4Order = view.findViewById(R.id.order_ride_button);
        backBtn = view.findViewById(R.id.step2_back_button);
        step4Back = view.findViewById(R.id.step4_back_button);
        step2Next = view.findViewById(R.id.step2_button);
        Button buttonDateTime = view.findViewById(R.id.button_date_time);
        buttonDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        PassengerMainFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAccentColor(1);
                dpd.setAccentColor("#FF9642");
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

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
                        routeForCreateRideDTO.setDeparture(geolocationDto);
                        routeForCreateRideDTO.setDestination(geolocationDto2);
                        routeForCreateRideDTOS.add(routeForCreateRideDTO);
                        createRideDTO.setLocations(routeForCreateRideDTOS);
                        MapService.getRoute(routeForCreateRideDTO.getDeparture(),routeForCreateRideDTO.getDestination(),R.drawable.destination_marker,R.drawable.destination_marker,map,getContext());
                    });
                });
                step1.setVisibility(View.GONE);
                step2.setVisibility(View.VISIBLE);
                latestActiveView = step2;
            }
        });

        step2Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step2.setVisibility(View.GONE);
                step4.setVisibility(View.VISIBLE);
                latestActiveView = step4;
            }
        });



        step4Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step2.setVisibility(View.VISIBLE);
                step4.setVisibility(View.GONE);
                latestActiveView = step2;
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step1.setVisibility(View.VISIBLE);
                step2.setVisibility(View.GONE);
                latestActiveView = step1;
            }
        });

        SharedPreferences userPrefs = getContext().getSharedPreferences("userPrefs",Context.MODE_PRIVATE);

        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.wait_for_driver, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setView(dialogView);
        dialog = builder.create();
        // dodati samo da kad naruci voznju uradi prebacaj na odgovarajuc meni
        // step4.setVisibility(Visibility.GONE)
        // wait.setVisibility(Visibility.VISIBLE)
        // latestActiveView = wait;
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
                Log.d("SETDATE:", String.valueOf(setDate.getTimeInMillis()));
                LocalDateTime selectedTime;
                if(setDate.getTimeInMillis() > 0) {
                     selectedTime = LocalDateTime.ofInstant(setDate.toInstant(), ZoneId.ofOffset("UTC", ZoneOffset.UTC));
                    if (selectedTime.plusMinutes(1).isBefore(LocalDateTime.now(ZoneId.ofOffset("UTC",ZoneOffset.UTC)))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Error")
                                .setMessage("Cannot select a time or date before now")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Dismiss the dialog
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                        return;
                    } else if (selectedTime.isAfter(LocalDateTime.now()) && selectedTime.isBefore(LocalDateTime.now().plusMinutes(15))) {
                        selectedTime = LocalDateTime.now();
                    }
                }

                else{
                    selectedTime = null;

                }
                if(selectedTime != null) {
                    createRideDTO.setScheduledTime(selectedTime.toString());
                }
                else{
                    createRideDTO.setScheduledTime(null);
                }

                Call<RideDTO> createRideCall = ServiceUtils.rideService.createRide(createRideDTO);
                createRideCall.enqueue(new Callback<RideDTO>() {
                    @Override
                    public void onResponse(Call<RideDTO> call, retrofit2.Response<RideDTO> response) {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            if(response.body().status == Status.SCHEDULED){
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setMessage("You will be notified if a driver accepts your ride,please be patient!");
                                builder1.setCancelable(true);
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                                return;
                            }
                            populateDialogAndShow(dialogView,dialog,response.body());


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
                setDate = Calendar.getInstance();
                setDate.setTime(new Date());
                Log.d("RideDTO",createRideDTO.toString());
                step2.setVisibility(View.GONE);
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

        // evaluate the tile states
        while (true){
            if(map.getRepository()==null){
                continue;
            }
            TileStates tileStates = map.getOverlayManager().getTilesOverlay().getTileStates();
            if (tileStates.getTotal() == tileStates.getUpToDate())
            {
                // map is loaded completely
                Log.d("nesto", "kurac moj");
                checkForActiveRide();
                break;
            }
        }

//        checkForAcceptedRide();
    }

    private void checkForActiveRide(){
        ServiceUtils.rideService.getPassengerActiveRide(userPrefs.getString("id", "0")).enqueue(
                new Callback<RideDTO>() {
                    @Override
                    public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {

                        if(response.isSuccessful()){
                        ServiceUtils.driverService.getDriverVehicle(String.valueOf(response.body().getDriver().getId())).enqueue(new Callback<VehicleInfoDTO>() {
                            @Override
                            public void onResponse(Call<VehicleInfoDTO> call, Response<VehicleInfoDTO> response1) {
                                if (response1.isSuccessful()) {
                                    map.getOverlays().clear();
                                    latestActiveView.setVisibility(View.GONE);
                                    waiting.setVisibility(View.VISIBLE);
                                    latestActiveView = waiting;
                                    MapService.getRoute(response1.body().currentLocation, response.body().getLocations().get(0).getDeparture(), R.drawable.destination_marker, R.drawable.destination_marker, map, getContext());
                                    carMarker = MapService.DrawMarker(response1.body().currentLocation, R.drawable.car_icon, map, getContext());
                                    MapService.ZoomTo(response1.body().currentLocation, 16.0, map);
                                    changeToCurrentRide();
                                    setCurrentRideData(response.body());
                                    Button panicBtn = currentRideView.findViewById(R.id.panicBtn);
                                    panicBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showPanicPopup(response.body());
                                        }
                                    });
                                }
                            }


                            @Override
                            public void onFailure(Call<VehicleInfoDTO> call, Throwable t) {

                            }
                            });
                            }
                                    else{
                                checkForAcceptedRide();
                            }

                }

                    @Override
                    public void onFailure(Call<RideDTO> call, Throwable t) {

                    }
        });
    }
    private void changeToCurrentRide(){
        latestActiveView.setVisibility(View.GONE);
        currentRideView.setVisibility(View.VISIBLE);
        latestActiveView = currentRideView;
    }
    private void setCurrentRideData(RideDTO rideDTO){
        CircleImageView driverProfilePic = currentRideView.findViewById(R.id.driverProfilePicture);
        TextView departureTextView = currentRideView.findViewById(R.id.departure_text_view);
        departureTextView.setText(rideDTO.getLocations().get(0).getDeparture().getAddress());

        TextView destinationTextView = currentRideView.findViewById(R.id.destination_text_view);
        destinationTextView.setText(rideDTO.getLocations().get(0).getDestination().getAddress());

        TextView driverNameAndLastName = currentRideView.findViewById(R.id.driverNameAndLastName);
        ServiceUtils.userService.getUserById(Integer.toString(rideDTO.getDriver().getId())).enqueue(
                new Callback<UserInfoDTO>() {
                    @Override
                    public void onResponse(Call<UserInfoDTO> call, Response<UserInfoDTO> response) {
                        if (response.isSuccessful()) {
                            Glide.with(getContext()).load(response.body().getProfilePicture()).into(driverProfilePic);
                            String nameAndLastName = response.body().getName() + " " + response.body().getSurname();
                            driverNameAndLastName.setText(nameAndLastName);
                            driverProfilePic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    openChat(response.body(),rideDTO);
                                }
                            });
                        }
                    }
                    @Override
                    public void onFailure(Call<UserInfoDTO> call, Throwable t) {

                    }
                });

    }
    private void openChat(UserInfoDTO user, RideDTO body) {
        String userId = user.getId().toString();

        Intent intent = new Intent(getActivity(), MessageListActivity.class);
        intent.putExtra("Sender",userId);
        intent.putExtra("Ride",body);
        intent.putExtra("text", "message.getDateTime().toString()");
        intent.putExtra("currentUser",passenger);
        startActivityForResult(intent, 0);
    }


    private void populateDialogAndShow(View dialogView, AlertDialog dialog, RideDTO ride) {
        TextView emailView = dialogView.findViewById(R.id.emailInfo);
        TextView nameSurname = dialogView.findViewById(R.id.name_surname);
        TextView phoneNumber = dialogView.findViewById(R.id.phoneNumberinfo);
        TextView carNameView = dialogView.findViewById(R.id.carNameValue);
        TextView carTypeView = dialogView.findViewById(R.id.carTypeValue);
        TextView isForPetsView = dialogView.findViewById(R.id.isForPetsValue);
        TextView isForBabiesView = dialogView.findViewById(R.id.isForBabiesValue);
        TextView departureView = dialogView.findViewById(R.id.departureValue);
        TextView destinationView = dialogView.findViewById(R.id.destinationValue);
        CircleImageView profilePic = dialogView.findViewById(R.id.driverImageValue);
        ImageView gifLoading = dialogView.findViewById(R.id.loadingGif);
        TextView distanceView = dialogView.findViewById(R.id.distanceValue);
        TextView timeView = dialogView.findViewById(R.id.timeValue);
        TextView moneyView = dialogView.findViewById(R.id.moneyValue);

        ServiceUtils.driverService.getDriverById(ride.getDriver().getId().toString()).enqueue(
                new Callback<UserInfoDTO>() {
                    @Override
                    public void onResponse(Call<UserInfoDTO> call, Response<UserInfoDTO> response) {
                        if (response.isSuccessful()) {
                            ServiceUtils.driverService.getDriverVehicle(ride.getDriver().getId().toString()).enqueue(new Callback<VehicleInfoDTO>() {
                                @Override
                                public void onResponse(Call<VehicleInfoDTO> call, Response<VehicleInfoDTO> response2) {
                                    emailView.setText(ride.getDriver().getEmail());
                                    assert response.body() != null;
                                    MapService.getDistance(ride.getLocations().get(0).getDeparture(), ride.getLocations().get(0).getDestination(), callback -> {
                                        TimeAndDistanceDTO timeAndDistanceDTO = callback;
                                        requireActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                distanceView.setText(String.valueOf(timeAndDistanceDTO.getDistanceInKm() + " KM"));
                                                timeView.setText(String.valueOf(timeAndDistanceDTO.getTimeInMinutes() + " MIN"));
                                            }
                                        });
                                    });
                                    nameSurname.setText(String.format("%s %s", response.body().getName(), response.body().getSurname()));
                                    phoneNumber.setText(response.body().getTelephoneNumber());
                                    carNameView.setText(response2.body().getModel());
                                    carTypeView.setText(response2.body().getVehicleType());
                                    isForBabiesView.setText(String.valueOf(ride.isBabyTransport()));
                                    isForPetsView.setText(String.valueOf(ride.isPetTransport()));
                                    departureView.setText(ride.getLocations().get(0).getDeparture().getAddress());
                                    destinationView.setText(ride.getLocations().get(0).getDestination().getAddress());


                                    moneyView.setText(String.valueOf(ride.getTotalCost()) + " RSD");
                                    Glide.with(getContext()).load(response.body().getProfilePicture()).into(profilePic);

                                    GifDrawable gifDrawable = null;
                                    try {
                                        gifDrawable = new GifDrawable(getContext().getResources(), R.drawable.loading_gif);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    gifLoading.setImageDrawable(gifDrawable);
                                    dialog.setCancelable(false);
                                    dialog.show();


                                }

                                @Override
                                public void onFailure(Call<VehicleInfoDTO> call, Throwable t) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfoDTO> call, Throwable t) {

                    }
                }
        );
    };

    private void showPanicPopup(RideDTO rideDTO){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.custom_dialog_rejection_of_ride, null);
        builder2.setView(dialogLayout);
        builder2.setTitle("What is happening?");
        builder2.setCancelable(false);
        builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView textView = dialogLayout.findViewById(R.id.reason);
                RejectionTextDTO rejectionTextDTO = new RejectionTextDTO();
                rejectionTextDTO.setReason(textView.getText().toString());
                Call<PanicDTO> call = ServiceUtils.rideService.panicRide(new ReasonDTO(rejectionTextDTO.getReason()),rideDTO.getId().toString());
                call.enqueue(new Callback<PanicDTO>() {

                    @Override
                    public void onResponse(Call<PanicDTO> call, Response<PanicDTO> response) {

                        map.getOverlays().clear();
                        currentRideView.setVisibility(View.GONE);
                        step1.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onFailure(Call<PanicDTO> call, Throwable t) {

                    }
                });

            }
        });
        AlertDialog dialog = builder2.create();
        dialog.show();
    }


    private void createPassengerNotifSession() {
        URI uri;
        try {
            // Connect to local host

            uri = new URI("ws://"+LOCALHOST+"/websocket");

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
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new DateTimeDeserializer());
                        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new DateTimeSerializer());
                        Gson gson = gsonBuilder.create();
                        try {
                            RideDTO rideDTO = gson.fromJson(message, RideDTO.class);
                            if (rideDTO.getStatus() == Status.ACCEPTED) {
                                dialog.dismiss();
                                latestActiveView.setVisibility(View.GONE);
                                waiting.setVisibility(View.VISIBLE);
                                latestActiveView = waiting;
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setMessage("Your ride was accepted!");
                                builder1.setCancelable(true);
                                setAcceptedRide(rideDTO);

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                            if (rideDTO.getStatus() == Status.PENDING) {
                                dialog.dismiss();
                                populateDialogAndShow(dialogView, dialog, rideDTO);
                            }
                            if (rideDTO.getStatus() == Status.ACTIVE){
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setMessage("Your driver has arrived, ride is about to start.");
                                builder1.setCancelable(true);
                                map.getOverlays().clear();
                                MapService.getRoute(rideDTO.getLocations().get(0).getDeparture(), rideDTO.getLocations().get(0).getDestination(),R.drawable.destination_marker,R.drawable.destination_marker,map,getContext());
                                carMarker = MapService.DrawMarker(rideDTO.getLocations().get(0).getDeparture(),R.drawable.car_icon,map,getContext());
                                MapService.ZoomTo(rideDTO.getLocations().get(0).getDeparture(),16.0,map);
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                                changeToCurrentRide();
                                setCurrentRideData(rideDTO);
                                Button panicBtn = currentRideView.findViewById(R.id.panicBtn);
                                panicBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showPanicPopup(rideDTO);
                                    }
                                });
                            }
                            if(rideDTO.getStatus() == Status.FINISHED){
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                LayoutInflater inflater = getLayoutInflater();
                                View dialogLayout = inflater.inflate(R.layout.finished_ride_popup, null);
                                builder1.setView(dialogLayout);
                                builder1.setCancelable(true);
                                //builder1.setTitle("You arrived at your destination!");
                                TextView dateTextView = dialogLayout.findViewById(R.id.datetime);
                                dateTextView.setText(rideDTO.getEndTime().toString());

                                TextView price = dialogLayout.findViewById(R.id.priceTextView);
                                String priceStr = "PRICE: " + rideDTO.getTotalCost();
                                price.setText(priceStr);

                                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //do something
                                        showReviewPopUp(rideDTO);

                                    }
                                });
                                builder1.setNegativeButton("No", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        map.getOverlays().clear();
                                        currentRideView.setVisibility(View.GONE);
                                        step1.setVisibility(View.VISIBLE);
                                    }
                                });
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                            if(rideDTO.getStatus() == Status.SCHEDULED){
                                dialog.dismiss();
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                LayoutInflater inflater = getLayoutInflater();
                                View dialogLayout = inflater.inflate(R.layout.finished_ride_popup, null);
                                builder1.setView(dialogLayout);
                                builder1.setCancelable(true);
                                builder1.setTitle("Your ride scheduled for " + rideDTO.startTime + " has been accepted by one of our drivers!");
                                TextView dateTextView = dialogLayout.findViewById(R.id.datetime);
                                dateTextView.setText(rideDTO.getEndTime().toString());

                                TextView price = dialogLayout.findViewById(R.id.priceTextView);
                                String priceStr = "PRICE: " + rideDTO.getTotalCost();
                                price.setText(priceStr);

                                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //do something
                                        showReviewPopUp(rideDTO);

                                    }
                                });
                                builder1.setNegativeButton("No", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        map.getOverlays().clear();
                                        currentRideView.setVisibility(View.GONE);
                                        step1.setVisibility(View.VISIBLE);
                                        latestActiveView = step1;
                                    }
                                });
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            }
                            if(rideDTO.status == Status.REJECTED){

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setMessage("Your ride is canceled.");
                                builder1.setCancelable(true);
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                                map.getOverlays().clear();
                                waiting.setVisibility(View.GONE);
                                step1.setVisibility(View.VISIBLE);
                                latestActiveView = step1;


                            }

                            if(rideDTO.status == Status.PANIC){

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setMessage("Your ride is interupted because panic was pressed.");
                                builder1.setCancelable(true);
                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                                map.getOverlays().clear();
                                latestActiveView.setVisibility(View.GONE);
                                step1.setVisibility(View.VISIBLE);
                                latestActiveView = step1;
                            }
                        }
                        catch (Exception e){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                            builder1.setMessage("No drivers left for your ride!");
                            builder1.setCancelable(true);
                            AlertDialog alert11 = builder1.create();
                            dialog.dismiss();
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

    private void setAcceptedRide(RideDTO rideDTO){
        ServiceUtils.driverService.getDriverVehicle(String.valueOf(rideDTO.getDriver().getId())).enqueue(new Callback<VehicleInfoDTO>() {
            @Override
            public void onResponse(Call<VehicleInfoDTO> call, Response<VehicleInfoDTO> response) {
                map.getOverlays().clear();
                MapService.getRoute(response.body().currentLocation, rideDTO.getLocations().get(0).getDeparture(),R.drawable.destination_marker,R.drawable.destination_marker,map,getContext());
                carMarker = MapService.DrawMarker(response.body().currentLocation,R.drawable.car_icon,map, getContext());
                MapService.ZoomTo(response.body().currentLocation,16.0,map);
            }

            @Override
            public void onFailure(Call<VehicleInfoDTO> call, Throwable t) {

            }
        });
    }


    private void checkForAcceptedRide(){
        Call<RideDTO> acceptedRideCall = ServiceUtils.rideService.getPassengerAcceptedRide(userPrefs.getString("id", "0"));
        acceptedRideCall.enqueue(new Callback<RideDTO>() {
            @Override
            public void onResponse(Call<RideDTO> call1, Response<RideDTO> response1) {
                if(response1.body() != null){

                    ServiceUtils.driverService.getDriverVehicle(String.valueOf(response1.body().getDriver().getId())).enqueue(new Callback<VehicleInfoDTO>() {
                        @Override
                        public void onResponse(Call<VehicleInfoDTO> call, Response<VehicleInfoDTO> response) {
                            map.getOverlays().clear();
                            latestActiveView.setVisibility(View.GONE);
                            waiting.setVisibility(View.VISIBLE);
                            latestActiveView = waiting;
                            MapService.getRoute(response.body().currentLocation, response1.body().getLocations().get(0).getDeparture(),R.drawable.destination_marker,R.drawable.destination_marker,map,getContext());
                            carMarker = MapService.DrawMarker(response.body().currentLocation,R.drawable.car_icon,map, getContext());
                            MapService.ZoomTo(response.body().currentLocation,16.0,map);
                        }

                        @Override
                        public void onFailure(Call<VehicleInfoDTO> call, Throwable t) {

                        }
                    });
                }
                else{
                    checkForPendingRides();
                }
            }

            @Override
            public void onFailure(Call<RideDTO> call1, Throwable t) {

            }
        });
    }

    private void checkForPendingRides(){
        Call<RideDTO> pendingRideCall = ServiceUtils.rideService.getPassengerPendingRide(userPrefs.getString("id", "0"));
        pendingRideCall.enqueue(new Callback<RideDTO>() {
            @Override
            public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                if(response.body() != null){
                    latestActiveView = step4;
                    dialog.dismiss();
                    populateDialogAndShow(dialogView, dialog, response.body());
                }

            }

            @Override
            public void onFailure(Call<RideDTO> call, Throwable t) {

            }
        });
    }

    private void createDriverVehicleLocationNotifSession(){
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://"+LOCALHOST+"/simulation");
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
        webSocketClient.enableAutomaticReconnection(1000);
        webSocketClient.addHeader("Authorization", "Bearer " + TokenHolder.getInstance().getJwtToken());
        webSocketClient.addHeader("id",userPrefs.getString("id","0"));
        webSocketClient.addHeader("role",userPrefs.getString("role","0"));
        webSocketClient.connect();
    }

    private void showReviewPopUp(RideDTO rideDTO) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.review_dialog, null);
        builder1.setView(dialogLayout);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Rate", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CreateReviewDTO createVehicleReview = new CreateReviewDTO();
                TextView textView = dialogLayout.findViewById(R.id.vehicleComment);
                RatingBar ratingBar = dialogLayout.findViewById(R.id.vehicleRatingBar);
                createVehicleReview.setComment(textView.getText().toString());
                createVehicleReview.setRating(ratingBar.getRating());
                Log.d("vehicle comment", createVehicleReview.getComment());
                Call<CreateReviewResponseDTO> call = ServiceUtils.passengerService.createVehicleReview(Integer.toString(rideDTO.getId()), createVehicleReview);
                call.enqueue(new Callback<CreateReviewResponseDTO>() {

                    @Override
                    public void onResponse(Call<CreateReviewResponseDTO> call, Response<CreateReviewResponseDTO> response) {
                        CreateReviewDTO createDriverReview = new CreateReviewDTO();
                        TextView driverComment = dialogLayout.findViewById(R.id.driverComment);
                        RatingBar ratingBar = dialogLayout.findViewById(R.id.driverRatingBar);
                        createDriverReview.setComment(driverComment.getText().toString());
                        createDriverReview.setRating(ratingBar.getRating());
                        Log.d("driver comment", createDriverReview.getComment());
                        Call<CreateReviewResponseDTO> call2 = ServiceUtils.passengerService.createDriverReview(Integer.toString(rideDTO.getId()), createDriverReview);
                        call2.enqueue(new Callback<CreateReviewResponseDTO>() {
                            @Override
                            public void onResponse(Call<CreateReviewResponseDTO> call, Response<CreateReviewResponseDTO> response) {
                                map.getOverlays().clear();
                                currentRideView.setVisibility(View.GONE);
                                step1.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFailure(Call<CreateReviewResponseDTO> call, Throwable t) {

                            }
                        });

                    }
                    @Override
                    public void onFailure(Call<CreateReviewResponseDTO> call, Throwable t) {

                    }
                });

            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        setDate.set(Calendar.YEAR,year);
        setDate.set(Calendar.MONTH,monthOfYear);
        setDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        setDate.set(year,monthOfYear,dayOfMonth);
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                PassengerMainFragment.this,
                Calendar.HOUR_OF_DAY-1,
                Calendar.MINUTE,
                true
        );
        timePickerDialog.setAccentColor("#FF9642");
        timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {
        setDate.set(Calendar.HOUR_OF_DAY,hourOfDay+1);
        setDate.set(Calendar.MINUTE,minute);
    }
}
