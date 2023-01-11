package com.example.uberapp_tim6.tools;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.example.uberapp_tim6.DTOS.GeoLocationDTO;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polyline;

import java.util.List;

public class RouteOverlay extends Overlay {

        private List<GeoPoint> routePoints;
        private Polyline routePolyline;

        public RouteOverlay(List<GeoPoint> routePoints) {
            this.routePoints = routePoints;
            this.routePolyline = new Polyline();
            this.routePolyline.setPoints(routePoints);
            this.routePolyline.setColor(Color.RED);
        }

        @Override
        public void draw(Canvas canvas, MapView mapView, boolean shadow) {
            if (!shadow) {
                this.routePolyline.draw(canvas, mapView,shadow);
            }
        }
    }
