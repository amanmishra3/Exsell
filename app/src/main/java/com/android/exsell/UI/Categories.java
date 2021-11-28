package com.android.exsell.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.exsell.R;
import com.android.exsell.adapters.CategoryAdapter;
import com.android.exsell.fragments.FragmentSearchBar;
import com.android.exsell.fragments.FragmentTopBar;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Category;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class Categories extends AppCompatActivity implements FragmentTopBar.navbarHamburgerOnClickCallback, FragmentSearchBar.SearchBarOnSearch {

    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;
    private ImageView search, wishlist, addListing, message, notification;
    public static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView categoryRecycler;
    private static ArrayList<Category> categoryNames;
    private String TAG = "Categories";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_categories);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameTopBar, new FragmentTopBar());
        fragmentTransaction.commit();

        layoutBottom = findViewById(R.id.layoutBottomBar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(new navigationListener(getApplicationContext()));

        wishlist = (ImageView) layoutBottom.findViewById(R.id.wishlistButton);
        wishlist.setOnClickListener(new TopBottomNavigationListener(R.id.wishlistButton, getApplicationContext()));
        addListing = (ImageView) layoutBottom.findViewById(R.id.addItemButton);
        addListing.setOnClickListener(new TopBottomNavigationListener(R.id.addItemButton, getApplicationContext()));
        message = (ImageView) findViewById(R.id.chatButton);
        message.setOnClickListener(new TopBottomNavigationListener(R.id.chatButton, getApplicationContext()));

        loadProducts();
        categoryRecycler = (RecyclerView) findViewById(R.id.recyclerViewCategoryTiles);
        categoryRecycler.setNestedScrollingEnabled(false);
        loadRecycler(categoryRecycler, categoryNames);
        adapter = new CategoryAdapter(categoryNames);
        categoryRecycler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawer.closeDrawer(Gravity.LEFT, false);
    }

    public void loadProducts() {
        String[] fakeTags = {"Textbooks", "COEN"};
        Category catergory1 = new Category(1, "Category 1", R.drawable.test_image);
        Category catergory2 = new Category(2, "Category 2", R.drawable.test_image);
        Category catergory3 = new Category(3, "Category 3", R.drawable.test_image);
        Category catergory4 = new Category(4, "Category 4", R.drawable.test_image);
        // add to arraylists
        categoryNames = new ArrayList<Category>();
        categoryNames.add(catergory1);
        categoryNames.add(catergory2);
        categoryNames.add(catergory3);
        categoryNames.add(catergory4);
    }

    public void loadRecycler(RecyclerView thisRecycler, ArrayList<Category> cat){
        layoutManager = new GridLayoutManager(this, 2);
        thisRecycler.setHasFixedSize(true); // set has fixed size
        thisRecycler.setLayoutManager(layoutManager); // set layout manager
        // create and set adapter
        adapter = new CategoryAdapter(cat);
        thisRecycler.setAdapter(adapter);
    }

    public void itemDetails(View v){
        Intent intent = new Intent(getApplicationContext(), ItemListing.class);
        // pass data about which product is clicked
        startActivity(intent);
    }

    @Override
    public void onHamburgerClickCallback() {
        Log.i(TAG,"onHamburgerClickCallback");
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void onSearch(String search) {
        Log.i(TAG,"onSearch received "+search);
    }

}