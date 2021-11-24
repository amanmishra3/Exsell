package com.android.exsell.models;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Product {
    String title;
    int price;
    String productId;
    int image;
    boolean bargain;
    String buyer;
    String description;
    String seller;
    String status;
    String imageUri;
    String smallImage;
    List<String> tags;
    List<String> categories;
    List<String> search;

    public Product() {};
    public Product(String id, String title, int price, int image, List<String> tags){
        this.productId = id;
        this.title = title;
        this.price = price;
        this.image = image;
        this.tags = tags;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice(){
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getProductId(){
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getImage(){
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<String> getTags(){
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isBargain() {
        return bargain;
    }
    public void setBargain(boolean bargain) {
        this.bargain = bargain;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }
    public String getBuyer() {
        return buyer;
    }
    public void setSeller(String seller) {
        this.seller = seller;
    }
    public String getSeller() {
        return seller;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setSearch(List<String> search) {
        this.search = search;
    }

    public List<String> getSearch() {
        return search;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }
    public Bitmap decodeImage(String imgString) {
        byte[] bytes= Base64.decode(imgString,Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        return bitmap;
    }
    public String encodeImage(Bitmap bitmap, ContentResolver context) {
        String imgString = new String();
//        Uri uri = imgData.getData();
        try {
//            Bitmap bitmap= MediaStore.Images.Media.getBitmap(context,uri);
            // initialize byte stream
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] bytes=stream.toByteArray();
            imgString = Base64.encodeToString(bytes,Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgString;
    }
}
