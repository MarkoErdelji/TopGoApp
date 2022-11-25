package com.example.uberapp_tim6.models;

public class Vehicle {
    private int id;
    private Driver driver;
    private String model;
    private String licencePlate;
    private int seatNumber;
    private boolean forBabies;
    private boolean forAnimals;
    private Location currentLocation;

    public Vehicle(int id, Driver driver, String model, String licencePlate, int seatNumber, boolean forBabies, boolean forAnimals, Location currentLocation) {
        this.id = id;
        this.driver = driver;
        this.model = model;
        this.licencePlate = licencePlate;
        this.seatNumber = seatNumber;
        this.forBabies = forBabies;
        this.forAnimals = forAnimals;
        this.currentLocation = currentLocation;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
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

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
