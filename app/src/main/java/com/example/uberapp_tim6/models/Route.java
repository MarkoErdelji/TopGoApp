package com.example.uberapp_tim6.models;

import java.io.Serializable;

public class Route implements Serializable  {
   private int id;
   private Location begginingLocation;
   private Location destination;
   private float lenght;
   private float estimatedTime;
   private float price;

    public Route(int id, Location begginingLocation, Location destination, float lenght, float estimatedTime, float price) {
        this.id = id;
        this.begginingLocation = begginingLocation;
        this.destination = destination;
        this.lenght = lenght;
        this.estimatedTime = estimatedTime;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getBegginingLocation() {
        return begginingLocation;
    }

    public void setBegginingLocation(Location begginingLocation) {
        this.begginingLocation = begginingLocation;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public float getLenght() {
        return lenght;
    }

    public void setLenght(float lenght) {
        this.lenght = lenght;
    }

    public float getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(float estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
