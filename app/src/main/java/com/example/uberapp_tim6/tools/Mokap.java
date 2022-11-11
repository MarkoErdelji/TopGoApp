package com.example.uberapp_tim6.tools;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.uberapp_tim6.models.Message;
import com.example.uberapp_tim6.models.RideHistory;
import com.example.uberapp_tim6.models.User;


public class Mokap {

    public static List<Message> getMessages(){
        ArrayList<Message> messages = new ArrayList<Message>();
        Message u1 = new Message(5, "Blabla!","Sve najgore ti zelim pozdrav loool xd", LocalDateTime.now());
        Message u2 = new Message(3, "Alo bre","prebicu te skroz looool xd xd", LocalDateTime.now());
        Message u3 = new Message(6, "Kasnis 100 godina","Narucio sam voznju pre 100 sati a tebe jos nema ovo nista nije dobro",LocalDateTime.now());

        messages.add(u1);
        messages.add(u2);
        messages.add(u3);

        return messages;
    }


    public static List<Message> getPassengerMessages(){
        ArrayList<Message> messages = new ArrayList<Message>();
        Message u1 = new Message(1, "Cao!","Ovo je poruka za passengera", LocalDateTime.now());
        Message u2 = new Message(2, "TestTest","Druga poruka za passengera", LocalDateTime.now());
        Message u3 = new Message(4, "Ovo je malo duzi naslov","Ovo treba da bude najduza poruka za passengera da bi videli izgled teksta kada se ispise",LocalDateTime.now());

        messages.add(u1);
        messages.add(u2);
        messages.add(u3);

        return messages;
    }
    public static List<RideHistory> getDriverRideHistory(){
        ArrayList<RideHistory> rides = new ArrayList<RideHistory>();
        RideHistory rh1 = new RideHistory(1, "11.9.2022", "Luksemburg", "100e");
        RideHistory rh2 = new RideHistory(2, "11.9.2022", "Beograd", "1000e");
        RideHistory rh3 = new RideHistory(3, "11.9.2022", "Kikinda", "1500e");

        rides.add(rh1);
        rides.add(rh2);
        rides.add(rh3);
        return rides;
    }

    public static User getPassengerProfile(){
        return new User(1,"Marko","Markovic","markomarkovic@gmail.com","testtest","063221355", LocalDate.now());
    }

}
