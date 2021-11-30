package com.android.exsell.adapters;

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
import com.android.exsell.models.Preview;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagePreviewAdapter extends RecyclerView.Adapter<MessagePreviewAdapter.MyViewHolder> {
    private static final String TAG = "MessagePreviewAdapter";
    private ArrayList<Preview> previewArrayList;

    private OnSelectListener mOnSelectListener;

    public MessagePreviewAdapter(ArrayList<Preview> previewArrayList, OnSelectListener onSelectListener) {
        Log.i(TAG, "MessagePreviewAdapter");
        this.previewArrayList = previewArrayList;
        this.mOnSelectListener = onSelectListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nameText;
        private TextView messageText;
        private TextView deliveredAt;
        private CircleImageView profilePic;

        OnSelectListener onSelectListener;


        public MyViewHolder(final View view, OnSelectListener onSelectListener ) {
            super(view);
            this.onSelectListener = onSelectListener;

            nameText = view.findViewById(R.id.contact_name);
            messageText = view.findViewById(R.id.message_preview);
            deliveredAt = view.findViewById(R.id.time_stamp);
            profilePic = (CircleImageView) view.findViewById(R.id.profile_pic);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onSelectListener.onSelectClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_preview, parent, false);
        return new MyViewHolder(itemView, mOnSelectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder "+previewArrayList.get(position));
        String name = previewArrayList.get(position).getName();
        if(name!= null)
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
        String message = previewArrayList.get(position).getMessage();
        Calendar timeStamp = previewArrayList.get(position).getTimeStamp();
        String profilePic = previewArrayList.get(position).getProfilePic();

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

            time = day + "/" + month + "/" + year;
        }

        holder.nameText.setText(name);
        holder.messageText.setText(message);
        holder.deliveredAt.setText(time);
        if(profilePic != null && profilePic.length() > 0)
            Picasso.get().load(profilePic).into(holder.profilePic);
        // TODO implement profilePic
//        holder.profilePic.setImage(profilePic);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount");
        return previewArrayList.size();
    }

    public interface OnSelectListener {
        void onSelectClick(int position);
    }
}
