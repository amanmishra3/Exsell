package com.android.exsell.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;
import com.android.exsell.adapters.MessagePreviewAdapter;
import com.android.exsell.adapters.NotificationAdapter;
import com.android.exsell.db.UserDb;
import com.android.exsell.fragments.FragmentSearchBar;
import com.android.exsell.fragments.FragmentTopBar;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Notifications;
import com.android.exsell.models.Preview;
import com.android.exsell.models.Product;
import com.android.exsell.services.FirebaseNotificationService;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MessagePreviews extends AppCompatActivity implements MessagePreviewAdapter.OnSelectListener, FragmentTopBar.navbarHamburgerOnClickCallback, FragmentSearchBar.SearchBarOnSearch, FragmentTopBar.NotificationBellClickCallback, FragmentSearchBar.SearchBarBack, FirebaseNotificationService.notifcationReloaded {
    private static final String TAG = "MessagePreviews";

    FirebaseAuth mAuth;
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;
    RecyclerView notificationRecycler;
    private RecyclerView.LayoutManager layoutManager;
    public static RecyclerView.Adapter notificationAdapter;
    MessagePreviewAdapter adapter;

    private ArrayList<Preview> previewArrayList;
    private ArrayList<Preview> searchMessages;
    private RecyclerView recyclerView;
    private ImageView search, wishlist, addListing, message, notification, messageIcon, profilePic, hamburger;
    private TextView userName, userEmail;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.message_preview_list);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameTopBar, new FragmentTopBar());
        fragmentTransaction.commit();

        layoutBottom = findViewById(R.id.layoutBottomBar);
        messageIcon = (ImageView) layoutBottom.findViewById(R.id.chatButton);
        messageIcon.setImageResource(R.drawable.ic_chat2);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setNavigationItemSelectedListener(new navigationListener(MessagePreviews.this, this));

        wishlist = (ImageView) layoutBottom.findViewById(R.id.wishlistButton);
        wishlist.setOnClickListener(new TopBottomNavigationListener(R.id.wishlistButton, getApplicationContext()));
        addListing = (ImageView) layoutBottom.findViewById(R.id.addItemButton);
        addListing.setOnClickListener(new TopBottomNavigationListener(R.id.addItemButton, getApplicationContext()));
        message = (ImageView) findViewById(R.id.chatButton);
        message.setOnClickListener(new TopBottomNavigationListener(R.id.chatButton, getApplicationContext()));
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
                    flag = 2;
                }
                else if(drawer.isDrawerOpen(GravityCompat.START)){
                    hamburger = (ImageView) findViewById(R.id.leftNavigationButton);
                    hamburger.setImageResource(R.drawable.ic_menu_open);
                    flag = 1;
                }

            }
        };
        drawer.addDrawerListener(mDrawerToggle);

        recyclerView = findViewById(R.id.message_preview_list);

        getMessagePreviews();
        setAdapter();

        notificationRecycler = (RecyclerView) findViewById(R.id.right_drawer);
        notificationRecycler.setNestedScrollingEnabled(true);
        loadNotificationsRecycler(notificationRecycler, Notifications.getMyNotifications(), 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawer.closeDrawer(GravityCompat.END, false);
        drawer.closeDrawer(GravityCompat.START, false);
    }

    public void loadNotificationsRecycler(RecyclerView thisRecycler, List<JSONObject> products, int columns) {
        layoutManager = new GridLayoutManager(this, columns);
        thisRecycler.setHasFixedSize(true); // set has fixed size
        thisRecycler.setLayoutManager(layoutManager); // set layout manager

        // create and set adapter
        notificationAdapter = new NotificationAdapter(products, this);
        thisRecycler.setAdapter(notificationAdapter);
    }

    private void setAdapter() {
        Log.i(TAG, "setAdapter");
        adapter = new MessagePreviewAdapter(previewArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getMessagePreviews() {
        Log.i(TAG, "getMessagePreviews");
        mAuth = FirebaseAuth.getInstance();
        String uidSelf = mAuth.getCurrentUser().getUid();

        previewArrayList = new ArrayList<>();
        FirebaseFirestore.getInstance()
                .collection("Users").document(uidSelf).collection("messages")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.exists()) {
                                Preview preview = new Preview();

                                String messageId = doc.getString("messageId");
                                preview.setMessageId(messageId);

                                String name = doc.getString("otherName");
                                preview.setName(name);

                                String imageUri = doc.getString("otherPic");
                                preview.setProfilePic(imageUri);

                                // TODO implement profilePic query

                                preview.setMessage("");
                                preview.setTimeStamp(Calendar.getInstance());
                                setAdapter();
                                FirebaseFirestore.getInstance()
                                        .collection("messages").document(messageId)
                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                                @Nullable FirebaseFirestoreException e) {
                                                if (e != null) {
                                                    Log.w(TAG, "Listen failed.", e);
                                                    return;
                                                }
                                                if (snapshot != null && snapshot.exists()) {
                                                    String message = snapshot.getString("previewMessage");
                                                    preview.setMessage(message);

                                                    if(!message.equals(null) && !message.equals("")) {
                                                        Calendar calendar = Calendar.getInstance();
                                                        Map<String, Object> map = (Map<String, Object>) snapshot.get("previewTimeStamp");
                                                        calendar.setTime(((Timestamp) map.get("time")).toDate());
                                                        preview.setTimeStamp(calendar);
                                                    }

                                                    boolean sameMessage = false;
                                                    for(Preview p : previewArrayList) {
                                                        if(p.isSame(preview)) {
                                                            sameMessage = true;
                                                            break;
                                                        } else if(p.getMessageId().compareTo(preview.getMessageId()) == 0) {
                                                            previewArrayList.remove(p);
                                                            break;
                                                        }
                                                    }
                                                    if(!sameMessage) {
                                                        previewArrayList.add(preview);
                                                        Collections.sort(previewArrayList);
                                                        searchMessages = previewArrayList;
                                                        adapter.notifyItemInserted(previewArrayList.size() - 1);
                                                    }
                                                } else {
                                                    Log.d(TAG, "Current data: null");
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    @Override
    public void onSelectClick(int position) {
        Intent intent = new Intent(this, PrivateMessage.class);
        intent.putExtra("messageId", previewArrayList.get(position).getMessageId());
        intent.putExtra("name", previewArrayList.get(position).getName());
        intent.putExtra("imageUri", previewArrayList.get(position).getProfilePic());
        startActivity(intent);
    }

    public List<Preview> searchPreviews(List<Preview> searchMessages, String searchKeyword) {
        List<Preview> searchResult;
        if(searchKeyword.trim().isEmpty()) {
            searchResult = searchMessages;
        } else {
            ArrayList<Preview> temp = new ArrayList<>();
            for(Preview preview : searchMessages) {
                if(preview.getName().toLowerCase().contains(searchKeyword.toLowerCase())) {
                    temp.add(preview);
                }
            }
            searchResult = temp;
            Log.i("Size of temp products: ", String.valueOf(searchResult.size()));
        }
        return searchResult;
    }

    @Override
    public void onHamburgerClickCallback() {
        Log.i(TAG,"onHamburgerClickCallback");
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            drawer.closeDrawer(GravityCompat.END, false);
            drawer.openDrawer(GravityCompat.START);
            userName = (TextView) drawer.findViewById(R.id.userNameNav);
            userEmail = (TextView) drawer.findViewById(R.id.userEmailNav);
            profilePic = (ImageView) drawer.findViewById(R.id.profilePicNav);
            getUserDetails();

        }
    }

    @Override
    public void onNotificationBellClick() {
        if(drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }
        else {
            Log.i(TAG,"onNotificationBellClick");
            drawer.closeDrawer(GravityCompat.START, false);
            drawer.openDrawer(GravityCompat.END);
        }
    }

    public void setNavigationHeader() {

    }

    @Override
    public void onSearch(String search) {
        Log.i(TAG,"onSearch received "+search);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new MessagePreviewAdapter((ArrayList<Preview>) searchPreviews(searchMessages ,search), this));
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

    @Override
    public void reloadCallback(List<JSONObject> notifications) {
        notificationRecycler = (RecyclerView) findViewById(R.id.right_drawer);
        notificationRecycler.setNestedScrollingEnabled(true);
        loadNotificationsRecycler(notificationRecycler, notifications, 1);
    }
}
