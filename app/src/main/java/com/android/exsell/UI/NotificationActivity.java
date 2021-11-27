package com.android.exsell.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.exsell.R;
import com.android.exsell.adapters.NotificationAdapter;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.models.Notifications;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    ArrayList<Notifications> notificationArrayList;
    String[] notificationHeadings;
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    private ImageView search, wishlist, addListing, message, notification;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notification);
        layoutTop = (LinearLayout) findViewById(R.id.layoutTopBar);
        layoutBottom = (LinearLayout) findViewById(R.id.layoutBottomBar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayoutNotification);
        recyclerView = findViewById(R.id.new_notifications_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        search = (ImageView) layoutTop.findViewById(R.id.searchButton);
        search.setOnClickListener(new TopBottomNavigationListener(R.id.searchButton, getApplicationContext()));
        wishlist = (ImageView) layoutBottom.findViewById(R.id.wishlistButton);
        wishlist.setOnClickListener(new TopBottomNavigationListener(R.id.wishlistButton, getApplicationContext()));
        addListing = (ImageView) layoutBottom.findViewById(R.id.addItemButton);
        addListing.setOnClickListener(new TopBottomNavigationListener(R.id.addItemButton, getApplicationContext()));
        message = (ImageView) findViewById(R.id.chatButton);
        message.setOnClickListener(new TopBottomNavigationListener(R.id.chatButton, getApplicationContext()));
        notification = (ImageView) findViewById(R.id.notificationButton);
        notification.setOnClickListener(new TopBottomNavigationListener(R.id.notificationButton, getApplicationContext()));
        layoutTop.findViewById(R.id.leftNavigationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
                navigationView.bringToFront();

            }
        });
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
}