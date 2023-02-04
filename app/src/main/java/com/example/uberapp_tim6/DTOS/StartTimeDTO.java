package com.example.uberapp_tim6.DTOS;

import java.time.LocalDateTime;

public class StartTimeDTO {
    private String start;



    public StartTimeDTO() {
        start = LocalDateTime.now().toString();
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
}
