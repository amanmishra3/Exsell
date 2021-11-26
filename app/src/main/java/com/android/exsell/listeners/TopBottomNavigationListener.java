package com.android.exsell.listeners;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContentInfo;
import android.view.View;

import com.android.exsell.R;
import com.android.exsell.UI.MessagePreviews;
import com.android.exsell.UI.MyListings;
import com.android.exsell.UI.NewListing;
import com.android.exsell.UI.SearchBar;
import com.android.exsell.UI.WishlistActivity;

public class TopBottomNavigationListener implements View.OnClickListener{
    private String TAG = "TopBottonNavigation Listener";

    private int id;
    private Context context;
    public TopBottomNavigationListener(int layoutID, Context context){
        this.id = layoutID;
        this.context = context;
    }
    @Override
    public void onClick(View v) {
        if(id == R.id.searchButton){
            searchFunction();
        }
        else if(id == R.id.wishlistButton){
            wishlistButton();
        }
        else if(id == R.id.addItemButton){
            addItemButton();
        }
        else if(id == R.id.chatButton){
            startChat();
        }

    }

    private void startChat() {
        Log.i(TAG, "Chat Function");
        Intent intent = new Intent(context, MessagePreviews.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void addItemButton() {
        Log.i(TAG, "Add item Function");
        Intent intent = new Intent(context, NewListing.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void wishlistButton() {
        Log.i(TAG, "Wishlist Function");
        Intent intent = new Intent(context, WishlistActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void searchFunction() {
        Log.i(TAG, "Search Function");
        Intent intent = new Intent(context, SearchBar.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
