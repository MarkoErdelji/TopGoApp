package com.example.uberapp_tim6.driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.uberapp_tim6.DTOS.CreateReviewResponseDTO;
import com.example.uberapp_tim6.DTOS.DriverReviewListDTO;
import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserRef;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.adapters.DriverRideHistoryAdapter;
import com.example.uberapp_tim6.adapters.ReviewAdapter;
import com.example.uberapp_tim6.models.Ride;
import com.example.uberapp_tim6.services.ServiceUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CertainRideFromHistory extends AppCompatActivity {
    Context context = this;
    CertainRideFromHistory certainRideFromHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certain_ride_from_history);
        certainRideFromHistory = this;

        int rideId = (int) getIntent().getSerializableExtra("rideId");
        Call<RideDTO> call = ServiceUtils.rideService.getRide(Integer.toString(rideId));
        call.enqueue(new Callback<RideDTO>() {
            @Override
            public void onResponse(Call<RideDTO> call, Response<RideDTO> response) {
                if (response.body() != null) {
                    RideDTO ride = response.body();
                    TextView date = (TextView) findViewById(R.id.tripDateTextView);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    String formattedDate = ride.getStartTime().format(formatter);
                    String dateText = "" + formattedDate;
                    date.setText(dateText);

                    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                    TextView price = (TextView) findViewById(R.id.tripPriceTextView);
                    String  priceText = ride.getTotalCost() + "RSD";
                    price.setText(priceText);

                    TextView from = (TextView) findViewById(R.id.departure_text_view);
                    String fromText = ride.getLocations().get(0).getDeparture().getAddress();
                    from.setText(fromText);

                    TextView to = (TextView) findViewById(R.id.destination_text_view);
                    String toText = ride.getLocations().get(0).getDestination().getAddress();
                    to.setText(toText);

                    showPassengers(ride);

                    ImageView imageView = (ImageView) findViewById(R.id.closeIcon);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                    Call<List<CreateReviewResponseDTO>> call2 = ServiceUtils.rideService.getAllRideReviews(Integer.toString(ride.getId()));
                    call2.enqueue(new Callback<List<CreateReviewResponseDTO>>() {
                        @Override
                        public void onResponse(Call<List<CreateReviewResponseDTO>> call2, Response<List<CreateReviewResponseDTO>> response) {
                            if (response.body() != null) {
                                @SuppressLint({"MissingInflatedId", "LocalSuppress"})
                                ListView list = (ListView) findViewById(R.id.reviewListView);
                                ReviewAdapter reviewAdapter = new ReviewAdapter(certainRideFromHistory, response.body());
                                list.setAdapter(reviewAdapter);

                            }

                        }

                        @Override
                        public void onFailure(Call<List<CreateReviewResponseDTO>> call2, Throwable t) {
                        }

                    });

                }

            }

            @Override
            public void onFailure(Call<RideDTO> call, Throwable t) {

            }

        });



    }
    public void showPassengers(RideDTO body) {
        RelativeLayout passengerIcons = findViewById(R.id.passengerIcons);
        List<UserRef> passengers = body.getPassengers();
        final int[] previousId = {0};
        for (int i = 0; i < passengers.size(); i++) {
            UserRef passenger = passengers.get(i);
            Call<UserInfoDTO> call = ServiceUtils.userService.getUserById(passenger.getId().toString());
            call.enqueue(new Callback<UserInfoDTO>() {
                @Override
                public void onResponse(Call<UserInfoDTO> call, Response<UserInfoDTO> response) {
                    UserInfoDTO user = response.body();
                    RelativeLayout passengerLayout = new RelativeLayout(context );
                    passengerLayout.setId(View.generateViewId());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    if (previousId[0] != 0) {
                        layoutParams.addRule(RelativeLayout.RIGHT_OF, previousId[0]);
                    }
                    layoutParams.setMargins(10, 10, 10, 10);
                    passengerLayout.setLayoutParams(layoutParams);
                    // Create new passenger icon
                    CircleImageView passengerIcon = new CircleImageView(context);
                    passengerIcon.setId(View.generateViewId());
                    passengerIcon.setLayoutParams(new RelativeLayout.LayoutParams(100, 100));
                    passengerIcon.setImageResource(R.drawable.tate);
                    Glide.with(getApplicationContext()).load(response.body().getProfilePicture()).into(passengerIcon);
                    layoutParams = (RelativeLayout.LayoutParams) passengerIcon.getLayoutParams();
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                    passengerIcon.setLayoutParams(layoutParams);
                    passengerLayout.addView(passengerIcon);
                    // Create new passenger name TextView
                    TextView passengerName = new TextView(context);
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