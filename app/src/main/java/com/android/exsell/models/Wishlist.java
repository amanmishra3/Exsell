package com.android.exsell.models;

public class Wishlist {
    String title;
    int price;
    int id_;
    int image;
    String[] tags;

    public Wishlist(int id_, String title, int price, int image, String[] tags){
        this.id_ = id_;
        this.title = title;
        this.price = price;
        this.image = image;
        this.tags = tags;
    }

    public String getTitle(){
        return title;
    }

    public int getPrice(){
        return price;
    }

    public int getId(){
        return id_;
    }

    public int getImage(){
        return image;
    }

    public String[] getTags(){
        return tags;
    }

    public String getTagString(){
        String tagString = "";
        if(tags.length>0){
            tagString += tags[0];
            for(int i=1; i<tags.length; i++){
                tagString += ", "+tags[i];
            }
        }
        return tagString;
    }
}
