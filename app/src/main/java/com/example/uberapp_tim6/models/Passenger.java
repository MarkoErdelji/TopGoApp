package com.example.uberapp_tim6.models;

import java.time.LocalDate;
import java.util.List;

public class Passenger extends User {
    private List<Payment> payments;
    private List<Ride> rides;
    private List<Route> favouriteRoutes;

    public Passenger(int id, String firstName, String lastName, String email, String password,
                     String phoneNumber, LocalDate dateOfBirth, String address,
                     List<Payment> payments, List<Ride> rides, List<Route> favouriteRoutes) {
        super(id, firstName, lastName, email, password, phoneNumber, dateOfBirth, address);
        this.favouriteRoutes =favouriteRoutes;
        this.payments = payments;
        this.rides = rides;
    }


    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Ride> getRides() {
        return rides;
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }

    public List<Route> getFavouriteRoutes() {
        return favouriteRoutes;
    }

    public void setFavouriteRoutes(List<Route> favouriteRoutes) {
        this.favouriteRoutes = favouriteRoutes;
    }
}
