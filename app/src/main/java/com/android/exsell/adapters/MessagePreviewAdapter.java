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

import java.util.ArrayList;
import java.util.Calendar;

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
        private ImageView profilePic;

        OnSelectListener onSelectListener;


        public MyViewHolder(final View view, OnSelectListener onSelectListener ) {
            super(view);
            this.onSelectListener = onSelectListener;

            nameText = view.findViewById(R.id.contact_name);
            messageText = view.findViewById(R.id.message_preview);
            deliveredAt = view.findViewById(R.id.time_stamp);
            profilePic = view.findViewById(R.id.profile_pic);

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
        Log.i(TAG, "onBindViewHolder");
        String name = previewArrayList.get(position).getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        String message = previewArrayList.get(position).getMessage();
        Calendar timeStamp = previewArrayList.get(position).getTimeStamp();
        Image profilePic = previewArrayList.get(position).getProfilePic();

        String time;

        if (timeStamp.get(Calendar.DATE) == Calendar.getInstance().get(Calendar.DATE)
                && timeStamp.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)
                && timeStamp.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {

            String hour = String.valueOf(timeStamp.get(Calendar.HOUR));
            String min = String.valueOf(timeStamp.get(Calendar.MINUTE));
            if(min.length()==1)
                min = "0" + min;
            String sec = String.valueOf(timeStamp.get(Calendar.SECOND));
            if(sec.length()==1)
                sec = "0" + min;

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

    public interface OnSelectListener {
        void onSelectClick(int position);
    }
}
