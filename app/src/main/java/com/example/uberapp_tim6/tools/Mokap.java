package com.example.uberapp_tim6.tools;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.uberapp_tim6.models.Message;


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

}
