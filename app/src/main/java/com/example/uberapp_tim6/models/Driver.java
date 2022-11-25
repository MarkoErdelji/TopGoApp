package com.example.uberapp_tim6.models;

import java.time.LocalDate;
import java.util.List;

public class Driver extends User{
    private String imagePath;
    private boolean isActive;
    private List<Ride> rides;

    public Driver(int id, String firstName, String lastName, String email, String password,
                  String phoneNumber, LocalDate dateOfBirth, String address,
                  String imagePath, boolean isActive, List<Ride> rides) {
        super(id, firstName, lastName, email, password, phoneNumber, dateOfBirth, address);
        this.imagePath = imagePath;
        this.isActive = isActive;
        this.rides = rides;

    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }
}
