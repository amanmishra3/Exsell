package com.android.exsell.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.exsell.R;
import com.android.exsell.adapters.NotificationAdapter;
import com.android.exsell.fragments.FragmentSearchBar;
import com.android.exsell.fragments.FragmentTopBar;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.models.Notifications;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity implements FragmentTopBar.navbarHamburgerOnClickCallback, FragmentSearchBar.SearchBarOnSearch {
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    ArrayList<Notifications> notificationArrayList;
    String[] notificationHeadings;
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    private ImageView search, wishlist, addListing, message, notification;
    NavigationView navigationView;
    private String TAG = "NotificationActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notification);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameTopBar, new FragmentTopBar());
        fragmentTransaction.commit();

        layoutBottom = (LinearLayout) findViewById(R.id.layoutBottomBar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayoutNotification);
        recyclerView = findViewById(R.id.new_notifications_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        wishlist = (ImageView) layoutBottom.findViewById(R.id.wishlistButton);
        wishlist.setOnClickListener(new TopBottomNavigationListener(R.id.wishlistButton, getApplicationContext()));
        addListing = (ImageView) layoutBottom.findViewById(R.id.addItemButton);
        addListing.setOnClickListener(new TopBottomNavigationListener(R.id.addItemButton, getApplicationContext()));
        message = (ImageView) findViewById(R.id.chatButton);
        message.setOnClickListener(new TopBottomNavigationListener(R.id.chatButton, getApplicationContext()));

        notificationArrayList = new ArrayList<Notifications>();

        notificationAdapter = new NotificationAdapter(this, notificationArrayList);
        recyclerView.setAdapter(notificationAdapter);

        notificationHeadings = new String[]{
                "the full form of USA is United State of America",
                "the capital of USA is Washington DC",
                "the currency of USA is US dollar",
                "the most popular language in USA is English",
                "there are 50 states in USA according its flag",
                "the president of USA is Donald Trump",
                "the USA celebrated their Independence Day on 4th July 1776",
                "the motto of USA is in god we trust",
                "the National Anthem of USA is The star spangled banner",
                "the full form of USA is United State of America",
                "the capital of USA is Washington DC",
                "the currency of USA is US dollar",
                "the most popular language in USA is English",
                "there are 50 states in USA according its flag",
                "the president of USA is Donald Trump",
                "the USA celebrated their Independence Day on 4th July 1776",
                "the motto of USA is in god we trust"
        };
        getData();
    }

    private void getData() {
        for(int i=0; i < notificationHeadings.length; i++){
            Notifications notifications = new Notifications(notificationHeadings[i]);
            notificationArrayList.add(notifications);
        }
        notificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHamburgerClickCallback() {
        Log.i(TAG,"onHamburgerClickCallback");
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void onSearch(String search) {
        Log.i(TAG,"onSearch received "+search);
    }
}