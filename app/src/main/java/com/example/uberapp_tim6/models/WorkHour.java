package com.example.uberapp_tim6.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class WorkHour implements Serializable  {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Driver driver;

    public WorkHour(int id, LocalDateTime start, LocalDateTime end, Driver driver) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.driver = driver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
