package com.android.exsell.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;
import com.android.exsell.adapters.MylistAdapter;
import com.android.exsell.adapters.ProductAdapter;
import com.android.exsell.db.ItemDb;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Product;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MyListings extends AppCompatActivity {
    private String TAG = "Home";
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;
    public static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView newlyListedRecycler, recommendedRecycler;
    private static ArrayList<Product> newProducts, recommendedProducts;
    private Object List;
    private ItemDb itemDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemDb = ItemDb.newInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_my_listings);

        // side navigation
        layoutTop = findViewById(R.id.layoutTopBar);
        layoutBottom = findViewById(R.id.layoutBottomBar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayoutHome);

        navigationView = findViewById(R.id.navigationMenuHome);

        layoutTop.findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyListings.this, SearchBar.class));
            }
        });

        layoutTop.findViewById(R.id.leftNavigationButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);

            }
        });
        loadProducts();
        itemDb.getAllItems(new ItemDb.getItemsCallback() {
            @Override
            public void onCallback(java.util.List<Product> itemsList) {
                if(itemsList == null || itemsList.size() == 0) {

                } else  {
                    // add cards to recyclers
                    newlyListedRecycler = findViewById(R.id.recyclerViewMyTiles);
                    loadRecycler(newlyListedRecycler, itemsList); // loads fake products into arraylists for recyclers
                }
            }
        });

        // add category images to linear layout

    }

    // create fake products (could adapt to work with database)
    public void loadProducts(){
        List<String> fakeTags = new ArrayList<>();
        fakeTags.add("CEO");
        fakeTags.add("Gaand");
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
        adapter = new MylistAdapter(products);
        thisRecycler.setAdapter(adapter);
    }
}