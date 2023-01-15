package com.example.uberapp_tim6.DTOS;

import java.util.List;

public class MatrixAPIDTO {
    List<List<Double>> locations;
    List<String> metrics;


    public MatrixAPIDTO() {
    }

    public List<List<Double>> getLocations() {
        return locations;
    }

    public void setLocations(List<List<Double>> locations) {
        this.locations = locations;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<String> metrics) {
        this.metrics = metrics;
    }
}
