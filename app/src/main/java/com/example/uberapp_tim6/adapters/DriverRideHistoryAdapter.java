package com.example.uberapp_tim6.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uberapp_tim6.DTOS.RideDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.models.Ride;
import com.example.uberapp_tim6.models.RideHistory;

import org.w3c.dom.Text;

import java.util.List;

public class DriverRideHistoryAdapter extends BaseAdapter {
    private Activity activity;
    private List<RideDTO> rides;

    public DriverRideHistoryAdapter(Activity activity,List<RideDTO> rides) {
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
        RideDTO rideHistory = this.rides.get(position);

        if(convertView==null)
            vi = activity.getLayoutInflater().inflate(R.layout.histoty_item_list, null);

        TextView date = (TextView)vi.findViewById(R.id.dateTextView);
        TextView beggining = (TextView)vi.findViewById(R.id.begginingTextView);
        TextView end = (TextView)vi.findViewById(R.id.endTextView);
        TextView price = (TextView)vi.findViewById(R.id.priceTextView);

        String dateText = "" + rideHistory.getStartTime().toLocalDate();
        date.setText(dateText);

        String begginingText = "Beggining: " + rideHistory.getLocations().get(0).getDeparture().getAddress();
        beggining.setText(begginingText);

        String endText = "End: " + rideHistory.getLocations().get(0).getDestination().getAddress();
        end.setText(endText);

        String priceText = "Price: " + rideHistory.getTotalCost() + "RSD";
        price.setText(priceText);


        return  vi;
    }
}
