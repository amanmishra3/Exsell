package com.android.exsell.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;
import com.android.exsell.adapters.MessagePreviewAdapter;
import com.android.exsell.fragments.FragmentSearchBar;
import com.android.exsell.fragments.FragmentTopBar;
import com.android.exsell.listeners.TopBottomNavigationListener;
import com.android.exsell.listeners.navigationListener;
import com.android.exsell.models.Preview;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;

public class MessagePreviews extends AppCompatActivity implements MessagePreviewAdapter.OnSelectListener, FragmentTopBar.navbarHamburgerOnClickCallback, FragmentSearchBar.SearchBarOnSearch  {
    private static final String TAG = "MessagePreviews";

    LinearLayout layoutTop, layoutBottom;
    DrawerLayout drawer;
    NavigationView navigationView;

    private ArrayList<Preview> previewArrayList;
    private RecyclerView recyclerView;
    private ImageView search, wishlist, addListing, message, notification;

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
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(new navigationListener(getApplicationContext()));

        wishlist = (ImageView) layoutBottom.findViewById(R.id.wishlistButton);
        wishlist.setOnClickListener(new TopBottomNavigationListener(R.id.wishlistButton, getApplicationContext()));
        addListing = (ImageView) layoutBottom.findViewById(R.id.addItemButton);
        addListing.setOnClickListener(new TopBottomNavigationListener(R.id.addItemButton, getApplicationContext()));
        message = (ImageView) findViewById(R.id.chatButton);
        message.setOnClickListener(new TopBottomNavigationListener(R.id.chatButton, getApplicationContext()));

        recyclerView = findViewById(R.id.message_preview_list);
        previewArrayList = new ArrayList<>();

        setPreviewInfo(); // TODO replace with getMessagePreviews() when implementedgit

        setAdapter();
    }

    private void setAdapter() {
        Log.i(TAG, "setAdapter");
        MessagePreviewAdapter adapter = new MessagePreviewAdapter(previewArrayList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void setPreviewInfo() {
        Log.i(TAG, "setPreviewInfo");
        Calendar timeStamp = Calendar.getInstance();
        previewArrayList.add(new Preview("1", "andrew", "hello there", timeStamp, null));
        previewArrayList.add(new Preview("2", "jack", "my name is jack", timeStamp, null));
        previewArrayList.add(new Preview("3", "paul", "wheres the gabagoo", timeStamp, null));
        previewArrayList.add(new Preview("4", "sam", "how much?????", timeStamp, null));
    }

    public void getMessagePreviews() {
        Log.i(TAG, "getMessagePreviews");
        /*
        TODO
        For each message thread that user has
            Preview preview = new Preview()
            preview.uid = get contacts uid
            preview.name = get contacts name
            preview.message = get most recent message
            preview.timeStamp = get timestamp for most recent message
            preview.profilePic = get contacts profile pic

            previewArrayList.append(previews)

         Sort list in descending order by preview.time
         */
    }

    @Override
    public void onSelectClick(int position) {
        Intent intent = new Intent(this, PrivateMessage.class );
        intent.putExtra("uid",  previewArrayList.get(position).getUid());
        intent.putExtra("name",  previewArrayList.get(position).getName());
        startActivity(intent);
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
