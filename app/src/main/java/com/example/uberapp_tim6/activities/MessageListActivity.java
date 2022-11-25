package com.example.uberapp_tim6.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.adapters.MessageListAdapter;
import com.example.uberapp_tim6.driver.models.Message;
import com.example.uberapp_tim6.driver.models.User;
import com.example.uberapp_tim6.tools.Mokap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_test);

        Toolbar toolbar = findViewById(R.id.toolbar_gchannel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mMessageRecycler = (RecyclerView) findViewById(R.id.recycler_gchat);
        List<Message> messageList = Mokap.getMessages();
        //

        //

        mMessageAdapter = new MessageListAdapter(this, messageList,(User)getIntent().getSerializableExtra("currentUser"));
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        this.finish();
        return true;
    }

}