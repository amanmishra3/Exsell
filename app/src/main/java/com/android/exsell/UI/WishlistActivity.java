package com.android.exsell.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.android.exsell.R;
import com.android.exsell.adapters.WishlistAdapter;
import com.android.exsell.models.Wishlist;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {
    LinearLayout layoutTop;
    DrawerLayout drawer;
    NavigationView navigationView;
    public static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView wishlistRecycler;
    private static ArrayList<Wishlist> wishlistItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        layoutTop = findViewById(R.id.layoutTopBar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationMenu);

        layoutTop.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WishlistActivity.this, SearchBar.class));
            }
        });

        layoutTop.findViewById(R.id.leftNavigationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        loadProducts();
        wishlistRecycler = findViewById(R.id.recyclerViewWishlistTiles);
        loadRecycler(wishlistRecycler, wishlistItems);
        adapter = new WishlistAdapter(wishlistItems);
        wishlistRecycler.setAdapter(adapter);
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
        // create and set adapter
        adapter = new WishlistAdapter(cat);
        thisRecycler.setAdapter(adapter);
    }
}