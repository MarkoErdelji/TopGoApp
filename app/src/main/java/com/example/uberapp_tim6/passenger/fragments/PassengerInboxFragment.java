package com.example.uberapp_tim6.passenger.fragments;

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
import com.example.uberapp_tim6.adapters.MessageAdapter;
import com.example.uberapp_tim6.models.Message;
import com.example.uberapp_tim6.tools.Mokap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerInboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerInboxFragment extends ListFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PassengerInboxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PassengerInboxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PassengerInboxFragment newInstance() {
        return new PassengerInboxFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getActivity(), "Dobavljene poruke!!", Toast.LENGTH_SHORT).show();

        //Dodaje se adapter
        MessageAdapter adapter = new MessageAdapter(getActivity());
        setListAdapter(adapter);
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

        Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
        intent.putExtra("title", message.getTitle());
        intent.putExtra("text", message.getText());
        startActivityForResult(intent, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passenger_inbox, container, false);
    }
}