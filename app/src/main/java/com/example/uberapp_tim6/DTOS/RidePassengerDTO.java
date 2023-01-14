package com.example.uberapp_tim6.DTOS;

public class RidePassengerDTO {
    /* "id": 123,
             "email": "user@example.com",
             "type": "VOZAC"*/
    private Integer id;


    private String email;

    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
