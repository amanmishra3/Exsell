package com.android.exsell.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.exsell.R;
import com.android.exsell.db.ItemDb;
import com.android.exsell.db.UserDb;
import com.android.exsell.fragments.FragmentSearchBar;
import com.android.exsell.fragments.FragmentTopBar;
import com.android.exsell.listeners.BasicOnClickListeners;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemListing extends AppCompatActivity implements FragmentTopBar.navbarHamburgerOnClickCallback, FragmentSearchBar.SearchBarOnSearch {
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;
    private View parent;
    private UserDb userDb;
    private Map<String, Object> product;
    private ImageView search, wishlist, addListing, message, productImage, addToWishlist,notification;
    private TextView title, description, price, tags;
    private String TAG = "ItemListing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_item_listing);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameTopBar, new FragmentTopBar());
        fragmentTransaction.commit();
        product = ItemDb.selectedProduct;
        userDb = UserDb.newInstance();
        // side navigation
        //-->
        parent  = (View) findViewById(R.id.activity_item_listing_inner_constraint);
        productImage = (ImageView) parent.findViewById(R.id.image);
        title = (TextView) parent.findViewById(R.id.title);
        price = (TextView) parent.findViewById(R.id.price);
        description = (TextView) parent.findViewById(R.id.description);
        tags = (TextView) parent.findViewById(R.id.tags);
        addToWishlist = (ImageView) parent.findViewById(R.id.add_to_wishlist);
        // <--

        layoutBottom = findViewById(R.id.layoutBottomBar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayoutItem);
        navigationView = findViewById(R.id.navigationMenuItem);
        navigationView.setNavigationItemSelectedListener(new navigationListener(getApplicationContext()));

        wishlist = (ImageView) layoutBottom.findViewById(R.id.wishlistButton);
        wishlist.setOnClickListener(new TopBottomNavigationListener(R.id.wishlistButton, getApplicationContext()));
        addListing = (ImageView) layoutBottom.findViewById(R.id.addItemButton);
        addListing.setOnClickListener(new TopBottomNavigationListener(R.id.addItemButton, getApplicationContext()));
        message = (ImageView) findViewById(R.id.chatButton);
        message.setOnClickListener(new TopBottomNavigationListener(R.id.chatButton, getApplicationContext()));

        //-->getting from static stuff for now
        if(product.containsKey("imageUri") && product.get("imageUri") != null) {
            Picasso.get().load((String)product.get("imageUri")).into(productImage);
        } else {
            productImage.setImageResource(R.drawable.tanmay);
        }
        title.setText((String)product.get("title"));
        price.setText(product.get("price").toString());
        description.setText((String)product.get("description"));
        tags.setText("");
        tags.setText(((List<String>)(product.get("tags"))).toString());

        checkWishList();

        addToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToWishList();
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        drawer.closeDrawer(Gravity.LEFT, false);
    }
    public void contactSeller(View view){
        Toast.makeText(this, "Contacting seller",Toast.LENGTH_SHORT).show();
    }

    public void buyNow(View view){
        Toast.makeText(this, "Buy now",Toast.LENGTH_SHORT).show();
    }

    public void addToWishList() {
        List<String> myWishlist = (List<String>)UserDb.myUser.get("wishlist");
        if(myWishlist == null) {
            myWishlist = new ArrayList<>();
        }
        if(myWishlist.contains((String)product.get("productId")))  {
            myWishlist.remove((String)product.get("productId"));
            userDb.removeFromWishList((String)UserDb.myUser.get("userId"),(String)product.get("productId"));
            addToWishlist.setImageResource(R.drawable.ic_heart_white);
        } else {
            myWishlist.add((String)product.get("productId"));
            userDb.addToWishList((String)UserDb.myUser.get("userId"),(String)product.get("productId"));
            addToWishlist.setImageResource(R.drawable.ic_heart_yellow);
        }
    }
    public void checkWishList() {
        List<String> myWishlist = (List<String>)UserDb.myUser.get("wishlist");
        if(myWishlist == null || !myWishlist.contains((String)product.get("productId"))) {
            addToWishlist.setImageResource(R.drawable.ic_heart_grey);
        } else {
            addToWishlist.setImageResource(R.drawable.ic_heart_yellow);
        }
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