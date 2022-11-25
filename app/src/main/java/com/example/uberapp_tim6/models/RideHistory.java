package com.example.uberapp_tim6.models;

import java.time.LocalDateTime;

public class RideHistory {
    private int id;
    private String date;
    private String destination;
    private String price;

    public RideHistory(int id, String date, String destination, String price){
        this.id = id;
        this.date = date;
        this.destination = destination;
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public String getDestination() {
        return destination;
    }

    public String getPrice() {
        return price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
