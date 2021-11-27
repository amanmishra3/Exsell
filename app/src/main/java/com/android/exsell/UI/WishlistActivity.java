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
import android.widget.TextView;

import com.android.exsell.R;
import com.android.exsell.adapters.WishlistAdapter;
import com.android.exsell.db.ItemDb;
import com.android.exsell.db.UserDb;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Product;
import com.android.exsell.models.Wishlist;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;
    public static RecyclerView.Adapter adapter;
    private ItemDb itemDb;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView wishlistRecycler;
    private static ArrayList<Wishlist> wishlistItems;
    private ImageView search, wishlist, addListing, message;
    private TextView noitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wishlist);
        itemDb = ItemDb.newInstance();
        noitem = (TextView) findViewById(R.id.noitem_text);
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
        message = (ImageView) findViewById(R.id.chatButton);
        message.setOnClickListener(new TopBottomNavigationListener(R.id.chatButton, getApplicationContext()));
        layoutTop.findViewById(R.id.leftNavigationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        loadProducts();
        List<String> myWishList = (List<String>)UserDb.myUser.get("wishlist");
        if(myWishList == null || myWishList.size() <= 0) {
            noitem.setVisibility(View.VISIBLE);
        } else {
            itemDb.getItemsFromWishList(myWishList, new ItemDb.getItemsCallback() {
                @Override
                public void onCallback(List<Product> itemsList) {
                    if(itemsList == null || itemsList.size() <= 0) {
                        noitem.setVisibility(View.VISIBLE);
                    } else {
                        noitem.setVisibility(View.INVISIBLE);
                        wishlistRecycler = (RecyclerView) findViewById(R.id.recyclerViewWishlistTiles);
                        wishlistRecycler.setNestedScrollingEnabled(false);
                        loadRecycler(wishlistRecycler, itemsList);
                        adapter = new WishlistAdapter(itemsList, getApplicationContext());
                        wishlistRecycler.setAdapter(adapter);
                    }
                }
            });
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        drawer.closeDrawer(Gravity.LEFT, false);
        List<String> myWishList = (List<String>)UserDb.myUser.get("wishlist");
        if(myWishList == null || myWishList.size() <= 0) {
            noitem.setVisibility(View.VISIBLE);
        } else {
            itemDb.getItemsFromWishList(myWishList, new ItemDb.getItemsCallback() {
                @Override
                public void onCallback(List<Product> itemsList) {
                    if(itemsList == null || itemsList.size() <= 0) {
                        noitem.setVisibility(View.VISIBLE);
                    } else {
                        noitem.setVisibility(View.INVISIBLE);
                        wishlistRecycler = (RecyclerView) findViewById(R.id.recyclerViewWishlistTiles);
                        wishlistRecycler.setNestedScrollingEnabled(false);
                        loadRecycler(wishlistRecycler, itemsList);
                        adapter = new WishlistAdapter(itemsList, getApplicationContext());
                        wishlistRecycler.setAdapter(adapter);
                    }
                }
            });
        }
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
    public void loadRecycler(RecyclerView thisRecycler, List<Product> items){
        layoutManager = new GridLayoutManager(this, 2);
        thisRecycler.setHasFixedSize(true); // set has fixed size
        thisRecycler.setLayoutManager(layoutManager); // set layout manager
        // create and set adapter
        adapter = new WishlistAdapter(items,this);
        thisRecycler.setAdapter(adapter);
    }
    public void itemDetails(View v){
        Intent intent = new Intent(getApplicationContext(), ItemListing.class);
        // pass data about which product is clicked
        startActivity(intent);
    }
}