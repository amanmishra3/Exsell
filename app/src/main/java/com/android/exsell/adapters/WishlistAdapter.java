package com.android.exsell.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;
import com.android.exsell.UI.ItemListing;
import com.android.exsell.db.ItemDb;
import com.android.exsell.models.Product;
import com.android.exsell.models.Wishlist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.MyViewHolder> {
    private List<Product> wishlist;
    public String TAG = "ProductAdapter";
    private Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        View currentItem;
        private CardView card;
        TextView textViewTitle, textViewPrice, textViewTags;
        ImageView imageViewIcon;
        private Product selectedProduct;

        public MyViewHolder(View itemView){
            super(itemView);
//            this.currentItem = itemView;
            this.card = (CardView) itemView.findViewById(R.id.wishlistCards);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.itemTitle);
            this.textViewPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            this.textViewTags = (TextView) itemView.findViewById(R.id.itemTags);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.itemImage);
        }
    }

    public WishlistAdapter(List<Product> data, Context context){
        this.wishlist = data;
        this.context = context;
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
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Home.itemDetails(products.get(position), position);
                ItemDb.setCurrentProduct(holder.selectedProduct);
                Intent intent = new Intent(context, ItemListing.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.selectedProduct = wishlist.get(position);
        textViewTitle.setText(wishlist.get(position).getTitle());
        textViewPrice.setText("$"+wishlist.get(position).getPrice());
//        textViewTags.setText(wishlist.get(position).getTagString());
        if(wishlist.get(position).getImageUri() != null) {
            Picasso.get().load(wishlist.get(position).getImageUri()).into(imageView);
        } else {
            imageView.setImageResource(wishlist.get(position).getImage());
        }
    }
    public void clear() {
        wishlist.clear();
    }
    @Override
    public int getItemCount() {
        if(wishlist != null)
            return wishlist.size();
        return 0;
    }
}
