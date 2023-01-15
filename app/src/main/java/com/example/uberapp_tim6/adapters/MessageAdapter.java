package com.example.uberapp_tim6.adapters;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserMessagesDTO;
import com.example.uberapp_tim6.DTOS.UserMessagesListDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.services.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    private List<UserInfoDTO> messages;
    private UserInfoDTO driver;
    private List<String> senders;
    private View vi;

    public MessageAdapter(Activity activity, List<UserInfoDTO> messages, UserInfoDTO driver) {
        this.activity = activity;
        this.messages = messages;
        this.driver  = driver;
        this.senders = new ArrayList<>();
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



    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        vi=convertView;
        UserInfoDTO message = this.messages.get(position);
        Log.d("position", String.valueOf(position));

        if(convertView==null)
            vi = activity.getLayoutInflater().inflate(R.layout.message_list, null);
        TextView title = vi.findViewById(R.id.name);
        ImageView pfp = vi.findViewById(R.id.profilePicture);
        title.setText(message.getName() + " " + message.getSurname());
        Glide.with(activity.getApplicationContext()).load(message.getProfilePicture()).into(pfp);








        return  vi;
    }
}
