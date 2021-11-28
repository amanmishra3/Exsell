package com.android.exsell.models;

public class Category {
    String title;
    int id_;
    int image;

    public Category(int id_, String title, int image){
        this.id_ = id_;
        this.title = title;
        this.image = image;

    }

    public String getTitle(){
        return title;
    }

    public int getId(){
        return id_;
    }

    public int getImage(){
        return image;
    }

}
