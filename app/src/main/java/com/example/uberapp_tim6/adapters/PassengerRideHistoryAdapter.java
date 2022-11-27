package com.example.uberapp_tim6.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.models.Ride;

import java.util.List;

public class PassengerRideHistoryAdapter extends BaseAdapter {
    private Activity activity;
    private List<Ride> rides;

    public PassengerRideHistoryAdapter(Activity activity,List<Ride> rides) {
        this.activity = activity;
        this.rides = rides;
    }

    /*
     * Ova metoda vraca ukupan broj elemenata u listi koje treba prikazati
     * */
    @Override
    public int getCount() {
        return this.rides.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Override
    public Object getItem(int position) {
        return this.rides.get(position);
    }


    /*
     * Ova metoda vraca jedinstveni identifikator, za adaptere koji prikazuju
     * listu ili niz, pozicija je dovoljno dobra. Naravno mozemo iskoristiti i
     * jedinstveni identifikator objekta, ako on postoji.
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Ova metoda popunjava pojedinacan element ListView-a podacima.
     * Ako adapter cuva listu od n elemenata, adapter ce u petlji ici
     * onoliko puta koliko getCount() vrati. Prilikom svake iteracije
     * uzece java objekat sa odredjene poziciuje (model) koji cuva podatke,
     * i layout koji treba da prikaze te podatke (view) npr R.layout.cinema_list.
     * Kada adapter ima model i view, prosto ce uzeti podatke iz modela,
     * popuniti view podacima i poslati listview da prikaze, i nastavice
     * sledecu iteraciju.
     * */
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        Ride rideHistory = this.rides.get(position);

        if(convertView==null)
            vi = activity.getLayoutInflater().inflate(R.layout.passenger_ride_history_list, null);

        TextView startDate = (TextView)vi.findViewById(R.id.startDate);
        TextView date = (TextView)vi.findViewById(R.id.date);
        TextView name = (TextView)vi.findViewById(R.id.driverName);
        TextView price = (TextView)vi.findViewById(R.id.price);
        TextView route = (TextView)vi.findViewById(R.id.route);
        name.setText(rideHistory.getDriver().getFirstName() + " " + rideHistory.getDriver().getLastName());
        date.setText(rideHistory.getBeggining().getHour() + ":" + rideHistory.getBeggining().getMinute() + "-" +
                rideHistory.getEnd().getHour() + ":" + rideHistory.getEnd().getMinute());
        startDate.setText(rideHistory.getBeggining().toLocalDate().toString());
        price.setText(Float.toString(rideHistory.getPrice()));
        route.setText(rideHistory.getRoute().getBegginingLocation().getLatitude());
        //String infoText = "Destination: " + rideHistory.getDestination() + " Price: " + rideHistory.getPrice();
        //info.setText(infoText);


        return  vi;
    }
}

