package com.android.exsell.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.exsell.R;
import com.android.exsell.cloudStorage.MyFirebaseStorage;
import com.android.exsell.db.ItemDb;
import com.android.exsell.db.UserDb;
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
    private UserDb userDb;
    Animation anim1, anim2;
    ImageView exsell1, exsell2;
    public static int SPLASH_SCREEN = 5000;
//    private MyFirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        storage = MyFirebaseStorage.newInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        anim1 = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        exsell1 = (ImageView) findViewById(R.id.exsell1);
        exsell2 = (ImageView) findViewById(R.id.exsell2);
//        exsell1.setAnimation(anim2);
        exsell2.setAnimation(anim1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                Intent intent;
                if (currentUser != null) {
                    userDb = UserDb.newInstance();
                    userDb.setMyUser();
                    intent = new Intent(MainActivity.this, Home.class);
                } else {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);

    }


    @Override
    protected void onStart() {
        super.onStart();

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