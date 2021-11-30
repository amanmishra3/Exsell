package com.android.exsell.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;
import com.android.exsell.adapters.HorizontalProductAdapter;
import com.android.exsell.adapters.NotificationAdapter;
import com.android.exsell.adapters.ProductAdapter;
import com.android.exsell.cloudStorage.MyFirebaseStorage;
import com.android.exsell.db.AppSettingsDb;
import com.android.exsell.db.ItemDb;
import com.android.exsell.db.UserDb;
import com.android.exsell.fragments.FragmentSearchBar;
import com.android.exsell.fragments.FragmentTopBar;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Notifications;
import com.android.exsell.models.Product;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Home extends AppCompatActivity implements FragmentTopBar.navbarHamburgerOnClickCallback, FragmentSearchBar.SearchBarOnSearch, FragmentTopBar.NotificationBellClickCallback, FragmentSearchBar.SearchBarBack {
    // side navigation
    private String TAG = "Home";
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;
    HorizontalProductAdapter horizontalProductAdapterSearch;
    private long backPressedTime;
    private Toast backToast;
    // categories
    LinearLayout ll;
    int[] categoryImages = {R.drawable.ic_all,R.drawable.ic_category_textbooks, R.drawable.ic_category_clothes, R.drawable.ic_category_furniture, R.drawable.ic_category_electronics, R.drawable.ic_category_sports};
    int[] categoryIDs = {R.id.category0,R.id.category1, R.id.category2, R.id.category3, R.id.category4, R.id.category5, R.id.category6};

    // card recyclers
    public static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView newlyListedRecycler, recommendedRecycler, notificationRecycler;
    private static ArrayList<Product> newProducts, recommendedProducts;
    private Object List;
    private ItemDb itemDb;
    private UserDb userDb;
    private AppSettingsDb appSettingsDb;
    private MyFirebaseStorage myStorage;
    private HorizontalScrollView view;
    private ConstraintLayout constraintLayout;
    private ImageView search, wishlist, addListing, message, notification, profilePic, hamburger;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private int noteClickedPosition = -1;
    private TextView newHeader, recommendedHeader, userName, userEmail;
    private int flag= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemDb = ItemDb.newInstance();
        userDb = UserDb.newInstance();
        appSettingsDb = AppSettingsDb.newInstance();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            userDb.setMyUser();
            Notifications.updateNotifications();
        }
        myStorage = new MyFirebaseStorage();
        Product p = new Product();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameTopBar, new FragmentTopBar());
        fragmentTransaction.commit();

        // side navigation
        newHeader = (TextView) findViewById(R.id.new_header);
        recommendedHeader = (TextView) findViewById(R.id.recommended_header);
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
        categorySelected("All");

        itemDb.getAllItems(new ItemDb.getItemsCallback() {
            @Override
            public void onCallback(java.util.List<Product> itemsList) {
                if (itemsList == null || itemsList.size() == 0) {

                } else {
                    // add cards to recyclers
                    newlyListedRecycler = (RecyclerView) findViewById(R.id.new_recycler);
                    newlyListedRecycler.setNestedScrollingEnabled(true);
                    loadRecycler(newlyListedRecycler, itemsList, itemsList.size());
                }
            }
        });

        notificationRecycler = (RecyclerView) findViewById(R.id.right_drawer);
        notificationRecycler.setNestedScrollingEnabled(true);
        loadNotificationsRecycler(notificationRecycler, Notifications.getMyNotifications(), 1);

        recommendedRecycler = (RecyclerView) findViewById(R.id.recommended_recycler);
        recommendedRecycler.setNestedScrollingEnabled(true);
        loadRecyclerHorizontal(recommendedRecycler, recommendedProducts, 1);
        // add category images to linear layout
        ll = (LinearLayout) findViewById(R.id.linear);
        loadCategoryImages();
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.nav_open, R.string.nav_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (flag == 1){

                hamburger = (ImageView) findViewById(R.id.leftNavigationButton);
                hamburger.setImageResource(R.drawable.ic_left_navigation_menu_button);
                flag = 0;
                }
                else if(flag == 2){
                    notification = (ImageView) findViewById(R.id.notificationButton);
                    notification.setImageResource(R.drawable.ic_notifications);
                    flag = 0;
                }
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
                if(drawer.isDrawerOpen(GravityCompat.END)){
                notification = (ImageView) findViewById(R.id.notificationButton);
                notification.setImageResource(R.drawable.ic_notifications_black_24dp);
                onNotificationBellClick();
                flag = 2;
                }
                else if(drawer.isDrawerOpen(GravityCompat.START)){
                    hamburger = (ImageView) findViewById(R.id.leftNavigationButton);
                    hamburger.setImageResource(R.drawable.ic_menu_open);
                    onHamburgerClickCallback();
                    flag = 1;
                }

            }
        };
        drawer.addDrawerListener(mDrawerToggle);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.setCheckedItem(R.id.home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawer.closeDrawer(GravityCompat.END, false);
        drawer.closeDrawer(GravityCompat.START, false);
        navigationView.setCheckedItem(R.id.home);
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

    public void loadNotificationsRecycler(RecyclerView thisRecycler, List<JSONObject> products, int columns) {
        layoutManager = new GridLayoutManager(this, columns);
        thisRecycler.setHasFixedSize(true); // set has fixed size
        thisRecycler.setLayoutManager(layoutManager); // set layout manager

        // create and set adapter
        adapter = new NotificationAdapter(products, this);
        thisRecycler.setAdapter(adapter);
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
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            Intent main_activity = new Intent(getApplicationContext(), Home.class);
            main_activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            main_activity.putExtra("EXIT", true);
            startActivity(main_activity);
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();

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
//            default:
//                category = "More";
//                startActivity(new Intent(Home.this, Categories.class));
//                break;
        }
        categorySelected(category);
        // instead of toast, go to correct category activity
    }

    // onclick handler for cards
    // as of now, just goes to item-listing activity

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
//                    Toast.makeText(getApplicationContext(), "Loading..  " + category +" " + itemsList.size(), Toast.LENGTH_LONG).show();
                    recommendedProducts = (ArrayList<Product>) itemsList;
                    loadRecyclerHorizontal(recommendedRecycler, itemsList, 1);
                }
            }
        });
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

    @Override
    public void onSearch(String search) {
        Log.i(TAG,"onSearch received "+search);
        newlyListedRecycler.setVisibility(View.GONE);
        newHeader.setVisibility(View.GONE);
        recommendedHeader.setText("Searched Items...");
        loadRecyclerHorizontal(recommendedRecycler, searchProducts(recommendedProducts, search), 1);
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

    public void setNavigationHeader() {

    }

    @Override
    public void onSearchBack() {
        newlyListedRecycler.setVisibility(View.VISIBLE);
        newHeader.setVisibility(View.VISIBLE);
        recommendedHeader.setText("Recommended For You");
        loadRecyclerHorizontal(recommendedRecycler, recommendedProducts, 1);
    }
    public void getUserDetails(){
        userName.setText((String) UserDb.myUser.get("name"));
        userEmail.setText((String) UserDb.myUser.get("email"));
        if(userDb.myUser.containsKey("imageUri")) {
            Picasso.get().load((String)UserDb.myUser.get("imageUri")).into(profilePic);
        }
    }
}