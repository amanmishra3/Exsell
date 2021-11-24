package com.android.exsell.listeners;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.exsell.R;
import com.android.exsell.UI.LoginActivity;
import com.android.exsell.UI.UserProfile;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class navigationListener implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG = "navigationListener";
    Context context;
    public navigationListener (Context context) {
        this.context = context;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.signOut) {
            onSignOut();
            return true;
        } else if(item.getItemId() == R.id.profile) {
            onClickProfile();
            return true;
        }
        return false;
    }

    private void onClickProfile() {
        Log.i(TAG, "onProfile");
        Intent intent = new Intent(context, UserProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void onSignOut() {
        Log.i(TAG, "onSignOut");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
        }
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}

//To call the listener
// navigationView.setNavigationItemSelectedListener(new navigationListener(getApplicationContext()));
//remember to pass the context
