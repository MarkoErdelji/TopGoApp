package com.example.uberapp_tim6.DTOS;

import java.io.Serializable;

public class UserRef implements Serializable {
    Integer id;

    String email;

    public UserRef() {
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
