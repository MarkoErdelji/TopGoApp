package com.example.uberapp_tim6.DTOS;

public class TimeAndDistanceDTO {

    private double timeInMinutes;
    private double distanceInKm;

    public TimeAndDistanceDTO() {
    }

    public double getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(double timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

    public double getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(double distanceInKm) {
        this.distanceInKm = distanceInKm;
    }
}
