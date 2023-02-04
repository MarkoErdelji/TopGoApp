package com.example.uberapp_tim6.DTOS;

import java.time.LocalDateTime;

public class EndTimeDTO {
    private String end;


    public EndTimeDTO() {
        end = LocalDateTime.now().toString();
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
