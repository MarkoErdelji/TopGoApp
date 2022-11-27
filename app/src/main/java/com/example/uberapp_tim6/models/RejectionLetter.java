package com.example.uberapp_tim6.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RejectionLetter  implements Serializable {
    private int id;
    private String reason;
    private LocalDateTime timeOfRejection;
    private Ride ride;

    public RejectionLetter(int id, String reason, LocalDateTime timeOfRejection, Ride ride) {
        this.id = id;
        this.reason = reason;
        this.timeOfRejection = timeOfRejection;
        this.ride = ride;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getTimeOfRejection() {
        return timeOfRejection;
    }

    public void setTimeOfRejection(LocalDateTime timeOfRejection) {
        this.timeOfRejection = timeOfRejection;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }
}
