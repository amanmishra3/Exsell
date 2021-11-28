package com.android.exsell.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.exsell.R;
import com.android.exsell.adapters.HorizontalProductAdapter;
import com.android.exsell.chat.MessagePreviews;
import com.android.exsell.cloudStorage.MyFirebaseStorage;
import com.android.exsell.db.ItemDb;
import com.android.exsell.fragments.FragmentSearchBar;
import com.android.exsell.fragments.FragmentTopBar;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Product;
import com.android.exsell.adapters.ProductAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity implements FragmentTopBar.navbarHamburgerOnClickCallback, FragmentSearchBar.SearchBarOnSearch {
    // side navigation
    private String TAG = "Home";
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;

    // categories
    LinearLayout ll;
    int[] categoryImages = {R.drawable.ic_category_all,R.drawable.ic_category_textbooks, R.drawable.ic_category_clothes, R.drawable.ic_category_furniture, R.drawable.ic_category_electronics, R.drawable.ic_category_sports, R.drawable.ic_category_more};
    int[] categoryIDs = {R.id.category0,R.id.category1, R.id.category2, R.id.category3, R.id.category4, R.id.category5, R.id.category6};

    // card recyclers
    public static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView newlyListedRecycler, recommendedRecycler;
    private static ArrayList<Product> newProducts, recommendedProducts;
    private Object List;
    private ItemDb itemDb;
    private MyFirebaseStorage myStorage;
    private HorizontalScrollView view;
    private ConstraintLayout constraintLayout;
    private ImageView search, wishlist, addListing, message, notification;
    private FirebaseAuth mAuth;

    private int noteClickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemDb = ItemDb.newInstance();
        mAuth = FirebaseAuth.getInstance();
        myStorage = new MyFirebaseStorage();
        Product p = new Product();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameTopBar, new FragmentTopBar());
        fragmentTransaction.commit();

        // side navigation
        layoutBottom = (LinearLayout) findViewById(R.id.layoutBottomBar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayoutHome);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_home);
        view = (HorizontalScrollView) constraintLayout.findViewById(R.id.horizontal_scroll_categories);
        view.setHorizontalScrollBarEnabled(false);
        view.setVerticalScrollBarEnabled(false);
        navigationView = findViewById(R.id.navigationMenuHome);
        navigationView.setNavigationItemSelectedListener(new navigationListener(getApplicationContext()));

        wishlist = (ImageView) layoutBottom.findViewById(R.id.wishlistButton);
        wishlist.setOnClickListener(new TopBottomNavigationListener(R.id.wishlistButton, getApplicationContext()));
        addListing = (ImageView) layoutBottom.findViewById(R.id.addItemButton);
        addListing.setOnClickListener(new TopBottomNavigationListener(R.id.addItemButton, getApplicationContext()));
        message = (ImageView) findViewById(R.id.chatButton);
        message.setOnClickListener(new TopBottomNavigationListener(R.id.chatButton, getApplicationContext()));

        loadProducts();
        itemDb.getAllItems(new ItemDb.getItemsCallback() {
            @Override
            public void onCallback(java.util.List<Product> itemsList) {
                if (itemsList == null || itemsList.size() == 0) {

                } else {
                    // add cards to recyclers
                    newlyListedRecycler = (RecyclerView) findViewById(R.id.new_recycler);
                    newlyListedRecycler.setNestedScrollingEnabled(false);
                    loadRecycler(newlyListedRecycler, itemsList, itemsList.size());
                }
            }
        });


        recommendedRecycler = (RecyclerView) findViewById(R.id.recommended_recycler);
        recommendedRecycler.setNestedScrollingEnabled(false);
        loadRecyclerHorizontal(recommendedRecycler, recommendedProducts, 1);

        // add category images to linear layout
        ll = (LinearLayout) findViewById(R.id.linear);
        loadCategoryImages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawer.closeDrawer(Gravity.LEFT, false);
    }

    public void onCardClicked(Product product, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), ItemListing.class);
        intent.putExtra("isBuyer", true);
        intent.putExtra("productId", product.getProductId());
        startActivity(intent);
    }

    // create fake products (could adapt to work with database)
    public void loadProducts() {
        List<String> fakeTags = new ArrayList<>();
        fakeTags.add("CEO");
        fakeTags.add("Data");
        Product product1 = new Product("1", "Product 1", 8, R.drawable.test_image, fakeTags);
        Product product2 = new Product("2", "Product 2", 2, R.drawable.test_image, fakeTags);
        Product product3 = new Product("3", "Product 3", 10, R.drawable.test_image, fakeTags);
        Product product4 = new Product("4", "Product 4", 25, R.drawable.test_image, fakeTags);
        Product product5 = new Product("5", "Product 5", 12, R.drawable.test_image, fakeTags);

        // add to arraylists
        newProducts = new ArrayList<Product>();
        newProducts.add(product1);
        newProducts.add(product2);
        newProducts.add(product3);
        newProducts.add(product4);
        newProducts.add(product5);

        recommendedProducts = new ArrayList<Product>();
        recommendedProducts.add(product1);
        recommendedProducts.add(product2);
        recommendedProducts.add(product3);
        recommendedProducts.add(product4);
    }

    // recycler setup
    public void loadRecycler(RecyclerView thisRecycler, List<Product> products, int columns) {
        layoutManager = new GridLayoutManager(this, columns);
        thisRecycler.setHasFixedSize(true); // set has fixed size
        thisRecycler.setLayoutManager(layoutManager); // set layout manager

        // create and set adapter
        adapter = new ProductAdapter(products, this);
        thisRecycler.setAdapter(adapter);
    }

    // recycler setup
    public void loadRecyclerHorizontal(RecyclerView thisRecycler, List<Product> products, int columns) {
        layoutManager = new GridLayoutManager(this, columns);
        thisRecycler.setHasFixedSize(true); // set has fixed size
        thisRecycler.setLayoutManager(layoutManager); // set layout manager

        // create and set adapter
        adapter = new HorizontalProductAdapter(products, this);
        thisRecycler.setAdapter(adapter);
    }

    // category images
    public void loadCategoryImages() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        layoutParams.setMargins(10, 0, 10, 0);
        for (int i = 0; i < categoryImages.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(categoryIDs[i]);
            imageView.setImageResource(categoryImages[i]);
            imageView.setLayoutParams(layoutParams);
            imageView.setOnClickListener((v) -> {
                moveToCategory(v);
            });
            ll.addView(imageView);
        }
    }

    // onclick handler for categories
    // as of now, just creates a Toast
    public void moveToCategory(View view) {
        int id = view.getId();
        String category = "";
        switch (id) {
            case R.id.category0:
                category = "All";
                break;
            case R.id.category1:
                category = "Textbooks";
                break;
            case R.id.category2:
                category = "Clothes";
                break;
            case R.id.category3:
                category = "Furniture";
                break;
            case R.id.category4:
                category = "Electronics";
                break;
            case R.id.category5:
                category = "Sports";
                break;
            default:
                category = "More";
                startActivity(new Intent(Home.this, Categories.class));
                break;
        }
        categorySelected(category);
        // instead of toast, go to correct category activity
    }

    // onclick handler for cards
    // as of now, just goes to item-listing activity
    public void itemDetails(View v) {
        Intent intent = new Intent(getApplicationContext(), ItemListing.class);
        // pass data about which product is clicked
        startActivity(intent);
    }

    public void categorySelected(String category) {
        Toast.makeText(this, "Category " + category, Toast.LENGTH_SHORT).show();
        Product searchParam = new Product();
        if(category != "All")
            searchParam.setCategories(Arrays.asList(category.toLowerCase(),category));
        itemDb.searchItems(searchParam, new ItemDb.getItemsCallback() {
            @Override
            public void onCallback(java.util.List<Product> itemsList) {
                if (itemsList == null || itemsList.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No item Found " + category, Toast.LENGTH_SHORT).show();
                    loadRecyclerHorizontal(recommendedRecycler, itemsList, 1);

                } else {
                    // add cards to recyclers
                    Toast.makeText(getApplicationContext(), "Loading..  " + category +" " + itemsList.size(), Toast.LENGTH_LONG).show();
                    loadRecyclerHorizontal(recommendedRecycler, itemsList, 1);
                }
            }
        });
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

    public void setNavigationHeader() {
    }
}