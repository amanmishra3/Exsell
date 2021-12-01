package com.android.exsell.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.exsell.R;
import com.android.exsell.UI.Home;
import com.android.exsell.services.FirebaseNotificationService;
import com.android.exsell.services.NotificationEvent;

public class FragmentTopBar extends Fragment implements NotificationEvent {
    DrawerLayout drawer;
    private navbarHamburgerOnClickCallback navbarHamburgerOnClickCallback;
    private NotificationBellClickCallback notificationBellClickCallback;
    private ImageView notificationBell;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_bar, container, false);
        FirebaseNotificationService.notificationEventRegister(this::newNotification);
        this.navbarHamburgerOnClickCallback = (navbarHamburgerOnClickCallback) getActivity();
        this.notificationBellClickCallback = (NotificationBellClickCallback) getActivity();
        this.notificationBell = (ImageView) view.findViewById(R.id.notificationButton);
        view.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameTopBar, new FragmentSearchBar());
                fragmentTransaction.commit();
            }
        });

        view.findViewById(R.id.appName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Home.class));
            }
        });

        view.findViewById(R.id.notificationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notificationBellClickCallback != null) {
                    notificationBellClickCallback.onNotificationBellClick();
                }
            }
        });

        view.findViewById(R.id.leftNavigationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("InsideFragmentTopBar", "Clicked Navigation Button");
                if(navbarHamburgerOnClickCallback != null) {
                    Log.i("InsideFragmentSearchBar", "callback Attached");
                    navbarHamburgerOnClickCallback.onHamburgerClickCallback();
                }
            }
        });
        return view;
    }

    @Override
    public void newNotification() {
        this.notificationBell.setImageResource(R.drawable.ic_bell);
    }

    public interface navbarHamburgerOnClickCallback {
        void onHamburgerClickCallback();
    }
    public interface NotificationBellClickCallback {
        void onNotificationBellClick();
    }
}