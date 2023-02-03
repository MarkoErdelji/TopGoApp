package com.example.uberapp_tim6.driver.fragments;


import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserRidesListDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.models.enumerations.Status;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.example.uberapp_tim6.tools.ChartLocalDateFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverReportsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverReportsFragment extends Fragment {

    private UserInfoDTO driver;
    private View reportsView;
    Map<String,Float> mapPrice = new HashMap<>();
    int totalCount = 0;

    private EditText beginDateInput;
    private EditText endDateInput;
    private Button submitButton;

    private TextView totalMoneyView;
    private TextView totalKmView;
    private TextView totalRidesView;

    List<Entry> entries = new ArrayList<>();
    List<Entry> entries2 = new ArrayList<>();
    List<Entry> entries3 = new ArrayList<>();


    List<Entry> averageEntries = new ArrayList<>();
    List<Entry> averageEntries2 = new ArrayList<>();
    List<Entry> averageEntries3 = new ArrayList<>();

    private static final String ARG_DRIVER = "arg_driver";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    public DriverReportsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DriverReportsFragment newInstance(UserInfoDTO d) {
        DriverReportsFragment fragment = new DriverReportsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DRIVER, d);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        driver = (UserInfoDTO) getArguments().getSerializable(ARG_DRIVER);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reportsView = inflater.inflate(R.layout.fragment_driver_reports, null);


        beginDateInput = reportsView.findViewById(R.id.editText_start_date);
        endDateInput = reportsView.findViewById(R.id.editText_end_date);
        submitButton = reportsView.findViewById(R.id.button_submit);

        totalMoneyView = reportsView.findViewById(R.id.totalMoneyValue);
        totalKmView = reportsView.findViewById(R.id.totalKmValue);
        totalRidesView = reportsView.findViewById(R.id.totalRidesValue);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String beginDateString = beginDateInput.getText().toString();
                String endDateString = endDateInput.getText().toString();

                if (validateInput(beginDateString, endDateString)) {
                    submitForm(beginDateString, endDateString);
                }
            }
        });

        Map<String,Float> chart1Map = new HashMap<>();
        Map<String,Float> chart2Map = new HashMap<>();
        Map<String,Float> chart3Map = new HashMap<>();

        LineChart earned_money_chart = reportsView.findViewById(R.id.chart1);
        LineChart total_rides_chart = reportsView.findViewById(R.id.chart2);
        LineChart total_km_chart = reportsView.findViewById(R.id.chart3);



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        ServiceUtils.driverService.getDriverRides(driver.getId(),0,10000).enqueue(new Callback<UserRidesListDTO>() {
            @Override
            public void onResponse(Call<UserRidesListDTO> call, Response<UserRidesListDTO> response) {
                if(response.isSuccessful()) {
                    List<RideDTO> rides = response.body().getResults();
                    Collections.reverse(rides);

                    int totalCountMoney = 0;
                    int totalCountRides = 0;
                    int totalCountKM = 0;
                    AtomicReference<Float> totalValuePrice = new AtomicReference<>(Float.valueOf(0));
                    AtomicReference<Float> totalKm = new AtomicReference<>(Float.valueOf(0));
                    AtomicReference<Integer> totalRides = new AtomicReference<>(Integer.valueOf(0));
                    Log.d("RIDES",rides.toString());
                    rides.forEach(ride->{
                        if(ride.status == Status.PANIC ||  ride.status == Status.REJECTED){
                            return;
                        }
                        String startDate = ride.startTime.format(formatter);
                        if(chart1Map.containsKey(startDate)){
                            float val = chart1Map.get(startDate)+ride.getTotalCost();
                            chart1Map.put(startDate,val);
                        }
                        else{
                            chart1Map.put(startDate,ride.getTotalCost());
                        }
                        if(chart2Map.containsKey(startDate)){
                            float val = chart2Map.get(startDate)+1;
                            chart2Map.put(startDate,val);

                        }
                        else{
                            chart2Map.put(startDate, 1F);
                        }
                        if(chart3Map.containsKey(startDate)){
                            float val = chart3Map.get(startDate)+ride.getLocations().get(0).getLenght();
                            chart3Map.put(startDate,val);
                        }
                        else{
                            chart3Map.put(startDate, ride.getLocations().get(0).getLenght());
                        }
                        totalValuePrice.updateAndGet(v -> v + ride.getTotalCost());
                        totalRides.updateAndGet(v -> v+1);
                        totalKm.updateAndGet(v -> v+ride.getLocations().get(0).getLenght());
                    });
                    chart1Map.forEach((key,value)->{
                        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        entries.add(new Entry(LocalDate.parse(key,formatter).toEpochDay(),value.intValue()));
                    });

                    chart2Map.forEach((key,value)->{
                        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        entries2.add(new Entry(LocalDate.parse(key,formatter).toEpochDay(),value.intValue()));
                    });

                    chart3Map.forEach((key,value)->{
                        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        entries3.add(new Entry(LocalDate.parse(key,formatter).toEpochDay(),value.intValue()));
                    });
                    totalCountMoney = chart1Map.size();
                    totalCountRides = chart2Map.size();
                    totalCountKM = chart3Map.size();
                    Collections.sort(entries, new Comparator<Entry>() {
                        @Override
                        public int compare(Entry o1, Entry o2) {
                            return (int) (o1.getX() - o2.getX());
                        }
                    });
                    Collections.sort(entries2, new Comparator<Entry>() {
                        @Override
                        public int compare(Entry o1, Entry o2) {
                            return (int) (o1.getX() - o2.getX());
                        }
                    });
                    Collections.sort(entries3, new Comparator<Entry>() {
                        @Override
                        public int compare(Entry o1, Entry o2) {
                            return (int) (o1.getX() - o2.getX());
                        }
                    });
                    int finalTotalCountMoney = totalCountMoney;
                    entries.forEach(entry->{
                        averageEntries.add(new Entry(entry.getX(),(float)totalValuePrice.get().intValue()/ finalTotalCountMoney));
                    });
                    int finalTotalCountRides = totalCountRides;
                    entries2.forEach(entry->{
                        averageEntries2.add(new Entry(entry.getX(),(float)(totalRides.get().floatValue() / finalTotalCountRides)));
                    });
                    int finalTotalCountKm = totalCountKM;
                    entries3.forEach(entry->{
                        averageEntries3.add(new Entry(entry.getX(),(float)(totalKm.get().floatValue() / finalTotalCountKm)));
                    });


                    XAxis x_axis_earned_money = earned_money_chart.getXAxis();
                    x_axis_earned_money.setPosition(XAxis.XAxisPosition.BOTTOM);
                    x_axis_earned_money.setTextSize(10f);
                    x_axis_earned_money.setDrawAxisLine(true);
                    x_axis_earned_money.setDrawGridLines(false);
                    x_axis_earned_money.setLabelRotationAngle(90f);
                    x_axis_earned_money.setValueFormatter(new ChartLocalDateFormatter(formatter));

                    totalCountMoney = response.body().getTotalCount();
                    LineDataSet earned_per_day = new LineDataSet(entries, "Earned per day");
                    LineDataSet average_earned_per_day = new LineDataSet(averageEntries, "Average earned");

                    earned_per_day.setColor(Color.parseColor("#FF9642"));
                    earned_per_day.setValueTextSize(12f);
                    earned_per_day.setCircleColor(Color.parseColor("#FF9642"));
                    earned_per_day.setValueTextColor(Color.parseColor("#FF9642"));

                    LineData average_earned_data = new LineData(earned_per_day);
                    average_earned_data.addDataSet(average_earned_per_day);
                    YAxis y_axis_earned_money = earned_money_chart.getAxisLeft();
                    y_axis_earned_money.setTextSize(10f);
                    y_axis_earned_money.setLabelCount(10);

                    average_earned_per_day.setValueTextSize(12f);
                    average_earned_per_day.setValueTextColor(average_earned_per_day.getColor());

                    earned_money_chart.setMaxVisibleValueCount(10);
                    y_axis_earned_money.setAxisMaximum(earned_per_day.getYMax() + (0.1f * earned_per_day.getYMax()));

                    earned_money_chart.setScaleEnabled(true);
                    Description d = new Description();
                    d.setText("Earned money per day");
                    earned_money_chart.setDescription(d);
                    earned_money_chart.setData(average_earned_data);
                    earned_money_chart.invalidate();

                    //Total rides chart

                    XAxis x_axis_total_rides = total_rides_chart.getXAxis();
                    x_axis_total_rides.setPosition(XAxis.XAxisPosition.BOTTOM);
                    x_axis_total_rides.setTextSize(10f);
                    x_axis_total_rides.setDrawAxisLine(true);
                    x_axis_total_rides.setDrawGridLines(false);
                    x_axis_total_rides.setLabelRotationAngle(90f);
                    x_axis_total_rides.setValueFormatter(new ChartLocalDateFormatter(formatter));

                    Log.d("ENTRIES2:",entries2.toString());
                    Log.d("AVERAGEENTRIES:",averageEntries2.toString());
                    totalCountMoney = response.body().getTotalCount();
                    LineDataSet total_rides = new LineDataSet(entries2, "Rides per day");
                    LineDataSet average_total_rides = new LineDataSet(averageEntries2, "Average rides per day");

                    total_rides.setColor(Color.parseColor("#FF9642"));
                    total_rides.setValueTextSize(12f);
                    total_rides.setCircleColor(Color.parseColor("#FF9642"));
                    total_rides.setValueTextColor(Color.parseColor("#FF9642"));

                    average_total_rides.setValueTextSize(12f);
                    average_total_rides.setValueTextColor(average_total_rides.getColor());

                    LineData average_total_rides_data = new LineData(total_rides);
                    average_total_rides_data.addDataSet(average_total_rides);
                    YAxis y_axis_total_rides = total_rides_chart.getAxisLeft();
                    y_axis_total_rides.setTextSize(10f);
                    y_axis_total_rides.setLabelCount(10);


                    total_rides_chart.setMaxVisibleValueCount(10);
                    y_axis_total_rides.setAxisMaximum(total_rides.getYMax() + (0.1f * total_rides.getYMax()));

                    total_rides_chart.setScaleEnabled(true);
                    Description rides_desc = new Description();
                    rides_desc.setText("Total rides per day");
                    total_rides_chart.setDescription(rides_desc);

                    total_rides_chart.setData(average_total_rides_data);
                    total_rides_chart.invalidate();



                    //Total km chart

                    XAxis x_axis_km = total_km_chart.getXAxis();
                    x_axis_km.setPosition(XAxis.XAxisPosition.BOTTOM);
                    x_axis_km.setTextSize(10f);
                    x_axis_km.setDrawAxisLine(true);
                    x_axis_km.setDrawGridLines(false);
                    x_axis_km.setLabelRotationAngle(90f);
                    x_axis_km.setValueFormatter(new ChartLocalDateFormatter(formatter));

                    LineDataSet total_km = new LineDataSet(entries3, "Km per day");
                    LineDataSet average_km = new LineDataSet(averageEntries3, "Average km per day");

                    total_km.setColor(Color.parseColor("#FF9642"));
                    total_km.setValueTextSize(12f);
                    total_km.setCircleColor(Color.parseColor("#FF9642"));
                    total_km.setValueTextColor(Color.parseColor("#FF9642"));

                    average_km.setValueTextSize(12f);
                    average_km.setValueTextColor(average_km.getColor());

                    LineData average_km_data = new LineData(total_km);
                    average_km_data.addDataSet(average_km);
                    YAxis y_axis_km = total_km_chart.getAxisLeft();
                    y_axis_km.setTextSize(10f);
                    y_axis_km.setLabelCount(10);


                    total_km_chart.setMaxVisibleValueCount(10);
                    y_axis_total_rides.setAxisMaximum(total_rides.getYMax() + (0.1f * total_rides.getYMax()));

                    total_km_chart.setScaleEnabled(true);
                    Description km_desc = new Description();
                    km_desc.setText("Total km per day");
                    total_km_chart.setDescription(km_desc);

                    total_km_chart.setData(average_km_data);
                    total_km_chart.invalidate();

                    totalMoneyView.setText(totalValuePrice.toString());
                    totalKmView.setText(totalKm.toString());
                    totalRidesView.setText(totalRides.toString());
                }
                else{
                    return;
                }
            }

            @Override
            public void onFailure(Call<UserRidesListDTO> call, Throwable t) {

            }
        });


        return reportsView;
    }



    private boolean validateInput(String beginDateString, String endDateString) {
        if (TextUtils.isEmpty(beginDateString)) {
            beginDateInput.setError("Begin date is required");
            return false;
        }
        if (TextUtils.isEmpty(endDateString)) {
            endDateInput.setError("End date is required");
            return false;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            LocalDateTime beginDate = LocalDateTime.parse(beginDateString, formatter);
            LocalDateTime endDate = LocalDateTime.parse(endDateString, formatter);

            Log.d("BEGIN",beginDate.toString());
            Log.d("END",endDate.toString());
            if (beginDate.isAfter(endDate)) {
                beginDateInput.setError("Begin date must be before end date");
                endDateInput.setError("End date must be after begin date");
                return false;
            }
        } catch (DateTimeParseException e) {
            beginDateInput.setError("Invalid date format. Use ISO format: yyyy-MM-dd'T'HH:mm:ss");
            endDateInput.setError("Invalid date format. Use ISO format: yyyy-MM-dd'T'HH:mm:ss");
            return false;
        }
        return true;
    }

    private void submitForm(String beginDateString, String endDateString) {
        Map<String,Float> chart1Map = new HashMap<>();
        Map<String,Float> chart2Map = new HashMap<>();
        Map<String,Float> chart3Map = new HashMap<>();

        LineChart earned_money_chart = reportsView.findViewById(R.id.chart1);
        LineChart total_rides_chart = reportsView.findViewById(R.id.chart2);
        LineChart total_km_chart = reportsView.findViewById(R.id.chart3);



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        ServiceUtils.driverService.getDriverRidesWithInterval(driver.getId(),0,10000,beginDateString,endDateString).enqueue(new Callback<UserRidesListDTO>() {
            @Override
            public void onResponse(Call<UserRidesListDTO> call, Response<UserRidesListDTO> response) {
                if(response.isSuccessful()) {
                    entries.clear();
                    entries2.clear();
                    entries3.clear();
                    averageEntries.clear();
                    averageEntries2.clear();
                    averageEntries3.clear();
                    List<RideDTO> rides = response.body().getResults();
                    Collections.reverse(rides);
                    int totalCountMoney = 0;
                    int totalCountRides = 0;
                    int totalCountKM = 0;
                    AtomicReference<Float> totalValuePrice = new AtomicReference<>(Float.valueOf(0));
                    AtomicReference<Float> totalKm = new AtomicReference<>(Float.valueOf(0));
                    AtomicReference<Integer> totalRides = new AtomicReference<>(Integer.valueOf(0));
                    Log.d("RIDES",rides.toString());
                    rides.forEach(ride->{
                        if(ride.status == Status.PANIC ||  ride.status == Status.REJECTED){
                            return;
                        }
                        String startDate = ride.startTime.format(formatter);
                        if(chart1Map.containsKey(startDate)){
                            float val = chart1Map.get(startDate)+ride.getTotalCost();
                            chart1Map.put(startDate,val);
                        }
                        else{
                            chart1Map.put(startDate,ride.getTotalCost());
                        }
                        if(chart2Map.containsKey(startDate)){
                            float val = chart2Map.get(startDate)+1;
                            chart2Map.put(startDate,val);

                        }
                        else{
                            chart2Map.put(startDate, 1F);
                        }
                        if(chart3Map.containsKey(startDate)){
                            float val = chart3Map.get(startDate)+ride.getLocations().get(0).getLenght();
                            chart3Map.put(startDate,val);
                        }
                        else{
                            chart3Map.put(startDate, ride.getLocations().get(0).getLenght());
                        }
                        totalValuePrice.updateAndGet(v -> v + ride.getTotalCost());
                        totalRides.updateAndGet(v -> v+1);
                        totalKm.updateAndGet(v -> v+ride.getLocations().get(0).getLenght());
                    });
                    chart1Map.forEach((key,value)->{
                        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        entries.add(new Entry(LocalDate.parse(key,formatter).toEpochDay(),value.intValue()));
                    });

                    chart2Map.forEach((key,value)->{
                        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        entries2.add(new Entry(LocalDate.parse(key,formatter).toEpochDay(),value.intValue()));
                    });

                    chart3Map.forEach((key,value)->{
                        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        entries3.add(new Entry(LocalDate.parse(key,formatter).toEpochDay(),value.intValue()));
                    });
                    totalCountMoney = chart1Map.size();
                    totalCountRides = chart2Map.size();
                    totalCountKM = chart3Map.size();
                    Collections.sort(entries, new Comparator<Entry>() {
                        @Override
                        public int compare(Entry o1, Entry o2) {
                            return (int) (o1.getX() - o2.getX());
                        }
                    });
                    Collections.sort(entries2, new Comparator<Entry>() {
                        @Override
                        public int compare(Entry o1, Entry o2) {
                            return (int) (o1.getX() - o2.getX());
                        }
                    });
                    Collections.sort(entries3, new Comparator<Entry>() {
                        @Override
                        public int compare(Entry o1, Entry o2) {
                            return (int) (o1.getX() - o2.getX());
                        }
                    });
                    int finalTotalCountMoney = totalCountMoney;
                    entries.forEach(entry->{
                        averageEntries.add(new Entry(entry.getX(),(float)totalValuePrice.get().intValue()/ finalTotalCountMoney));
                    });
                    int finalTotalCountRides = totalCountRides;
                    entries2.forEach(entry->{
                        averageEntries2.add(new Entry(entry.getX(),(float)(totalRides.get().floatValue() / finalTotalCountRides)));
                    });
                    int finalTotalCountKm = totalCountKM;
                    entries3.forEach(entry->{
                        averageEntries3.add(new Entry(entry.getX(),(float)(totalKm.get().floatValue() / finalTotalCountKm)));
                    });
                    LineDataSet earned_per_day = new LineDataSet(entries, "Earned per day");
                    LineDataSet average_earned_per_day = new LineDataSet(averageEntries, "Average earned");

                    earned_per_day.setColor(Color.parseColor("#FF9642"));
                    earned_per_day.setValueTextSize(12f);
                    earned_per_day.setCircleColor(Color.parseColor("#FF9642"));
                    earned_per_day.setValueTextColor(Color.parseColor("#FF9642"));

                    LineData average_earned_data = new LineData(earned_per_day);
                    average_earned_data.addDataSet(average_earned_per_day);

                    LineDataSet total_rides = new LineDataSet(entries2, "Rides per day");
                    LineDataSet average_total_rides = new LineDataSet(averageEntries2, "Average rides per day");

                    total_rides.setColor(Color.parseColor("#FF9642"));
                    total_rides.setValueTextSize(12f);
                    total_rides.setCircleColor(Color.parseColor("#FF9642"));
                    total_rides.setValueTextColor(Color.parseColor("#FF9642"));

                    average_total_rides.setValueTextSize(12f);
                    average_total_rides.setValueTextColor(average_total_rides.getColor());

                    LineData average_total_rides_data = new LineData(total_rides);
                    average_total_rides_data.addDataSet(average_total_rides);

                    LineDataSet total_km = new LineDataSet(entries3, "Km per day");
                    LineDataSet average_km = new LineDataSet(averageEntries3, "Average km per day");

                    total_km.setColor(Color.parseColor("#FF9642"));
                    total_km.setValueTextSize(12f);
                    total_km.setCircleColor(Color.parseColor("#FF9642"));
                    total_km.setValueTextColor(Color.parseColor("#FF9642"));

                    average_km.setValueTextSize(12f);
                    average_km.setValueTextColor(average_km.getColor());

                    LineData average_km_data = new LineData(total_km);
                    average_km_data.addDataSet(average_km);
                    earned_money_chart.setData(average_earned_data);
                    total_km_chart.setData(average_km_data);
                    total_rides_chart.setData(average_total_rides_data);

                    earned_money_chart.invalidate();
                    total_km_chart.invalidate();
                    total_rides_chart.invalidate();

                    totalMoneyView.setText(totalValuePrice.toString());
                    totalKmView.setText(totalKm.toString());
                    totalRidesView.setText(totalRides.toString());

                }
                else{
                    return;
                }
            }

            @Override
            public void onFailure(Call<UserRidesListDTO> call, Throwable t) {

            }
        });    }

}