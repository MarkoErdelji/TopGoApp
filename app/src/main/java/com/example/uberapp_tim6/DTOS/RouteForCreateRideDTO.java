package com.example.uberapp_tim6.DTOS;

import java.io.Serializable;

public class RouteForCreateRideDTO implements Serializable {


    private GeoLocationDTO departure;

    private GeoLocationDTO destination;

    public RouteForCreateRideDTO() {
    }

    public GeoLocationDTO getDeparture() {
        return departure;
    }

    public void setDeparture(GeoLocationDTO departure) {
        this.departure = departure;
    }

    public GeoLocationDTO getDestination() {
        return destination;
    }

    public void setDestination(GeoLocationDTO destination) {
        this.destination = destination;
    }
}
