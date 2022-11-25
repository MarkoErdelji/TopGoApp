package com.example.uberapp_tim6.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.models.Message;

import java.util.List;


/*
 * Adapteri unutar Android-a sluze da prikazu unapred nedefinisanu kolicinu podataka
 * pristigle sa interneta ili ucitane iz baze ili filesystem-a uredjaja.
 * Da bi napravili adapter treba da napraivmo klasu, koja nasledjuje neki od postojecih adaptera.
 * Za potrebe ovih vezbi koristicemo BaseAdapter koji je sposoban da kao izvor podataka iskoristi listu ili niz.
 * Nasledjivanjem bilo kog adaptera, dobicemo
 * nekolkko metoda koje moramo da referinisemo da bi adapter ispravno radio.
 * */
public class MessageAdapter extends BaseAdapter{
    private Activity activity;
    private List<Message> messages;

    public MessageAdapter(Activity activity,List<Message> messages) {
        this.activity = activity;
        this.messages = messages;
    }

    /*
     * Ova metoda vraca ukupan broj elemenata u listi koje treba prikazati
     * */
    @Override
    public int getCount() {
        return this.messages.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Override
    public Object getItem(int position) {
        return this.messages.get(position);
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
        Message message = this.messages.get(position);

        if(convertView==null)
            vi = activity.getLayoutInflater().inflate(R.layout.message_list, null);

        TextView title = (TextView)vi.findViewById(R.id.name);
        TextView date = (TextView)vi.findViewById(R.id.description);

        title.setText(message.getSender().getFirstName() +" "+ message.getSender().getLastName());
        date.setText(message.getDateTime().toString());

        return  vi;
    }
}
