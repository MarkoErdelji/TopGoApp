package com.example.uberapp_tim6.DTOS;

import com.example.uberapp_tim6.models.enumerations.VehicleName;

import java.util.List;

public class FavouriteRideDTO {

    private String favoriteName;

    private List<RouteForCreateRideDTO> locations;

    private List<UserRef> passengers;

    private VehicleName vehicleType;

    private boolean babyTransport;

    private boolean petTransport;

    public FavouriteRideDTO() {
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public List<RouteForCreateRideDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<RouteForCreateRideDTO> locations) {
        this.locations = locations;
    }

    public List<UserRef> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<UserRef> passengers) {
        this.passengers = passengers;
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
}
