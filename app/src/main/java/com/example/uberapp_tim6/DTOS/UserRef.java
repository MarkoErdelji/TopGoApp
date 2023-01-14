package com.example.uberapp_tim6.DTOS;

import com.example.uberapp_tim6.models.Passenger;

import java.io.Serializable;

public class UserRef implements Serializable {
    Integer id;

    String email;

    public UserRef() {
    }
    public UserRef(Passenger passenger){
        this.id = passenger.getId();
        this.email = passenger.getEmail();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
