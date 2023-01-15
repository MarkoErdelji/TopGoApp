package com.example.uberapp_tim6.services;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.uberapp_tim6.DTOS.GeoLocationDTO;
import com.example.uberapp_tim6.DTOS.MatrixAPIDTO;
import com.example.uberapp_tim6.DTOS.TimeAndDistanceDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.tools.RouteOverlay;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MapService{

    public interface GeoLocationCallback {
        void onResponse(GeoLocationDTO geoLocationDTO);
    }

    public interface DistanceDurationCallback {
        void onResponse(TimeAndDistanceDTO timeAndDistanceDTO);
    }


    public static GeoLocationDTO getLocation(String locationName,GeoLocationCallback callback){
        String apiKey = "5b3ce3597851110001cf624865f18297bb26459a9f779c015d573b96";
        String baseUrl = "https://api.openrouteservice.org/geocode/search";

        GeoLocationDTO geoLocationDTO = new GeoLocationDTO();

        geoLocationDTO.setAddress(locationName);
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.openrouteservice.org")
                .addPathSegment("geocode")
                .addPathSegment("search")
                .addQueryParameter("api_key", apiKey)
                .addQueryParameter("text", locationName)
                .build();

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
                        geoLocationDTO.setLongitude(Float.parseFloat(coordinates.get(0).toString()));
                        geoLocationDTO.setLatitude((Float.parseFloat(coordinates.get(1).toString())));
                        Log.d("REEEEZ",geoLocationDTO.toString());
                        callback.onResponse(geoLocationDTO);
                    }catch (JSONException e) {
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
        return geoLocationDTO;
    }
    public static void getRoute(GeoLocationDTO departure, GeoLocationDTO destination, int iconDeparture, int iconDestination, MapView mapView, Context ctxt) {
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
                        DrawRoute(routePoints,departure,destination,iconDeparture,iconDestination,mapView,ctxt);
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


    public static TimeAndDistanceDTO getDistance(GeoLocationDTO start,GeoLocationDTO end,DistanceDurationCallback distanceDurationCallback){
        String apiKey = "5b3ce3597851110001cf624865f18297bb26459a9f779c015d573b96";
        String baseUrl = "https://api.openrouteservice.org/v2/matrix/driving-car";

        MatrixAPIDTO matrixAPIDTO = new MatrixAPIDTO();
        List<List<Double>> locations = new ArrayList<>();
        List<Double> departure = new ArrayList<>();
        List<Double> destination = new ArrayList<>();

        List<String> metric = new ArrayList<>();
        metric.add("distance");
        metric.add("duration");
        departure.add(Float.valueOf(start.getLongitude()).doubleValue());
        departure.add(Float.valueOf(start.getLatitude()).doubleValue());
        destination.add(Float.valueOf(end.getLongitude()).doubleValue());
        destination.add(Float.valueOf(end.getLatitude()).doubleValue());
        TimeAndDistanceDTO timeAndDistanceDTO = new TimeAndDistanceDTO();
        locations.add(departure);
        locations.add(destination);

        matrixAPIDTO.setLocations(locations);
        matrixAPIDTO.setMetrics(metric);

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.openrouteservice.org")
                .addPathSegment("v2")
                .addPathSegment("matrix")
                .addPathSegment("driving-car")
                .build();
        Gson gson = new Gson();
        Log.d("GSOOON",gson.toJson(matrixAPIDTO));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),gson.toJson(matrixAPIDTO));
        Request request = new Request.Builder()
                .url(url).addHeader("Authorization",apiKey).post(requestBody)
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
                        JSONArray durations = json.getJSONArray("durations");
                        JSONArray distances = json.getJSONArray("distances");
                        JSONArray firstDistance = distances.getJSONArray(0);
                        JSONArray firstDuration = durations.getJSONArray(0);

                        double distance = firstDistance.getDouble(1);
                        double duration = firstDuration.getDouble(1);
                        timeAndDistanceDTO.setDistanceInKm(distance/1000.0);
                        timeAndDistanceDTO.setTimeInMinutes(duration/60.0);
                        distanceDurationCallback.onResponse(timeAndDistanceDTO);
                        Log.d("JSOONCINA", String.valueOf(json));
                    }catch (JSONException e) {
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
        return timeAndDistanceDTO;
    }
    public static void DrawMarker(GeoLocationDTO location,int icon,MapView mapView,Context ctxt)
    {
        Bitmap customIcon = BitmapFactory.decodeResource(ctxt.getResources(),icon);
        customIcon = Bitmap.createScaledBitmap(customIcon, 100, 100, false);
        BitmapDrawable customIconDrawable = new BitmapDrawable(ctxt.getResources(), customIcon);

        Marker marker = new Marker(mapView);

        marker.setPosition(new GeoPoint(location.getLatitude(), location.getLongitude()));
        marker.setIcon(customIconDrawable);
        marker.setAnchor(0.5f,1f);
        if(icon == R.drawable.car_icon)marker.setAnchor(0.5f,0.5f);
        mapView.getOverlays().add(marker);
        mapView.invalidate();

    }

    public static void DrawRoute(List<GeoPoint> routePoints, GeoLocationDTO departure, GeoLocationDTO destination,int iconDeparture,int iconDestination,MapView mapView,Context ctxt) {



        mapView.getOverlays().add(new RouteOverlay(routePoints));
        DrawMarker(departure,iconDeparture,mapView,ctxt);
        DrawMarker(destination,iconDestination,mapView,ctxt);
        mapView.invalidate();
    }
    public static void ZoomTo(GeoLocationDTO location,double zoom,MapView mapView)
    {
        GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        mapView.getController().setZoom(zoom);
        mapView.getController().animateTo(startPoint);
    }

}
