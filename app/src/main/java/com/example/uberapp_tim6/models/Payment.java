package com.example.uberapp_tim6.models;

import com.example.uberapp_tim6.models.enumerations.PaymentType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Payment implements Serializable {
    private int id;
    private PaymentType paymentType;
    private LocalDateTime date;
    private float amount;
    private Ride ride;

    public Payment(int id, PaymentType paymentType, LocalDateTime date, float amount, Ride ride) {
        this.id = id;
        this.paymentType = paymentType;
        this.date = date;
        this.amount = amount;
        this.ride = ride;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }


}
