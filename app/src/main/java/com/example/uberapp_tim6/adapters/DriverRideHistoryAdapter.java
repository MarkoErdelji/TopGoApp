package com.example.uberapp_tim6.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.models.Message;
import com.example.uberapp_tim6.models.RideHistory;

import java.util.List;

public class DriverRideHistoryAdapter extends BaseAdapter {
    private Activity activity;
    private List<RideHistory> rides;

    public DriverRideHistoryAdapter(Activity activity,List<RideHistory> rides) {
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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        RideHistory rideHistory = this.rides.get(position);

        if(convertView==null)
            vi = activity.getLayoutInflater().inflate(R.layout.message_list, null);

        TextView date = (TextView)vi.findViewById(R.id.name);
        TextView info = (TextView)vi.findViewById(R.id.description);
        String dateText  = "Date: " + rideHistory.getDate();
        date.setText(dateText);
        String infoText = "Destination: " + rideHistory.getDestination() + " Price: " + rideHistory.getPrice();
        info.setText(infoText);


        return  vi;
    }
}
