package com.example.uberapp_tim6.passenger.fragments;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Base64;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.uberapp_tim6.DTOS.CreateReviewDTO;
import com.example.uberapp_tim6.DTOS.CreateReviewResponseDTO;
import com.example.uberapp_tim6.DTOS.CreateRideDTO;
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
import com.example.uberapp_tim6.models.Ride;
import com.example.uberapp_tim6.models.enumerations.Status;
import com.example.uberapp_tim6.models.enumerations.VehicleName;
import com.example.uberapp_tim6.services.MapService;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.example.uberapp_tim6.tools.DateTimeDeserializer;
import com.example.uberapp_tim6.tools.DateTimeSerializer;
import com.example.uberapp_tim6.tools.TokenHolder;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
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
public class PassengerMainFragment extends Fragment {

    private WebSocketClient webSocketClient;


    private EditText departureEditText;

    private EditText destinationEditText;

    private Button step1Order;

    private Spinner spinnerVehicleType;

    MapView map;

    private View step1;
    private View step2;
    private View step3;
    private View step4;

    private View latestActiveView;
    private View currentRideView;
    private CheckBox babyCheckbox;
    private CheckBox petCheckbox;
    private EditText numOfSeatsInput;

    private Button step2Next;
    private Button step3Next;
    private Button step4Order;

    private AppCompatButton step3Back;
    private AppCompatButton step4Back;
    AlertDialog dialog;
    private AppCompatButton backBtn;
    private View dialogView;

    private SharedPreferences userPrefs;

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
        userPrefs = getContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        createPassengerNotifSession();
        currentRideView = view.findViewById(R.id.passengerCurrentRide);
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
        latestActiveView = step1;
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
                latestActiveView = step2;
            }
        });

        step2Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step2.setVisibility(View.GONE);
                step3.setVisibility(View.VISIBLE);
                latestActiveView = step3;
            }
        });

        step3Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step3.setVisibility(View.GONE);
                step4.setVisibility(View.VISIBLE);
                latestActiveView = step4;
            }
        });

        step3Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step2.setVisibility(View.VISIBLE);
                step3.setVisibility(View.GONE);
                latestActiveView = step2;
            }
        });
        step4Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step3.setVisibility(View.VISIBLE);
                step4.setVisibility(View.GONE);
                latestActiveView = step3;
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
                            assert response.body() != null;
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
    }

    private void checkForActiveRide(int passengerId){
        ServiceUtils.rideService.getPassengerActiveRide(Integer.toString(passengerId)).enqueue(
                new Callback<RideDTO>() {
                    @Override
                    public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                        if (response.isSuccessful()) {
                            changeToCurrentRide();
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
    }
    private void setCurrentRideData(RideDTO rideDTO){
        TextView departureTextView = currentRideView.findViewById(R.id.departure_text_view);
        departureTextView.setText(rideDTO.getLocations().get(0).getDeparture().getAddress());

        TextView destinationTextView = currentRideView.findViewById(R.id.destination_text_view);
        destinationTextView.setText(rideDTO.getLocations().get(0).getDestination().getAddress());

        TextView driverNameAndLastName = currentRideView.findViewById(R.id.driverNameAndLastName);
        ServiceUtils.driverService.getDriverById(Integer.toString(rideDTO.getDriver().getId())).enqueue(
                new Callback<UserInfoDTO>() {
                    @Override
                    public void onResponse(Call<UserInfoDTO> call, Response<UserInfoDTO> response) {
                        if (response.isSuccessful()) {
                            String nameAndLastName = response.body().getName() + " " + response.body().getSurname();
                            driverNameAndLastName.setText(nameAndLastName);
                        }
                    }
                    @Override
                    public void onFailure(Call<UserInfoDTO> call, Throwable t) {

                    }
                });

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
                                    nameSurname.setText(String.format("%s %s", response.body().getName(), response.body().getSurname()));
                                    phoneNumber.setText(response.body().getTelephoneNumber());
                                    carNameView.setText(response2.body().getModel());
                                    carTypeView.setText(response2.body().getVehicleType());
                                    isForBabiesView.setText(String.valueOf(ride.isBabyTransport()));
                                    isForPetsView.setText(String.valueOf(ride.isPetTransport()));
                                    departureView.setText(ride.getLocations().get(0).getDeparture().getAddress());
                                    destinationView.setText(ride.getLocations().get(0).getDestination().getAddress());
                                    MapService.getDistance(ride.getLocations().get(0).getDeparture(), ride.getLocations().get(0).getDestination(), callback -> {
                                        TimeAndDistanceDTO timeAndDistanceDTO = callback;
                                        distanceView.setText(String.valueOf(timeAndDistanceDTO.getDistanceInKm() + " KM"));
                                        timeView.setText(String.valueOf(timeAndDistanceDTO.getTimeInMinutes() + " MIN"));
                                    });

                                    moneyView.setText(String.valueOf(ride.getTotalCost()) + " RSD");
                                    Glide.with(getContext()).load(response.body().getProfilePicture()).into(profilePic);

                                    GifDrawable gifDrawable = null;
                                    try {
                                        gifDrawable = new GifDrawable(getResources(), R.drawable.loading_gif);
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
            uri = new URI("ws://192.168.100.4:8000/websocket");
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
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                builder1.setMessage("Your ride was accepted!");
                                builder1.setCancelable(true);
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
                                builder1.setTitle("You arrived at your destination!");

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

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.addHeader("Authorization", "Bearer " + TokenHolder.getInstance().getJwtToken());
        webSocketClient.addHeader("id", userPrefs.getString("id", "0"));
        webSocketClient.addHeader("role", userPrefs.getString("role", "0"));
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


}
