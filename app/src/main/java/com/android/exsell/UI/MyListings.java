package com.android.exsell.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;
import com.android.exsell.adapters.HorizontalProductAdapter;
import com.android.exsell.adapters.MylistAdapter;
import com.android.exsell.adapters.ProductAdapter;
import com.android.exsell.db.ItemDb;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Product;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MyListings extends AppCompatActivity{
    private String TAG = "Home";
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;
    public static RecyclerView.Adapter adapter;
    private FirebaseAuth mAuth;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView newlyListedRecycler, recommendedRecycler;
    private static ArrayList<Product> newProducts, recommendedProducts;
    private Object List;
    private ItemDb itemDb;
    private ImageView search, wishlist, addListing, message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemDb = ItemDb.newInstance();
        mAuth = FirebaseAuth.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_my_listings);

        // side navigation
        layoutTop = findViewById(R.id.layoutTopBar);
        layoutBottom = findViewById(R.id.layoutBottomBar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayoutListing);
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
        Product searchParam = new Product();
        if(mAuth.getCurrentUser() != null)
            searchParam.setSeller(mAuth.getCurrentUser().getUid());
        itemDb.searchItems(searchParam, new ItemDb.getItemsCallback() {
            @Override
            public void onCallback(java.util.List<Product> itemsList) {
                if(itemsList == null || itemsList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "You Have 0 items Listed for Sale please add some", Toast.LENGTH_LONG).show();
                } else  {
                    // add cards to recyclers
                    newlyListedRecycler = (RecyclerView) findViewById(R.id.recyclerViewMyTiles);
                    newlyListedRecycler.setNestedScrollingEnabled(false);
                    loadRecycler(newlyListedRecycler, itemsList); // loads fake products into arraylists for recyclers
                }
            }
        });



    }
    @Override
    protected void onResume() {
        super.onResume();
        drawer.closeDrawer(Gravity.LEFT, false);
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
}