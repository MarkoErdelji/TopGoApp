package com.example.uberapp_tim6.tools;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.uberapp_tim6.models.Driver;
import com.example.uberapp_tim6.models.Location;
import com.example.uberapp_tim6.models.Message;
import com.example.uberapp_tim6.models.Passenger;
import com.example.uberapp_tim6.models.Payment;
import com.example.uberapp_tim6.models.Review;
import com.example.uberapp_tim6.models.Ride;
import com.example.uberapp_tim6.models.RideHistory;
import com.example.uberapp_tim6.models.Route;
import com.example.uberapp_tim6.models.User;
import com.example.uberapp_tim6.models.enumerations.PaymentType;
import com.example.uberapp_tim6.models.enumerations.Status;
import com.example.uberapp_tim6.models.enumerations.VehicleName;


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

    public static List<Ride> getAllRides(){
        List<Ride> rides = new ArrayList<Ride>();
        Driver driver = new Driver(1,"Kristijan","Bubasvaba","markomarkovic@gmail.com","testtest","063221355", LocalDate.now(),"bla", "path", true, null);
        Passenger passenger = new Passenger( 1,"Marko","Filipovic","markomarkovic@gmail.com","testtest","063221355", LocalDate.now(),"bla", null, null, null);
        List<Passenger> passengers = new ArrayList<Passenger>();
        passengers.add(passenger);

        List<Review> reviews = new ArrayList<Review>();
        reviews.add(new Review(1, 4.5F, "Not great not terrible", passenger));
        Location beggigning = new Location("100'' W", "250' E");
        Location end = new Location("250'' W", "100' E");
        Route route = new Route(1, beggigning, end, 100F, 500F, 5000);

        rides.add(new Ride (1, driver, passengers, LocalDateTime.now(), 5000, Status.FINISHED,
                true, true, false, VehicleName.LUXURY, false, reviews, null, null, route
                ));
        Payment payment = new Payment(1, PaymentType.PAYPAL, LocalDateTime.now(), 5000, rides.get(0));
        List<Payment> payments = new ArrayList<Payment>();
        payments.add(payment);
        rides.get(0).setPayments(payments);

        Passenger passenger2 = new Passenger( 1,"Marko","Filipovic","markomarkovic@gmail.com","testtest","063221355", LocalDate.now(),"bla", null, null, null);
        passengers.clear();
        passengers.add(passenger2);
        List<Review> reviews2 = new ArrayList<Review>();
        reviews2.add(new Review(2, 3.0F, "Not great", passenger2));
        beggigning = new Location("100'' W", "250' E");
        end = new Location("250'' W", "100' E");
        route = new Route(2, beggigning, end, 100F, 500F, 5000);

        rides.add(new Ride (2, driver, passengers, LocalDateTime.now(), 5000, Status.FINISHED,
                true, true, false, VehicleName.LUXURY, false, reviews2, null, null, route
        ));
        Payment payment2 = new Payment(1, PaymentType.PAYPAL, LocalDateTime.now(), 5000, rides.get(0));payments = rides.get(0).getPayments();
        List<Payment> payments2 = new ArrayList<Payment>();
        payments2.add(payment2);
        rides.get(1).setPayments(payments);


        return rides;
    }

}
