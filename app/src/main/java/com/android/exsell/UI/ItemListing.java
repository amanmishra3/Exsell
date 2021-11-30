package com.android.exsell.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.exsell.R;
import com.android.exsell.adapters.NotificationAdapter;
import com.android.exsell.db.ItemDb;
import com.android.exsell.db.UserDb;
import com.android.exsell.fragments.FragmentSearchBar;
import com.android.exsell.fragments.FragmentTopBar;
import com.android.exsell.listeners.BasicOnClickListeners;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Users;
import com.android.exsell.services.SendMessage;
import com.android.exsell.models.Notifications;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ItemListing extends AppCompatActivity implements FragmentTopBar.navbarHamburgerOnClickCallback, FragmentSearchBar.SearchBarOnSearch, FragmentTopBar.NotificationBellClickCallback, FragmentSearchBar.SearchBarBack {
    private String TAG = "Categories";
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;
    RecyclerView notificationRecycler;
    private RecyclerView.LayoutManager layoutManager;
    public static RecyclerView.Adapter adapter;
    private View parent;
    private UserDb userDb;
    private Map<String, Object> product;
    private ImageView search, wishlist, addListing, message, productImage, addToWishlist,notification, profilePic;
    private TextView title, description, price, tags, userEmail, userName;
    private Button contact_seller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

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
        contact_seller =  (Button) parent.findViewById(R.id.contact_seller);
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

        notificationRecycler = (RecyclerView) findViewById(R.id.right_drawer);
        notificationRecycler.setNestedScrollingEnabled(true);
        loadNotificationsRecycler(notificationRecycler, Notifications.getMyNotifications(), 1);

        //-->getting from static stuff for now
        if(product.containsKey("imageUri") && product.get("imageUri") != null) {
            Picasso.get().load((String)product.get("imageUri")).into(productImage);
        } else {
            productImage.setImageResource(R.drawable.test_image);
        }
        title.setText((String)product.get("title"));
        price.setText("$"+product.get("price").toString());
        description.setText((String)product.get("description"));
        tags.setText("");
        List<String> listTags;
        listTags = (List<String>) product.get("tags");
        String stringTags = String.join(", ", listTags);
        tags.setText("Tags: "+stringTags);

        checkWishList();

        addToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToWishList();
            }
        });

        contact_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDb.getUser((String)product.get("seller"), new UserDb.getUserCallback() {
                    @Override
                    public void onCallback(Users user) {
                        if(user != null) {
                            SendMessage.sendMessage(user.getRegisterationToken(), " Seller Notification", UserDb.myUser.get("name") + "wants to buy" + product.get("title"), "intent", new Date());
                        }
                    }
                });
            }
        });
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
    }
    public void contactSeller(View view){
//        Intent intent = new Intent(this, Home.class);
//        intent.putExtra("sellerID", 0);
//        startActivity(intent);
        Toast.makeText(this, "Contacting seller",Toast.LENGTH_SHORT).show();
    }

    public void buyNow(View view){
//        Intent intent = new Intent(this, Home.class);
//        intent.putExtra("sellerID", 0);
//        startActivity(intent);
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
            Toast.makeText(this, "Item Removed from Wishlist",Toast.LENGTH_LONG).show();
        } else {
            myWishlist.add((String)product.get("productId"));
            userDb.addToWishList((String)UserDb.myUser.get("userId"),(String)product.get("productId"));
            addToWishlist.setImageResource(R.drawable.ic_heart_yellow);
            Toast.makeText(this, "Item added to Wishlist",Toast.LENGTH_LONG).show();
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
        //startActivity(new Intent(getApplicationContext(), Home.class));
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