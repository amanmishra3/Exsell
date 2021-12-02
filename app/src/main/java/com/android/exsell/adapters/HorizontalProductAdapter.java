package com.android.exsell.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;
import com.android.exsell.UI.ItemListing;
import com.android.exsell.UI.MyListings;
import com.android.exsell.UI.NewListing;
import com.android.exsell.db.ItemDb;
import com.android.exsell.db.UserDb;
import com.android.exsell.models.Product;
import com.android.exsell.listeners.productListener;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HorizontalProductAdapter extends RecyclerView.Adapter<HorizontalProductAdapter.MyViewHolder> {
    private List<Product> products;
    private List<Product> productsSource;
    private String TAG = "HorizontalProductAdapter";
    private Context context;
    private Timer timer;
    private ItemDb itemDb;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        Button edit_item, mark_sold;
        TextView textViewTitle, textViewPrice, textViewDescription;
        ImageView imageViewIcon;
        private Product selectedProduct;

        public MyViewHolder(View itemView, boolean edit){
            super(itemView);
            this.card = (CardView) itemView.findViewById(R.id.layout_horizontal_card);
            this.edit_item = edit ? (Button) itemView.findViewById(R.id.button_edit_profile) : null;
            this.mark_sold = edit ? (Button) itemView.findViewById(R.id.button_saved_videos) : null;
            this.textViewTitle = (TextView) itemView.findViewById(R.id.itemTitle);
            this.textViewPrice = (TextView) itemView.findViewById(R.id.itemPrice);
            this.textViewDescription = (TextView) itemView.findViewById(R.id.itemDescription);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.itemImage);
        }
    }

    public HorizontalProductAdapter(List<Product> data, Context context){
        this.products = data;
        this.context = context;
        this.productsSource = data;
        itemDb = ItemDb.newInstance();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        String className = context.getClass().getName();
        Log.i(TAG, "Horizontal Product Adapter called by " + className);
        MyViewHolder myViewHolder;
        if (className.equals("com.android.exsell.UI.MyListings") ) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_item_tile_horizontal_my_listing, parent, false);
            myViewHolder = new MyViewHolder(view, true);
        }
        else{
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_item_tile_horizontal, parent, false);
            myViewHolder = new MyViewHolder(view, false);
        }
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewPrice = holder.textViewPrice;
        TextView textViewDescription = holder.textViewDescription;
        ImageView imageView = holder.imageViewIcon;
        holder.selectedProduct = products.get(position);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "On Click"+holder.selectedProduct.getTitle());
//                productListener.onProductClicked(products.get(position), position);
                ItemDb.setCurrentProduct(holder.selectedProduct);
                Intent intent = new Intent(context, ItemListing.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
        if(holder.edit_item != null) {
            holder.edit_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, NewListing.class);
                    intent.putExtra("imageUri", holder.selectedProduct.getImageUri());
                    intent.putExtra("title", holder.selectedProduct.getTitle());
                    intent.putExtra("description", holder.selectedProduct.getDescription());
                    intent.putExtra("price", holder.selectedProduct.getPrice());
                    intent.putExtra("productId", holder.selectedProduct.getProductId());
                    intent.putExtra("load", "true");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        if(holder.mark_sold != null) {
            holder.mark_sold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Mark as Sold");
                    alert.setMessage("Are you sure you want to mark this item as SOLD?");
                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "Marking Item as sold",Toast.LENGTH_LONG);
                            itemDb.deleteItem((String)holder.selectedProduct.getProductId());
                            Intent intent = new Intent(context, MyListings.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            context.startActivity(intent);
                        }
                    });
                    alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // close dialog
                            dialog.cancel();
                        }
                    });
                    alert.show();
                }
            });
        }


        textViewTitle.setText(products.get(position).getTitle());
        textViewPrice.setText("$"+products.get(position).getPrice());
        textViewDescription.setText(products.get(position).getDescription());
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
