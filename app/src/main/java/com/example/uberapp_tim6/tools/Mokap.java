package com.example.uberapp_tim6.tools;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.uberapp_tim6.driver.models.Message;
import com.example.uberapp_tim6.driver.models.RideHistory;
import com.example.uberapp_tim6.driver.models.User;


public class Mokap {

    public static List<Message> getMessages(){
        ArrayList<Message> messages = new ArrayList<Message>();
        List<User> users = getUsers();
        messages.add(new Message(1, "Panic","Ovo je poruka za passengera", LocalDateTime.now(),users.get(1),users.get(2)));
        messages.add(new Message(2, "Message","Alo", LocalDateTime.now(),users.get(1),users.get(2)));
        messages.add(new Message(3, "Message","Prebicu te!", LocalDateTime.now(),users.get(2),users.get(1)));
        messages.add(new Message(4, "Message","Debilu", LocalDateTime.now(),users.get(3),users.get(2)));
        messages.add(new Message(5, "Message","TESTTEST", LocalDateTime.now(),users.get(2),users.get(3)));
        messages.add(new Message(6, "Message","Sram te bilo", LocalDateTime.now(),users.get(1),users.get(4)));
        messages.add(new Message(7, "Message","Nista ne valja", LocalDateTime.now(),users.get(2),users.get(5)));


        return messages;
    }


    public static List<Message> getPassengerMessages(){
        ArrayList<Message> messages = new ArrayList<Message>();
        List<User> users = getUsers();
        messages.add(new Message(1, "Panic","Ovo je poruka za passengera", LocalDateTime.now(),users.get(1),users.get(2)));
        messages.add(new Message(2, "Message","Alo", LocalDateTime.now(),users.get(1),users.get(2)));
        messages.add(new Message(3, "Message","Prebicu te!", LocalDateTime.now(),users.get(2),users.get(1)));
        messages.add(new Message(4, "Message","Debilu", LocalDateTime.now(),users.get(3),users.get(2)));
        messages.add(new Message(5, "Message","TESTTEST", LocalDateTime.now(),users.get(2),users.get(3)));
        messages.add(new Message(6, "Message","Sram te bilo", LocalDateTime.now(),users.get(1),users.get(4)));
        messages.add(new Message(7, "Message","Nista ne valja", LocalDateTime.now(),users.get(2),users.get(5)));






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
        return new User(1,"Marko","Markovic","markomarkovic@gmail.com","testtest","063221355", LocalDate.now(),"bla");
    }

    public static List<User> getUsers(){
        List<User> users = new ArrayList<User>();
        users.add(new User(1,"Marko","Markovic","markomarkovic@gmail.com","testtest","063221355", LocalDate.now(),"bla"));
        users.add(new User(2,"Dejan","Govedo","deci@gmail.com","testtest","063222355", LocalDate.now(),"Bulevara Srdjana Gluponavica 5"));
        users.add(new User(3,"Petar","Petrovic","tarpe50@gmail.com","testtest","063221355", LocalDate.now(),"bla"));
        users.add(new User(4,"Dzoni","Sinic","dzonson@gmail.com","testtest","063221355", LocalDate.now(),"bla"));
        users.add(new User(5,"Jozef","Titivoc","madjar12@gmail.com","testtest","063221355", LocalDate.now(),"bla"));
        users.add(new User(6,"Kristijan","Golubovic","kikistar@gmail.com","testtest","063221355", LocalDate.now(),"bla"));
        return  users;
    }

}
