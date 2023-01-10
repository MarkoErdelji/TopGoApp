package com.example.uberapp_tim6.driver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.uberapp_tim6.R;
import com.example.uberapp_tim6.UserLoginActivity;
import com.example.uberapp_tim6.activities.MessageListActivity;
import com.example.uberapp_tim6.adapters.DrawerListAdapter;
import com.example.uberapp_tim6.driver.fragments.DriverInboxFragment;
import com.example.uberapp_tim6.driver.fragments.DriverProfileFragment;
import com.example.uberapp_tim6.driver.fragments.DriverRideHistoryFragment;

import com.example.uberapp_tim6.models.NavItem;
import com.example.uberapp_tim6.tools.FragmentTransition;

import java.util.ArrayList;

public class DriverMainActivity extends AppCompatActivity {

    private DriverMainActivity dvm;
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    private View mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private RelativeLayout profileLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences userPrefs = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);

        Log.d("Cao majstore",userPrefs.getString("id","nema id"));
        Log.d("Cao majstore",userPrefs.getString("email","nema id"));
        Log.d("Cao majstore",userPrefs.getString("role","nema id"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);

        dvm = this;

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_topgo_logo);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        profileLayout = findViewById(R.id.profileBox);

        mTitle = getTitle();
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerList = findViewById(R.id.navList);

        mDrawerPane = findViewById(R.id.drawerPane);

        mNavItems.add(new NavItem("Inbox", "Driver inbox", R.drawable.ic_action_mail));
        mNavItems.add(new NavItem("History", "Ride history", R.drawable.history_icon));
        mNavItems.add(new NavItem("Test", "Test", R.drawable.history_icon));
        DrawerListAdapter DLA = new DrawerListAdapter(this, mNavItems);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransition.to(DriverProfileFragment.newInstance(), dvm, false,R.id.mainContent);
                mDrawerLayout.closeDrawers();
            }
        });

        //mDrawerLayout.setDrawerShadow(androidx.constraintlayout.widget.R.drawable.abc_ic_star_black_48dp, GravityCompat.START);

        mDrawerList.setAdapter(DLA);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle("iReviewer");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                startActivity(new Intent(DriverMainActivity.this, UserLoginActivity.class));
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
            Log.i("Pisi", String.valueOf(position));

        }
    }

    private void selectItemFromDrawer(int position) {
        if(position == 0){
            FragmentTransition.to(DriverInboxFragment.newInstance(), this, false,R.id.mainContent);
        }else if(position == 1){
            FragmentTransition.to(DriverRideHistoryFragment.newInstance(), this, false,R.id.mainContent);

        }else if(position == 2){
            startActivity(new Intent(DriverMainActivity.this, MessageListActivity.class));

        }else if(position == 3){

        }else if(position == 4){
            //..
        }else if(position == 5){
            //...
        }else{
            Log.e("DRAWER", "Nesto van opsega!");
        }

        mDrawerList.setItemChecked(position, true);
        if(position != 5) // za sve osim za sync
        {
            setTitle(mNavItems.get(position).getmTitle());
        }
        mDrawerLayout.closeDrawer(mDrawerPane);
    }
}