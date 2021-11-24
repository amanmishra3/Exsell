package com.android.exsell.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.exsell.R;
import com.google.android.material.navigation.NavigationView;

public class ItemListing extends AppCompatActivity {
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_item_listing);

        // side navigation
        layoutTop = findViewById(R.id.layoutTopBar);
        layoutBottom = findViewById(R.id.layoutBottomBar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayoutItem);
        navigationView = findViewById(R.id.navigationMenuItem);

        layoutTop.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ItemListing.this, SearchBar.class));
            }
        });
        layoutBottom.findViewById(R.id.wishlistButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ItemListing.this, WishlistActivity.class));
            }
        });
        layoutTop.findViewById(R.id.leftNavigationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    public void contactSeller(View view){
//        Intent intent = new Intent(this, Home.class);
//        intent.putExtra("sellerID", 0);
//        startActivity(intent);
        Toast.makeText(this, "Contacting seller",Toast.LENGTH_SHORT).show();
    }

    public void buyNow(View view){
//        Intent intent = new Intent(this, Home.class);
//        intent.putExtra("sellerID", 0);
//        startActivity(intent);
        Toast.makeText(this, "Buy now",Toast.LENGTH_SHORT).show();
    }
}