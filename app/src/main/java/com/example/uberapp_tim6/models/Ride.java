package com.example.uberapp_tim6.models;

import com.example.uberapp_tim6.models.enumerations.Status;
import com.example.uberapp_tim6.models.enumerations.VehicleName;

import java.time.LocalDateTime;
import java.util.List;

public class Ride {
    private int id;
    private Driver driver;
    private List<Passenger> passenger;
    private LocalDateTime beggining;
    private LocalDateTime end;
    private float price;
    private Status status;
    private boolean forBabies;
    private boolean forAnimals;
    private boolean panic;
    private VehicleName vehicleName;
    private boolean splitFare;
    private List<Review> reviews;
    private List<Payment> payments;
    private RejectionLetter RejectionLetter;
    private Route route;

    public Ride(int id, Driver driver, List<Passenger> passenger, LocalDateTime beggining,  float price, Status status, boolean forBabies, boolean forAnimals, boolean panic, VehicleName vehicleName, boolean splitFare, List<Review> reviews, List<Payment> payments, com.example.uberapp_tim6.models.RejectionLetter rejectionLetter, Route route) {
        this.id = id;
        this.driver = driver;
        this.passenger = passenger;
        this.beggining = beggining;
        this.end = null;
        this.price = price;
        this.status = status;
        this.forBabies = forBabies;
        this.forAnimals = forAnimals;
        this.panic = panic;
        this.vehicleName = vehicleName;
        this.splitFare = splitFare;
        this.reviews = reviews;
        this.payments = payments;
        RejectionLetter = rejectionLetter;
        this.route = route;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public List<Passenger> getPassenger() {
        return passenger;
    }

    public void setPassenger(List<Passenger> passenger) {
        this.passenger = passenger;
    }

    public LocalDateTime getBeggining() {
        return beggining;
    }

    public void setBeggining(LocalDateTime beggining) {
        this.beggining = beggining;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isForBabies() {
        return forBabies;
    }

    public void setForBabies(boolean forBabies) {
        this.forBabies = forBabies;
    }

    public boolean isForAnimals() {
        return forAnimals;
    }

    public void setForAnimals(boolean forAnimals) {
        this.forAnimals = forAnimals;
    }

    public boolean isPanic() {
        return panic;
    }

    public void setPanic(boolean panic) {
        this.panic = panic;
    }

    public VehicleName getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(VehicleName vehicleName) {
        this.vehicleName = vehicleName;
    }

    public boolean isSplitFare() {
        return splitFare;
    }

    public void setSplitFare(boolean splitFare) {
        this.splitFare = splitFare;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReview(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public com.example.uberapp_tim6.models.RejectionLetter getRejectionLetter() {
        return RejectionLetter;
    }

    public void setRejectionLetter(com.example.uberapp_tim6.models.RejectionLetter rejectionLetter) {
        RejectionLetter = rejectionLetter;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
