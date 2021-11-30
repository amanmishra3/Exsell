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
import android.widget.TextView;

import com.android.exsell.R;
import com.android.exsell.adapters.NotificationAdapter;
import com.android.exsell.adapters.WishlistAdapter;
import com.android.exsell.db.ItemDb;
import com.android.exsell.db.UserDb;
import com.android.exsell.fragments.FragmentSearchBar;
import com.android.exsell.fragments.FragmentTopBar;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Notifications;
import com.android.exsell.models.Product;
import com.android.exsell.models.Wishlist;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends AppCompatActivity implements FragmentTopBar.navbarHamburgerOnClickCallback, FragmentSearchBar.SearchBarOnSearch, FragmentTopBar.NotificationBellClickCallback, FragmentSearchBar.SearchBarBack {
    LinearLayout layoutTop, layoutBottom;
    private String TAG = "Wishlist";
    DrawerLayout drawer;
    NavigationView navigationView;
    public static RecyclerView.Adapter adapter;
    RecyclerView notificationRecycler;
    private ItemDb itemDb;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView wishlistRecycler;
    private static ArrayList<Product> wishlistItems;
    private ImageView search, wishlist, addListing, message, notification, wishlistIcon, profilePic;
    private TextView noitem, userName, userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wishlist);
        itemDb = ItemDb.newInstance();
        noitem = (TextView) findViewById(R.id.noitem_text);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameTopBar, new FragmentTopBar());
        fragmentTransaction.commit();

        layoutBottom = findViewById(R.id.layoutBottomBar);
        wishlistIcon = layoutBottom.findViewById(R.id.wishlistButton);
        wishlistIcon.setImageResource(R.drawable.ic_heart2);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(new navigationListener(getApplicationContext()));

        wishlist = (ImageView) layoutBottom.findViewById(R.id.wishlistButton);
        wishlist.setOnClickListener(new TopBottomNavigationListener(R.id.wishlistButton, getApplicationContext()));
        addListing = (ImageView) layoutBottom.findViewById(R.id.addItemButton);
        addListing.setOnClickListener(new TopBottomNavigationListener(R.id.addItemButton, getApplicationContext()));
        message = (ImageView) findViewById(R.id.chatButton);
        message.setOnClickListener(new TopBottomNavigationListener(R.id.chatButton, getApplicationContext()));

        notificationRecycler = (RecyclerView) findViewById(R.id.right_drawer);
        notificationRecycler.setNestedScrollingEnabled(true);
        loadNotificationsRecycler(notificationRecycler, Notifications.getMyNotifications(), 1);

        List<String> myWishList = new ArrayList<>();
        if(FirebaseAuth.getInstance().getCurrentUser() != null && UserDb.myUser != null)
            myWishList = (List<String>)UserDb.myUser.get("wishlist");
        if(myWishList == null || myWishList.size() <= 0) {
            noitem.setVisibility(View.VISIBLE);
            wishlistRecycler = (RecyclerView) findViewById(R.id.recyclerViewWishlistTiles);
            loadRecycler(wishlistRecycler, null);
            wishlistRecycler.setAdapter(null);
        }
    }

    public void loadNotificationsRecycler(RecyclerView thisRecycler, List<JSONObject> products, int columns) {
        layoutManager = new GridLayoutManager(this, columns);
        thisRecycler.setHasFixedSize(true); // set has fixed size
        thisRecycler.setLayoutManager(layoutManager); // set layout manager

        // create and set adapter
        adapter = new NotificationAdapter(products, this);
        thisRecycler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawer.closeDrawer(Gravity.LEFT, false);
        List<String> myWishList = (List<String>)UserDb.myUser.get("wishlist");
        if(myWishList == null || myWishList.size() <= 0) {
            noitem.setVisibility(View.VISIBLE);
            wishlistRecycler = (RecyclerView) findViewById(R.id.recyclerViewWishlistTiles);
            loadRecycler(wishlistRecycler, null);
            wishlistRecycler.setAdapter(null);
        } else {
            itemDb.getItemsFromWishList(myWishList, new ItemDb.getItemsCallback() {
                @Override
                public void onCallback(List<Product> itemsList) {
                    Log.i("wishlist2"," name "+UserDb.myUser.get("name"));
                    if(itemsList == null || itemsList.size() <= 0) {
                        noitem.setVisibility(View.VISIBLE);

                    } else {
                        noitem.setVisibility(View.INVISIBLE);
                        wishlistRecycler = (RecyclerView) findViewById(R.id.recyclerViewWishlistTiles);
                        wishlistRecycler.setNestedScrollingEnabled(false);
                        wishlistItems = (ArrayList<Product>) itemsList;
                        loadRecycler(wishlistRecycler, itemsList);
                        adapter = new WishlistAdapter(itemsList, getApplicationContext());
                        wishlistRecycler.setAdapter(adapter);
                    }
                }
            });
        }
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

    @Override
    public void onHamburgerClickCallback() {
        Log.i(TAG,"onHamburgerClickCallback");
        drawer.closeDrawer(GravityCompat.END, false);
        drawer.openDrawer(GravityCompat.START);
        userName = (TextView) drawer.findViewById(R.id.userNameNav);
        userEmail = (TextView) drawer.findViewById(R.id.userEmailNav);
        profilePic = (ImageView) drawer.findViewById(R.id.profilePicNav);
        getUserDetails();
    }

    @Override
    public void onNotificationBellClick() {
        Log.i(TAG,"onNotificationBellClick");
        drawer.closeDrawer(GravityCompat.START, false);
        drawer.openDrawer(GravityCompat.END);
    }

    public void setNavigationHeader() {

    }
    @Override
    public void onSearch(String search) {
        Log.i(TAG,"onSearch received "+search);
        loadRecycler(wishlistRecycler, searchProducts(wishlistItems, search));
    }
    public List<Product> searchProducts(List<Product> recommendedProducts, String searchKeyword) {
        List<Product> searchResult;
        if(searchKeyword.trim().isEmpty()) {
            searchResult = recommendedProducts;
        } else {
            ArrayList<Product> temp = new ArrayList<>();
            for(Product product : recommendedProducts) {
                if(product.getTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                        || product.getDescription().toLowerCase().contains(searchKeyword.toLowerCase())) {
                    temp.add(product);
                }
            }
            searchResult = temp;
            Log.i("Size of temp products: ", String.valueOf(searchResult.size()));
        }
        return searchResult;
    }

    @Override
    public void onSearchBack() {
        Log.i("onSearchBack", "searchBack");
    }
    public void getUserDetails(){
        userName.setText((String) UserDb.myUser.get("name"));
        userEmail.setText((String) UserDb.myUser.get("email"));
        if(UserDb.myUser.containsKey("imageUri")) {
            Picasso.get().load((String)UserDb.myUser.get("imageUri")).into(profilePic);
        }
    }
}