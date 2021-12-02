package com.android.exsell.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.exsell.R;
import com.android.exsell.UI.Categories;
import com.android.exsell.UI.Home;
import com.android.exsell.UI.LoginActivity;
import com.android.exsell.UI.MyListings;
import com.android.exsell.UI.UserProfile;
import com.android.exsell.db.ItemDb;
import com.android.exsell.db.UserDb;
import com.android.exsell.models.Notifications;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class navigationListener implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG = "navigationListener";
    Context context;
    Activity activity;
    static MenuItem previousMenuItem = null;
    public navigationListener(Context context, Activity activity) {
        Log.i(TAG, "const");
        this.context = context;
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.signOut) {
            onSignOut();
            return true;
        } else if(item.getItemId() == R.id.profile) {
            onClickProfile();
            return true;
        }
        else if (item.getItemId() == R.id.edit) {
            myList();
            return true;
        }
        else if (item.getItemId() == R.id.home) {
            home();
            return true;
        }
        return false;
    }

    private void home() {
        Log.i(TAG, "Home");

        Intent intent = new Intent(context, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void onClickProfile() {
        Log.i(TAG, "onProfile");
        Intent intent = new Intent(context, UserProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void onSignOut() {
        Log.i(TAG, "onSignOut");
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Sign Out");
        alert.setMessage("Are you sure");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Signing Out",Toast.LENGTH_LONG);
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();
                }
                UserDb.clearData();
//                Notifications.clearData();
                ItemDb.clearData();

                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.finish();
                context.startActivity(intent);
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // close dialog
                dialog.cancel();
            }
        });
        alert.show();
    }

    public void myList() {
        Log.i(TAG, "My List");
        Intent intent = new Intent(context, MyListings.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}

//To call the listener
// navigationView.setNavigationItemSelectedListener(new navigationListener(getApplicationContext()));
//remember to pass the context
