package com.android.exsell.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.exsell.R;
import com.android.exsell.UI.Home;
import com.android.exsell.UI.NotificationActivity;
import com.android.exsell.listeners.navigationListener;
import com.google.android.material.navigation.NavigationView;

public class FragmentTopBar extends Fragment {
    DrawerLayout drawer;
    private navbarHamburgerOnClickCallback navbarHamburgerOnClickCallback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_bar, container, false);
        this.navbarHamburgerOnClickCallback = (navbarHamburgerOnClickCallback) getActivity();
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
                startActivity(new Intent(getActivity(), NotificationActivity.class));
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
}