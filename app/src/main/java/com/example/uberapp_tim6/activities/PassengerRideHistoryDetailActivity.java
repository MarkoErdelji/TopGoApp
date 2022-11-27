package com.example.uberapp_tim6.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.adapters.MessageListAdapter;
import com.example.uberapp_tim6.adapters.PassengerRideHistoryAdapter;
import com.example.uberapp_tim6.models.Message;
import com.example.uberapp_tim6.models.Ride;
import com.example.uberapp_tim6.models.User;
import com.example.uberapp_tim6.tools.Mokap;

import java.util.List;

public class PassengerRideHistoryDetailActivity  extends AppCompatActivity  {

    @SuppressLint("SetTextI18n")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger_ride_history_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Ride ride = (Ride)getIntent().getSerializableExtra("ride");
        TextView driverName = findViewById(R.id.driverName);
        TextView date = findViewById(R.id.date);
        TextView time = findViewById(R.id.time);
        TextView price = findViewById(R.id.price);
        TextView type = findViewById(R.id.type);

        driverName.setText("Driver: " +ride.getDriver().getFirstName() + " " + ride.getDriver().getLastName());
        date.setText(ride.getBeggining().toLocalDate().toString());
        time.setText("Time: " +ride.getBeggining().getHour() + ":" + ride.getBeggining().getMinute()+ "-"
        + ride.getEnd().getHour()+ ":" + ride.getEnd().getMinute());
        price.setText("Price: " +ride.getPrice() + "rsd");
        type.setText("Type: " + ride.getVehicleName());








    }

    public boolean onOptionsItemSelected(MenuItem item) {

        this.finish();
        return true;
    }

}

