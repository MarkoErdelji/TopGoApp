package com.example.uberapp_tim6.DTOS;

import java.time.LocalDateTime;

public class UserRejectionLetterDTO {

    public String reason;
    public LocalDateTime timeOfRejection;

    public UserRejectionLetterDTO() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getTimeOfRejection() {
        return timeOfRejection;
    }

    public void setTimeOfRejection(LocalDateTime timeOfRejection) {
        this.timeOfRejection = timeOfRejection;
    }
}
