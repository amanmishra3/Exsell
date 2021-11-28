package com.android.exsell.adapters;
import com.android.exsell.R;
import com.android.exsell.UI.ItemListing;
import com.android.exsell.db.ItemDb;
import com.android.exsell.models.Product;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private List<JSONObject> notifications;
    public String TAG = "ProductAdapter";
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //        View currentItem;
        private ConstraintLayout notificationTile;
        TextView textViewTitle, textViewPrice, textViewTags;
        ImageView imageViewIcon;
        private JSONObject selectedNotification;

        public MyViewHolder(View itemView){
            super(itemView);
//            this.currentItem = itemView;
            this.notificationTile = (ConstraintLayout) itemView.findViewById(R.id.notification_main);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.tv_notification);
//            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.itemImage);
        }
    }

    public NotificationAdapter(List<JSONObject> data, Context context){
        this.notifications = data;
        this.context = context;
    }


    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_notification_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewPrice = holder.textViewPrice;
        TextView textViewTags = holder.textViewTags;
        ImageView imageView = holder.imageViewIcon;
        holder.notificationTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Home.itemDetails(products.get(position), position);
                Log.i(TAG, "ProductAdapter "+holder.selectedNotification);
//                ItemDb.setCurrentProduct(holder.selectedProduct);
//                Intent intent = new Intent(context, ItemListing.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
            }
        });
        holder.selectedNotification = notifications.get(position);
        String messageText = new String();
        try {
            messageText = (String)notifications.get(position).get("message");
        } catch(Exception e) {
        }
        textViewTitle.setText(messageText);
//        if(products.get(position).getImageUri() != null) {
//            Picasso.get().load(products.get(position).getImageUri()).into(imageView);
//        } else {
//            imageView.setImageResource(products.get(position).getImage() != -1 ? R.drawable.test_image : products.get(position).getImage());
//        }
    }

    @Override
    public int getItemCount() {
        if(notifications != null)
            return notifications.size();
        return 0;
    }
}