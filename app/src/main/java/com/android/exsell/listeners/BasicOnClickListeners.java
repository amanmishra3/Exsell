package com.android.exsell.listeners;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.android.exsell.R;

public class BasicOnClickListeners implements View.OnClickListener{
    private View view;
    private Context context;
    private int layoutId;
    public BasicOnClickListeners(int layoutId, View view, Context context) {
        this.view = view;
        this.context = context;
        this.layoutId = layoutId;
    }
    @Override
    public void onClick(View view) {
        if(layoutId == R.id.add_to_wishlist) {
            ImageView img = view.findViewById(R.id.wishlistButton);
            img.setColorFilter(R.color.bumble);
        }
    }
}
