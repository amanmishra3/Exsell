package com.android.exsell.adapters;
import com.android.exsell.R;
import com.android.exsell.models.Product;
import com.squareup.picasso.Picasso;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private List<Product> products;
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

    public ProductAdapter(List<Product> data){
        this.products = data;
    }


    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_tile, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position) {
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

        textViewTitle.setText(products.get(position).getTitle());
        textViewPrice.setText("$"+products.get(position).getPrice());
        if(products.get(position).getImageUri() != null) {
            Picasso.get().load(products.get(position).getImageUri()).into(imageView);
        } else {
            imageView.setImageResource(products.get(position).getImage() != -1 ? R.drawable.test_image : products.get(position).getImage());
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
