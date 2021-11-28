package com.android.exsell.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.exsell.R;
import com.android.exsell.UI.Home;

public class FragmentTopBar extends Fragment {
    DrawerLayout drawer;
    private navbarHamburgerOnClickCallback navbarHamburgerOnClickCallback;
    private NotificationBellClickCallback notificationBellClickCallback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_bar, container, false);
        this.navbarHamburgerOnClickCallback = (navbarHamburgerOnClickCallback) getActivity();
        this.notificationBellClickCallback = (NotificationBellClickCallback) getActivity();
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

    public interface navbarHamburgerOnClickCallback {
        void onHamburgerClickCallback();
    }
    public interface NotificationBellClickCallback {
        void onNotificationBellClick();
    }
}