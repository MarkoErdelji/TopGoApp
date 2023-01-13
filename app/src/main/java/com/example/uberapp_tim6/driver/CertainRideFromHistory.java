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
import android.widget.TextView;

import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.adapters.ReviewAdapter;
import com.example.uberapp_tim6.models.Ride;

public class CertainRideFromHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certain_ride_from_history);

        RideDTO ride = (RideDTO) getIntent().getSerializableExtra("ride");

        TextView date = (TextView) findViewById(R.id.tripDateTextView);
        String dateText = "" + ride.getStartTime().toString();
        date.setText(dateText);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView price = (TextView) findViewById(R.id.tripPriceTextView);
        String  priceText = ride.getTotalCost() + "RSD";
        price.setText(priceText);

        TextView from = (TextView) findViewById(R.id.fromTextView);
        String fromText = ride.getLocations().get(0).getDeparture().getAddress();
        from.setText(fromText);

        TextView to = (TextView) findViewById(R.id.toTextView);
        String toText = ride.getLocations().get(0).getDestination().getAddress();
        to.setText(toText);

//        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
//        ListView list = (ListView) findViewById(R.id.reviewListView);
//        ReviewAdapter reviewAdapter = new ReviewAdapter(this, ride.getReviews());
//        list.setAdapter(reviewAdapter);
//
//        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
//        ImageView imageView = (ImageView) findViewById(R.id.closeIcon);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });


    }

}