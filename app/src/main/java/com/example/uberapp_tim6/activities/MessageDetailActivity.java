package com.example.uberapp_tim6.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.uberapp_tim6.R;

public class MessageDetailActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView msgTitle = findViewById(R.id.title);
        TextView msgText = findViewById(R.id.text);


        msgTitle.setText(getIntent().getStringExtra("title"));
        msgText.setText(getIntent().getStringExtra("text"));
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        this.finish();
        return true;
    }


}