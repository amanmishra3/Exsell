package com.android.exsell.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;
import com.android.exsell.adapters.HorizontalProductAdapter;
import com.android.exsell.adapters.MylistAdapter;
import com.android.exsell.adapters.NotificationAdapter;
import com.android.exsell.adapters.ProductAdapter;
import com.android.exsell.db.ItemDb;
import com.android.exsell.fragments.FragmentSearchBar;
import com.android.exsell.fragments.FragmentTopBar;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Notifications;
import com.android.exsell.models.Product;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyListings extends AppCompatActivity implements FragmentTopBar.navbarHamburgerOnClickCallback, FragmentSearchBar.SearchBarOnSearch, FragmentTopBar.NotificationBellClickCallback, FragmentSearchBar.SearchBarBack {
    private String TAG = "My Listings";
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;
    public static RecyclerView.Adapter adapter;

    RecyclerView notificationRecycler;
    private FirebaseAuth mAuth;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView newlyListedRecycler, recommendedRecycler;
    private static ArrayList<Product> newProducts, recommendedProducts;
    private Object List;
    private ItemDb itemDb;
    private ImageView search, wishlist, addListing, message, notification;
    private TextView noitem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemDb = ItemDb.newInstance();
        mAuth = FirebaseAuth.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_listings);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameTopBar, new FragmentTopBar());
        fragmentTransaction.commit();

        noitem = (TextView) findViewById(R.id.noitem_mylisting);

        // side navigation
        layoutBottom = findViewById(R.id.layoutBottomBar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayoutListing);
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

        loadProducts();

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
        navigationView.setCheckedItem(R.id.edit);
//        drawer.closeDrawer(GravityCompat.END, false);
        drawer.closeDrawer(GravityCompat.START, false);
        Product searchParam = new Product();
        if(mAuth.getCurrentUser() != null)
            searchParam.setSeller(mAuth.getCurrentUser().getUid());
        itemDb.searchItems(searchParam, new ItemDb.getItemsCallback() {
            @Override
            public void onCallback(java.util.List<Product> itemsList) {
                if(itemsList == null || itemsList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "You Have 0 items Listed for Sale please add some", Toast.LENGTH_LONG).show();
                    noitem.setVisibility(View.VISIBLE);
                    newlyListedRecycler = (RecyclerView) findViewById(R.id.recyclerViewMyTiles);
                    loadRecycler(newlyListedRecycler, null);
                } else  {
                    noitem.setVisibility(View.INVISIBLE);
                    // add cards to recyclers
                    newlyListedRecycler = (RecyclerView) findViewById(R.id.recyclerViewMyTiles);
                    newlyListedRecycler.setNestedScrollingEnabled(false);
                    loadRecycler(newlyListedRecycler, itemsList); // loads fake products into arraylists for recyclers
                }
            }
        });



    }
    @Override
    protected void onStart() {
        super.onStart();
        navigationView.setCheckedItem(R.id.edit);
    }
    // create fake products (could adapt to work with database)
    public void loadProducts(){
        List<String> fakeTags = new ArrayList<>();
        fakeTags.add("CEO");
        fakeTags.add("Data");
        Product product1 = new Product("1","Product 1", 8, R.drawable.test_image, fakeTags);
        Product product2 = new Product("2","Product 2", 2, R.drawable.test_image, fakeTags);
        Product product3 = new Product("3","Product 3", 10, R.drawable.test_image, fakeTags);
        Product product4 = new Product("4","Product 4", 25, R.drawable.test_image, fakeTags);

        // add to arraylists
        newProducts = new ArrayList<Product>();
        newProducts.add(product1);
        newProducts.add(product2);

        recommendedProducts = new ArrayList<Product>();
        recommendedProducts.add(product1);
        recommendedProducts.add(product2);
        recommendedProducts.add(product3);
        recommendedProducts.add(product4);
    }

    // recycler setup
    public void loadRecycler(RecyclerView thisRecycler, List<Product> products){
        layoutManager = new GridLayoutManager(this, 1);
        thisRecycler.setHasFixedSize(true); // set has fixed size
        thisRecycler.setLayoutManager(layoutManager); // set layout manager

        // create and set adapter
        adapter = new HorizontalProductAdapter(products, this);
        thisRecycler.setAdapter(adapter);
    }
    public void itemDetails(View v) {
        Intent intent = new Intent(getApplicationContext(), ItemListing.class);
        // pass data about which product is clicked
        startActivity(intent);
    }

    @Override
    public void onHamburgerClickCallback() {
        Log.i(TAG,"onHamburgerClickCallback");
        drawer.closeDrawer(GravityCompat.END, false);
        drawer.openDrawer(GravityCompat.START);
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
        //startActivity(new Intent(getApplicationContext(), Home.class));
    }

    @Override
    public void onSearchBack() {
        Log.i("onSearchBack", "searchBack");
    }
}