package com.example.uberapp_tim6.driver.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.activities.MessageDetailActivity;
import com.example.uberapp_tim6.activities.MessageListActivity;
import com.example.uberapp_tim6.adapters.MessageAdapter;
import com.example.uberapp_tim6.driver.models.Message;
import com.example.uberapp_tim6.driver.models.User;
import com.example.uberapp_tim6.tools.Mokap;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverInboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverInboxFragment extends ListFragment {

    User currentUser;


    public static DriverInboxFragment newInstance() {
        return new DriverInboxFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_driver_inbox, vg, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Message message = Mokap.getMessages().get(position);




 /*     Ako nasoj aktivnosti zelimo da posaljemo nekakve podatke
      za to ne treba da koristimo konstruktor. Treba da iskoristimo
      identicnu strategiju koju smo koristili kda smo radili sa
      fragmentima! Koristicemo Bundle za slanje podataka. Tacnije
     intent ce formirati Bundle za nas, ali mi treba da pozovemo
      odgovarajucu putExtra metodu.*/

        Intent intent = new Intent(getActivity(), MessageListActivity.class);
        intent.putExtra("Sender", message.getSender().getFirstName() + message.getSender().getLastName());
        intent.putExtra("text", message.getDateTime().toString());
        intent.putExtra("currentUser", currentUser);
        startActivityForResult(intent, 0);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        currentUser = Mokap.getUsers().get(1);
        super.onCreate(savedInstanceState);
        Toast.makeText(getActivity(), "onActivityCreated()", Toast.LENGTH_SHORT).show();
        List<Message> currentUserMessager = new ArrayList<Message>();
        //Dodaje se adapter
        for (Message m:Mokap.getMessages())
        {
            if (m.getSender().getId() == currentUser.getId() || m.getReceiver().getId() == currentUser.getId())
            {
                currentUserMessager.add(m);
            }

        }
        MessageAdapter adapter = new MessageAdapter(getActivity(),currentUserMessager);
        setListAdapter(adapter);
    }
}

    /*
     * Ako zelimo da nasa aktivnost/fragment prikaze ikonice unutar toolbar-a
     * to se odvija u nekoliko koraka. potrebno je da napravimo listu koja
     * specificira koje sve ikonice imamo unutar menija, koje ikone koristimo
     * i da li se uvek prikazuju ili ne. Nakon toga koristeci metdu onCreateOptionsMenu
     * postavljamo ikone na toolbar.
     * */
/*    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // ovo korostimo ako je nasa arhitekrura takva da imamo jednu aktivnost
        // i vise fragmentaa gde svaki od njih ima svoj menu unutar toolbar-a
        menu.clear();
        inflater.inflate(R.menu.activity_itemdetail, menu);
    }*/

    /*
     * Da bi znali na koju ikonicu je korisnik kliknuo
     * treba da iskoristimo jedinstveni identifikator
     * za svaki element u listi. Metoda onOptionsItemSelected
     * ce vratiti MenuItem na koji je korisnik kliknuo. Ako znamo
     * da ce on vratiti i id, tacno znamo na koji element je korisnik
     * kliknuo, i shodno tome reagujemo.
     * */
  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_refresh){
            Toast.makeText(getActivity(), "Refresh App", Toast.LENGTH_SHORT).show();
        }
        if(id == R.id.action_new){
            Toast.makeText(getActivity(), "Create Text", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}*/