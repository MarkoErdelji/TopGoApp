package com.example.uberapp_tim6.models;

import java.time.LocalDateTime;
import java.util.Date;

public class Message {
      private int id;
      private String title;
      private String text;
      private LocalDateTime dateTime;
      //private User
      //private User


    public Message(int id, String title,String text, LocalDateTime dateTime) {
        this.title = title;
        this.id = id;
        this.text = text;
        this.dateTime = dateTime;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }


}
