package com.example.uberapp_tim6.DTOS;

import com.example.uberapp_tim6.models.enumerations.Status;
import com.example.uberapp_tim6.models.enumerations.VehicleName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RideDTO {
    public Integer id;

    public LocalDateTime startTime;

    public LocalDateTime endTime;

    public float totalCost;

    public UserRef driver;

    public ArrayList<UserRef> passengers;

    public float estimatedTimeInMinutes;

    public VehicleName vehicleType;

    public boolean babyTransport;

    public boolean petTransport;

    public UserRejectionLetterDTO rejection;

    public List<RouteForCreateRideDTO> locations;

    public Status status;

    public RideDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public UserRef getDriver() {
        return driver;
    }

    public void setDriver(UserRef driver) {
        this.driver = driver;
    }

    public ArrayList<UserRef> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<UserRef> passengers) {
        this.passengers = passengers;
    }

    public float getEstimatedTimeInMinutes() {
        return estimatedTimeInMinutes;
    }

    public void setEstimatedTimeInMinutes(float estimatedTimeInMinutes) {
        this.estimatedTimeInMinutes = estimatedTimeInMinutes;
    }

    public VehicleName getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleName vehicleType) {
        this.vehicleType = vehicleType;
    }

    public boolean isBabyTransport() {
        return babyTransport;
    }

    public void setBabyTransport(boolean babyTransport) {
        this.babyTransport = babyTransport;
    }

    public boolean isPetTransport() {
        return petTransport;
    }

    public void setPetTransport(boolean petTransport) {
        this.petTransport = petTransport;
    }

    public UserRejectionLetterDTO getRejection() {
        return rejection;
    }

    public void setRejection(UserRejectionLetterDTO rejection) {
        this.rejection = rejection;
    }

    public List<RouteForCreateRideDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<RouteForCreateRideDTO> locations) {
        this.locations = locations;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
