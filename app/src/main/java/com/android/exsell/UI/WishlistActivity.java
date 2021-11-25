package com.android.exsell.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.exsell.R;
import com.android.exsell.adapters.WishlistAdapter;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Wishlist;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;
    public static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView wishlistRecycler;
    private static ArrayList<Wishlist> wishlistItems;
    private ImageView search, wishlist, addListing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wishlist);
        layoutTop = findViewById(R.id.layoutTopBar);
        layoutBottom = findViewById(R.id.layoutBottomBar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(new navigationListener(getApplicationContext()));
        search = (ImageView) layoutTop.findViewById(R.id.searchButton);
        search.setOnClickListener(new TopBottomNavigationListener(R.id.searchButton, getApplicationContext()));
        wishlist = (ImageView) layoutBottom.findViewById(R.id.wishlistButton);
        wishlist.setOnClickListener(new TopBottomNavigationListener(R.id.wishlistButton, getApplicationContext()));
        addListing = (ImageView) layoutBottom.findViewById(R.id.addItemButton);
        addListing.setOnClickListener(new TopBottomNavigationListener(R.id.addItemButton, getApplicationContext()));
        layoutTop.findViewById(R.id.leftNavigationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        loadProducts();
        wishlistRecycler = (RecyclerView) findViewById(R.id.recyclerViewWishlistTiles);
        wishlistRecycler.setNestedScrollingEnabled(false);
        loadRecycler(wishlistRecycler, wishlistItems);
        adapter = new WishlistAdapter(wishlistItems);
        wishlistRecycler.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        drawer.closeDrawer(Gravity.LEFT, false);
    }
    public void loadProducts() {
        String[] fakeTags = {"Textbooks", "COEN"};
        Wishlist product1 = new Wishlist(1,"Product 1", 8, R.drawable.test_image, fakeTags);
        Wishlist product2 = new Wishlist(2,"Product 2", 2, R.drawable.test_image, fakeTags);
        Wishlist product3 = new Wishlist(3,"Product 3", 10, R.drawable.test_image, fakeTags);
        Wishlist product4 = new Wishlist(4,"Product 4", 25, R.drawable.test_image, fakeTags);
        // add to arraylists
        wishlistItems = new ArrayList<Wishlist>();
        wishlistItems.add(product1);
        wishlistItems.add(product2);
        wishlistItems.add(product3);
        wishlistItems.add(product4);

    }
    public void loadRecycler(RecyclerView thisRecycler, ArrayList<Wishlist> cat){
        layoutManager = new GridLayoutManager(this, 2);
        thisRecycler.setHasFixedSize(true); // set has fixed size
        thisRecycler.setLayoutManager(layoutManager); // set layout manager
        thisRecycler.setPadding(100,0,0,0);
        // create and set adapter
        adapter = new WishlistAdapter(cat);
        thisRecycler.setAdapter(adapter);
    }
    public void itemDetails(View v){
        Intent intent = new Intent(getApplicationContext(), ItemListing.class);
        // pass data about which product is clicked
        startActivity(intent);
    }
}