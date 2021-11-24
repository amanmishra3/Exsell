package com.android.exsell.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.exsell.R;
import com.android.exsell.models.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private ArrayList<Category> category;
    public String TAG = "ProductAdapter";

    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        View currentItem;
        TextView textViewTitle;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView){
            super(itemView);
//            this.currentItem = itemView;
            this.textViewTitle = (TextView) itemView.findViewById(R.id.itemTitle);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.itemImage);
        }
    }

    public CategoryAdapter(ArrayList<Category> data){
        this.category = data;
    }


    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_category_tile, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {
        TextView textViewTitle = holder.textViewTitle;
        ImageView imageView = holder.imageViewIcon;
//        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Home.itemDetails(products.get(position), position);
//            }
//        });

        textViewTitle.setText(category.get(position).getTitle());
        imageView.setImageResource(category.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return category.size();
    }
}
