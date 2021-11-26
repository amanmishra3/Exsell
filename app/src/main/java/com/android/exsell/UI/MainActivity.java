package com.android.exsell.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.android.exsell.R;
import com.android.exsell.cloudStorage.MyFirebaseStorage;
import com.android.exsell.db.ItemDb;
import com.android.exsell.models.Product;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private FirebaseAuth mAuth;
    private String TAG = "Main";
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;
    private ItemDb itemDb;
//    private MyFirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        storage = MyFirebaseStorage.newInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        layoutTop = findViewById(R.id.layoutTopBar);
        layoutBottom = findViewById(R.id.layoutBottomBar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationMenu);

        layoutTop.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchBar.class));
            }
        });
        layoutBottom.findViewById(R.id.wishlistButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WishlistActivity.class));
            }
        });
        layoutTop.findViewById(R.id.leftNavigationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        if(navigationView != null){
            navigationView.setNavigationItemSelectedListener(this);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Intent intent;
        if (currentUser != null) {
            intent = new Intent(this, Home.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
    }

    // temporary button
    public void move(View view){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.edit) {
            // DO your stuff
            startActivity(new Intent(MainActivity.this, ViewListing.class));

        }
        return false;
    }

}