package com.android.exsell.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;
import com.android.exsell.models.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MessageAdapter";
    private ArrayList<Message> messageArrayList;

    private static final int TYPE_SELF = 0;
    private static final int TYPE_OTHER = 1;

    public MessageAdapter(ArrayList<Message> messageArrayList) {
        Log.i(TAG, "MessageAdapter");
        this.messageArrayList = messageArrayList;
    }

    public class OtherViewHolder extends RecyclerView.ViewHolder {
        private TextView message;
        private TextView deliveredAt;

        public OtherViewHolder(final View view) {
            super(view);
            message = view.findViewById(R.id.otherMessage);
            deliveredAt = view.findViewById(R.id.otherMessageTime);
        }
    }

    public class SelfViewHolder extends RecyclerView.ViewHolder {
        private TextView message;
        private TextView deliveredAt;

        public SelfViewHolder(final View view) {
            super(view);
            message = view.findViewById(R.id.selfMessage);
            deliveredAt = view.findViewById(R.id.selfMessageTime);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Log.i(TAG, "getItemViewType");
        String uidSelf = FirebaseAuth.getInstance().getUid();
        String uidSender = messageArrayList.get(position).getSender();
        Log.i(TAG, "uidSender: " + uidSender + messageArrayList.get(position).getMessage());
        if (uidSender.compareTo(uidSelf) == 0) {
            return TYPE_SELF;
        } else {
            return TYPE_OTHER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");
        View itemView;
        if (viewType == TYPE_OTHER) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_message, parent, false);
            return new OtherViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.self_message, parent, false);
            return new SelfViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder");

        String message = messageArrayList.get(position).getMessage();
        Calendar timeStamp = messageArrayList.get(position).getTimeStamp();
        Log.i(TAG, timeStamp.getTime().toString());

        String time;

        if (timeStamp.get(Calendar.DATE) == Calendar.getInstance().get(Calendar.DATE)
                && timeStamp.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)
                && timeStamp.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {

            String hour;
            if(timeStamp.get(Calendar.HOUR_OF_DAY) > 12)
                hour = String.valueOf(timeStamp.get(Calendar.HOUR));
            else
                hour = String.valueOf(timeStamp.get(Calendar.HOUR_OF_DAY));
            String min = String.valueOf(timeStamp.get(Calendar.MINUTE));
            if(min.length()==1)
                min = "0" + min;
            String ampm = timeStamp.get(Calendar.AM_PM) == 0 ? "AM": "PM";

            time = hour + ":" + min + " " + ampm;
        } else {
            String day = String.valueOf(timeStamp.get(Calendar.DATE));
            String month = String.valueOf(timeStamp.get(Calendar.MONTH));
            String year = String.valueOf(timeStamp.get(Calendar.YEAR));

            time = month + "/" + day + "/" + year;
        }

        if (getItemViewType(position) == TYPE_OTHER) {
            ((OtherViewHolder) holder).message.setText(message);
            ((OtherViewHolder) holder).deliveredAt.setText(time);
        } else {
            ((SelfViewHolder) holder).message.setText(message);
            ((SelfViewHolder) holder).deliveredAt.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount");
        if(messageArrayList == null)
            return 0;
        return messageArrayList.size();
    }
}
