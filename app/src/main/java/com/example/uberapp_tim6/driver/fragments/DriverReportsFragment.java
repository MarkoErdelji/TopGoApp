package com.example.uberapp_tim6.driver.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserRidesListDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.services.ServiceUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reportsView = inflater.inflate(R.layout.fragment_driver_reports, null);
        LineChart chart1 = reportsView.findViewById(R.id.chart1);
        List<Entry> entries = new ArrayList<>();

        ServiceUtils.driverService.getDriverRides(driver.getId(),0,10000).enqueue(new Callback<UserRidesListDTO>() {
            @Override
            public void onResponse(Call<UserRidesListDTO> call, Response<UserRidesListDTO> response) {
                if(response.isSuccessful()) {
                    List<RideDTO> rides = response.body().getResults();
                    Collections.reverse(rides);
                    AtomicInteger i = new AtomicInteger();
                    rides.forEach(ride->{
                        i.getAndIncrement();
                        entries.add(new BarEntry(i.intValue(), ride.getTotalCost(), ride.getStartTime().toString()));
                    });

                    totalCount = response.body().getTotalCount();
                    LineDataSet dataSet1 = new LineDataSet(entries, "Chart 1");
                    LineData lineData1 = new LineData(dataSet1);
                    chart1.setData(lineData1);
                    chart1.invalidate();
                }
                else{
                    return;
                }
            }

            @Override
            public void onFailure(Call<UserRidesListDTO> call, Throwable t) {

            }
        });
        LineChart chart2 = reportsView.findViewById(R.id.chart2);
        LineChart chart3 = reportsView.findViewById(R.id.chart3);


        List<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(0, 3));
        entries2.add(new Entry(1, 4));
        entries2.add(new Entry(2, 5));
        LineDataSet dataSet2 = new LineDataSet(entries2, "Chart 2");
        LineData lineData2 = new LineData(dataSet2);
        chart2.setData(lineData2);
        chart2.invalidate();

        List<Entry> entries3 = new ArrayList<>();
        entries3.add(new Entry(0, 6));
        entries3.add(new Entry(1, 7));
        entries3.add(new Entry(2, 8));
        LineDataSet dataSet3 = new LineDataSet(entries3, "Chart 3");
        LineData lineData3 = new LineData(dataSet3);
        chart3.setData(lineData3);
        chart3.invalidate();

        return reportsView;
    }
}