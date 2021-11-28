package com.android.exsell.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;
import com.android.exsell.UI.ItemListing;
import com.android.exsell.db.ItemDb;
import com.android.exsell.models.Product;
import com.android.exsell.listeners.productListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HorizontalProductAdapter extends RecyclerView.Adapter<HorizontalProductAdapter.MyViewHolder> {
    private List<Product> products;
    private String TAG = "HorizontalProductAdapter";
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        View currentItem;
        CardView card;
        TextView textViewTitle, textViewPrice, textViewTags;
        ImageView imageViewIcon;
        private Product selectedProduct;

        public MyViewHolder(View itemView){
            super(itemView);
            this.card = (CardView) itemView.findViewById(R.id.layout_horizontal_card);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.itemTitle);
            this.textViewPrice = (TextView) itemView.findViewById(R.id.itemPrice);
//            this.textViewTags = (TextView) itemView.findViewById(R.id.itemTags);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.itemImage);
        }
    }

    public HorizontalProductAdapter(List<Product> data, Context context){
        this.products = data;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_item_tile_horizontal, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewPrice = holder.textViewPrice;
//        TextView textViewTags = holder.textViewTags;
        ImageView imageView = holder.imageViewIcon;
        holder.selectedProduct = products.get(position);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "On Click"+holder.selectedProduct.getTitle());
//                productListener.onProductClicked(products.get(position), position);
                ItemDb.setCurrentProduct(holder.selectedProduct);
                Intent intent = new Intent(context, ItemListing.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        textViewTitle.setText(products.get(position).getTitle());
        textViewPrice.setText("$"+products.get(position).getPrice());
//        textViewTags.setText(products.get(position).getTags().toString());
        if(products.get(position).getImageUri() != null) {
            Picasso.get().load(products.get(position).getImageUri()).into(imageView);
        } else {
            imageView.setImageResource(products.get(position).getImage() != -1 ? R.drawable.test_image : products.get(position).getImage());
        }
    }

    @Override
    public int getItemCount() {
        if(products != null)
            return products.size();
        return 0;
    }
}
