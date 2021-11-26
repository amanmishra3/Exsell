package com.android.exsell.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.exsell.R;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Category;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewListing extends AppCompatActivity {
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawerlist;
    NavigationView navigationView;

    private ImageView search, wishlist, addListing, message, addImage;
    private Button addItem;
    private TextView title, description, tags, price;
    private static final int galleryPick = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_new_listing);
        layoutTop = findViewById(R.id.layoutTopBar);
        layoutBottom = findViewById(R.id.layoutBottomBar);
        drawerlist = (DrawerLayout) findViewById(R.id.drawerLayoutItem);
        navigationView = findViewById(R.id.navigationMenuItem);
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
                drawerlist.openDrawer(GravityCompat.START);

            }
        });
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        tags = (TextView) findViewById(R.id.tags);
        price = (TextView) findViewById(R.id.price);
        addImage = (ImageView) findViewById(R.id.imageAdd);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        addItem = (Button) findViewById(R.id.addListing);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewItem();
            }
        });
        //dummy items in the category
        String[] arraySpinner = new String[] {
                "Select an item", "Books", "Electronics", "Furniture", "Sports", "Stationery"
        };
        Spinner s = (Spinner) findViewById(R.id.category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    private void openGallery() {
        Intent intentGallery = new Intent();
        intentGallery.setAction(Intent.ACTION_GET_CONTENT);
        intentGallery.setType("image/*");
        startActivityForResult(intentGallery, galleryPick);
    }

    private void addNewItem() {
        String itemTitle = title.getText().toString();
        String itemDescription = description.getText().toString();
        String sTags = tags.getText().toString();
        List<String> itemTags= Arrays.asList(sTags.split(","));
        int itemPrice = Integer.parseInt(price.getText().toString());
        String itemSeller = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawerlist.closeDrawer(Gravity.LEFT, false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        title.setText("");
        description.setText("");
        tags.setText("");
        price.setText("");
    }
}