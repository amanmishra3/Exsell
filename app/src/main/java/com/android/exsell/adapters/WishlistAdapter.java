package com.android.exsell.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;
import com.android.exsell.models.Wishlist;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.MyViewHolder> {
    private ArrayList<Wishlist> wishlist;
    public String TAG = "ProductAdapter";

    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        View currentItem;
        TextView textViewTitle, textViewPrice, textViewTags;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView){
            super(itemView);
//            this.currentItem = itemView;
            this.textViewTitle = (TextView) itemView.findViewById(R.id.itemTitle);
            this.textViewPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            this.textViewTags = (TextView) itemView.findViewById(R.id.itemTags);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.itemImage);
        }
    }

    public WishlistAdapter(ArrayList<Wishlist> data){
        this.wishlist = data;
    }


    @NonNull
    @Override
    public WishlistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_tile_wishlist, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.MyViewHolder holder, int position) {
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewPrice = holder.textViewPrice;
        TextView textViewTags = holder.textViewTags;
        ImageView imageView = holder.imageViewIcon;
//        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Home.itemDetails(products.get(position), position);
//            }
//        });

        textViewTitle.setText(wishlist.get(position).getTitle());
        textViewPrice.setText("$"+wishlist.get(position).getPrice());
//        textViewTags.setText(wishlist.get(position).getTagString());
        imageView.setImageResource(wishlist.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return wishlist.size();
    }
}
