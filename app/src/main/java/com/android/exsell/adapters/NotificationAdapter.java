package com.android.exsell.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;
import com.android.exsell.models.Notifications;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    Context context;
    ArrayList<Notifications> notificationsList;

    public NotificationAdapter(Context context, ArrayList<Notifications> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_notification_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        Notifications notifications = notificationsList.get(position);
        holder.textViewNotification.setText(notifications.getNotificationItem());
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNotification;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNotification = itemView.findViewById(R.id.tv_notification);
        }
    }
}
