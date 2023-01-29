package com.example.uberapp_tim6.DTOS;

import com.example.uberapp_tim6.models.enumerations.VehicleName;

import java.time.LocalDateTime;
import java.util.List;

public class CreateRideDTO {

        private List<RouteForCreateRideDTO> locations ;

        private List<RidePassengerDTO> passengers;

        private VehicleName vehicleType;

        private boolean babyTransport;

        private boolean petTransport;

        private String scheduledTime;

    @Override
    public String toString() {
        return "CreateRideDTO{" +
                "locations=" + locations +
                ", passengers=" + passengers +
                ", vehicleType=" + vehicleType +
                ", babyTransport=" + babyTransport +
                ", petTransport=" + petTransport +
                ", scheduledTime=" + scheduledTime+
                '}';
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

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public List<RouteForCreateRideDTO> getLocations() {
            return locations;
        }

        public void setLocations(List<RouteForCreateRideDTO> locations) {
            this.locations = locations;
        }

        public List<RidePassengerDTO> getPassengers() {
            return passengers;
        }

        public void setPassengers(List<RidePassengerDTO> passengers) {
            this.passengers = passengers;
        }

        public VehicleName getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(VehicleName vehicleType) {
            this.vehicleType = vehicleType;
        }

        public Boolean getBabyTransport() {
            return babyTransport;
        }

        public void setBabyTransport(Boolean babyTransport) {
            this.babyTransport = babyTransport;
        }

        public Boolean getPetTransport() {
            return petTransport;
        }

        public void setPetTransport(Boolean petTransport) {
            this.petTransport = petTransport;
        }
    }
