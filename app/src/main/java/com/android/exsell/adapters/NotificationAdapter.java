package com.android.exsell.adapters;
import com.android.exsell.R;
import com.android.exsell.UI.PrivateMessage;
import com.android.exsell.db.ItemDb;
import com.android.exsell.db.UserDb;
import com.android.exsell.models.Product;
import com.google.firebase.firestore.auth.User;
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
    private UserDb userDb;
    public String TAG = "ProductAdapter";
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //        View currentItem;
        private ConstraintLayout notificationTile;
        TextView textViewTitle, textViewPrice, textViewTags;
        ImageView imageViewIcon, notificationIcon;
        private JSONObject selectedNotification;

        public MyViewHolder(View itemView){
            super(itemView);
//            this.currentItem = itemView;
            this.notificationTile = (ConstraintLayout) itemView.findViewById(R.id.notification_main);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.tv_notification);
//            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.itemImage);
            this.notificationIcon = (ImageView) itemView.findViewById(R.id.notificationButton);
        }
    }

    public NotificationAdapter(List<JSONObject> data, Context context){
        this.notifications = data;
        this.context = context;
        userDb = UserDb.newInstance();
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
        holder.selectedNotification = notifications.get(position);
        holder.notificationTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Home.itemDetails(products.get(position), position);
                Log.i(TAG, "ProductAdapter "+holder.selectedNotification);
                Intent intent = new Intent(context, PrivateMessage.class);
                try {
                    intent.putExtra("messageId", (String)holder.selectedNotification.get("messageId"));
                    intent.putExtra("name", (String)holder.selectedNotification.get("name"));
                    userDb.updateNotifications((String) UserDb.myUser.get("userId"), holder.selectedNotification);
                } catch(Exception e) {}
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.selectedNotification = notifications.get(position);
        String messageText = new String();
        try {
            messageText = (String)notifications.get(position).get("message");
            if(notifications.get(position).has("new")) {
                holder.notificationTile.setBackgroundColor(context.getResources().getColor(R.color.bumble));
            }
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