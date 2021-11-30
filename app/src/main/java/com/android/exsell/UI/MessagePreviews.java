package com.android.exsell.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FieldPath;
import com.google.firestore.v1.MapValue;
import com.squareup.picasso.Picasso;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagePreviews extends AppCompatActivity implements MessagePreviewAdapter.OnSelectListener, FragmentTopBar.navbarHamburgerOnClickCallback, FragmentSearchBar.SearchBarOnSearch, FragmentTopBar.NotificationBellClickCallback, FragmentSearchBar.SearchBarBack {
    private static final String TAG = "MessagePreviews";

    FirebaseAuth mAuth;
    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;
    RecyclerView notificationRecycler;
    private RecyclerView.LayoutManager layoutManager;
    public static RecyclerView.Adapter adapter;

    private ArrayList<Preview> previewArrayList;
    private RecyclerView recyclerView;
    private ImageView search, wishlist, addListing, message, notification, messageIcon, profilePic;
    private TextView userName, userEmail;
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



        recyclerView = findViewById(R.id.message_preview_list);
        previewArrayList = new ArrayList<>();

        setPreviewInfo();  // set adapter
//        getMessagePreviews(); // use firebase data

//        setAdapter();
    }

    public void loadNotificationsRecycler(RecyclerView thisRecycler, List<JSONObject> products, int columns) {
        layoutManager = new GridLayoutManager(this, columns);
        thisRecycler.setHasFixedSize(true); // set has fixed size
        thisRecycler.setLayoutManager(layoutManager); // set layout manager

        // create and set adapter
        adapter = new NotificationAdapter(products, this);
        thisRecycler.setAdapter(adapter);
    }

    private void setAdapter() {
        Log.i(TAG, "setAdapter");
        MessagePreviewAdapter adapter = new MessagePreviewAdapter(previewArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

//    public void setPreviewInfo() {
//        Log.i(TAG, "setPreviewInfo");
//        Calendar timeStamp = Calendar.getInstance();
//        previewArrayList.add(new Preview("1", "andrew", "hello there", timeStamp, null));
//        previewArrayList.add(new Preview("2", "jack", "my name is jack", timeStamp, null));
//        previewArrayList.add(new Preview("3", "paul", "wheres the gabagoo", timeStamp, null));
//        previewArrayList.add(new Preview("4", "sam", "how much?????", timeStamp, null));
//    }

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

//                        Image profilePic = (Image) doc.get("otherPic");
//                        preview.setProfilePic(profilePic);

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
                                                    Log.i(TAG, preview.getMessage() + " ");
                                                    preview.setMessage(message);

                                                    Calendar calendar = Calendar.getInstance();
                                                    Map<String, Object> map = (Map<String, Object>) snapshot.get("previewTimeStamp");
                                                    calendar.setTime(((Timestamp) map.get("time")).toDate());
                                                    preview.setTimeStamp(calendar);

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
//        previewArrayList.sort();
    }

    public void setPreviewInfo() {
        mAuth = FirebaseAuth.getInstance();
        String uidSelf = mAuth.getCurrentUser().getUid();
        FirebaseFirestore.getInstance()
                .collection("Users").document(uidSelf).collection("messages")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots != null) {
                    List<String> myChats = new ArrayList<>();
                    Map<String, Object> mp= new HashMap<>();
                    for(DocumentSnapshot doc: queryDocumentSnapshots) {
                        Log.i(TAG, "doc data "+doc.getData());
                        myChats.add(doc.getString("messageId"));
                        Map<String, Object> tmp = new HashMap<>();
                        tmp.put("messageId",doc.getString("messageId") );
                        tmp.put("name", doc.getString("otherNames"));
                        tmp.put("profilePic", doc.getString("otherPic"));
                        mp.put(doc.getString("messageId"), tmp);
                    }
                    FirebaseFirestore.getInstance()
                            .collection("messages")
                            .whereIn(FieldPath.documentId(), myChats)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    for(DocumentSnapshot doc: queryDocumentSnapshots) {
                                        Log.i(TAG, "doc data 2 "+doc.getData());
                                        String messageId = doc.getString("messageId");
                                        Map<String, Object> tmp = (Map<String, Object>) mp.get(messageId);
                                        String name = (String) tmp.get("name");
                                        String msg = doc.getString("previewMessage");
                                        Log.i(TAG, "adapter data "+messageId+" "+name+ " "+msg);
                                        previewArrayList.add(new Preview(messageId, name, msg, Calendar.getInstance(), null));
                                    }
                                    setAdapter();
                                }
                            });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onSelectClick(int position) {
        Intent intent = new Intent(this, PrivateMessage.class);
        intent.putExtra("messageId", previewArrayList.get(position).getMessageId());
        intent.putExtra("name", previewArrayList.get(position).getName());
        startActivity(intent);
    }

    @Override
    public void onHamburgerClickCallback() {
        Log.i(TAG, "onHamburgerClickCallback");
        drawer.closeDrawer(GravityCompat.END, false);
        drawer.openDrawer(GravityCompat.START);
        userName = (TextView) drawer.findViewById(R.id.userNameNav);
        userEmail = (TextView) drawer.findViewById(R.id.userEmailNav);
        profilePic = (ImageView) drawer.findViewById(R.id.profilePicNav);
        getUserDetails();
    }

    @Override
    public void onNotificationBellClick() {
        Log.i(TAG, "onNotificationBellClick");
        drawer.closeDrawer(GravityCompat.START, false);
        drawer.openDrawer(GravityCompat.END);
    }

    public void setNavigationHeader() {

    }

    @Override
    public void onSearch(String search) {
        Log.i(TAG, "onSearch received " + search);
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
