package com.example.uberapp_tim6.DTOS;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class WorkHoursDTO {

    public int id;
    public LocalDateTime start;
    public LocalDateTime end;

    public WorkHoursDTO() {
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
    public long getDifferenceInSeconds(){
        return this.end.minusSeconds(this.start.toEpochSecond(ZoneOffset.UTC)).toEpochSecond(ZoneOffset.UTC);
    }
    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
