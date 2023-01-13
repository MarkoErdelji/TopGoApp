package com.example.uberapp_tim6.DTOS;

public class GeoLocationDTO {

    private String address;

    private float latitude;

    private float longitude;

    public GeoLocationDTO() {
    }

    public GeoLocationDTO(float v, float v1) {
        setLatitude(v);
        setLongitude(v1);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
