package com.example.uberapp_tim6.driver.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.uberapp_tim6.DTOS.UserInfoDTO;
import com.example.uberapp_tim6.DTOS.UserMessagesDTO;
import com.example.uberapp_tim6.DTOS.UserMessagesListDTO;
import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.activities.MessageListActivity;
import com.example.uberapp_tim6.adapters.MessageAdapter;

import com.example.uberapp_tim6.services.ServiceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverInboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverInboxFragment extends ListFragment {

    private static final String ARG_DRIVER = "arg_driver";

    UserInfoDTO driver;
    List<UserMessagesDTO> messages;
    List<String> ids = new ArrayList<>();


    public static DriverInboxFragment newInstance(UserInfoDTO d) {
        DriverInboxFragment fragment = new DriverInboxFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DRIVER, d);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_driver_inbox, vg, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String userId = ids.get(position);





        Intent intent = new Intent(getActivity(), MessageListActivity.class);
        intent.putExtra("Sender",userId);
        intent.putExtra("text", "message.getDateTime().toString()");
        intent.putExtra("currentUser",driver);
        startActivityForResult(intent, 0);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        driver = (UserInfoDTO) getArguments().getSerializable(ARG_DRIVER);
        super.onCreate(savedInstanceState);
        Call<UserMessagesListDTO> call = ServiceUtils.userService.getUserMessages(driver.getId().toString());
        call.enqueue(new Callback<UserMessagesListDTO>() {
            @Override
            public void onResponse(Call<UserMessagesListDTO> call, Response<UserMessagesListDTO> response) {
                messages = response.body().getResults();
                Log.d("TEST",response.body().getResults().get(0).message);
                UserMessagesListDTO messages = new UserMessagesListDTO();
                for (UserMessagesDTO msg:response.body().getResults())
                {
                    String id = "";
                    if (msg.getSenderId() != driver.getId())
                    {
                        id = msg.getSenderId().toString();
                    }
                    if (msg.getSenderId() == driver.getId())
                    {
                        id = msg.getReceiverId().toString();
                    }

                    if(!ids.contains(id))
                    {
                        messages.getResults().add(msg);
                        ids.add(id);
                    }

                }
                getChatters(ids);


                MessageAdapter adapter = null;
                try {
                    adapter = new MessageAdapter(getActivity(),getChatters(ids).get(),driver);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setListAdapter(adapter);

            }

            @Override
            public void onFailure(Call<UserMessagesListDTO> call, Throwable t) {
                Log.d("TEST",t.getMessage());

            }
        });

    }

    private CompletableFuture<List<UserInfoDTO>> getChatters(List<String> ids) {
        List<CompletableFuture<UserInfoDTO>> futures = new ArrayList<>();
        for (String id : ids) {
            CompletableFuture<UserInfoDTO> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return ServiceUtils.passengerService.getPassengerById(id).execute().body();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            futures.add(future);
        }
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }
}

