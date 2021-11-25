package com.android.exsell.chat;

import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;

import java.util.ArrayList;
import java.util.Calendar;

public class MessagePreviewAdapter extends RecyclerView.Adapter<MessagePreviewAdapter.MyViewHolder> {
    private static final String TAG = "MessagePreviewAdapter";
    private ArrayList<Preview> previewArrayList;

    public MessagePreviewAdapter(ArrayList<Preview> previewArrayList) {
        Log.i(TAG, "MessagePreviewAdapter");
        this.previewArrayList = previewArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView messageText;
        private TextView deliveredAt;
        private ImageView profilePic;


        public MyViewHolder(final View view) {
            super(view);
            nameText = view.findViewById(R.id.contact_name);
            messageText = view.findViewById(R.id.message_preview);
            deliveredAt = view.findViewById(R.id.time_stamp);
            profilePic = view.findViewById(R.id.profile_pic);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), PrivateMessage.class);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_preview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder");
        String name = previewArrayList.get(position).name;
        String message = previewArrayList.get(position).message;
        Calendar timeStamp = previewArrayList.get(position).timeStamp;
        Image profilePic = previewArrayList.get(position).profilePic;

        String time;

        if (timeStamp.get(Calendar.DATE) == Calendar.getInstance().get(Calendar.DATE)
                && timeStamp.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)
                && timeStamp.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {

            String hour = String.valueOf(timeStamp.get(Calendar.HOUR));
            String min = String.valueOf(timeStamp.get(Calendar.MINUTE));
            String sec = String.valueOf(timeStamp.get(Calendar.SECOND));

            time = hour + ":" + min + ":" + sec;
        } else {
            String day = String.valueOf(timeStamp.get(Calendar.DATE));
            String month = String.valueOf(timeStamp.get(Calendar.MONTH));
            String year = String.valueOf(timeStamp.get(Calendar.YEAR));

            time = day + "/" + month + "/" + year;
        }

        holder.nameText.setText(name);
        holder.messageText.setText(message);
        holder.deliveredAt.setText(time);
//        holder.profilePic.setImage(profilePic);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount");
        return previewArrayList.size();
    }
}
