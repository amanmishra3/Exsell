package com.android.exsell.models;

import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Users {
    String userId;
    String fname;
    String lname;
    String contact;
    String email;
    Date dob;
    List<String> orders;
    List<String> wishlist;
    String rating;
    String school;
    Date createdOn;
    String imageUri;
    public Users() {

    }
    public Users(String name, String school, String email, String uid) {
        String[] fullname = name.split(" ", 2);
        if(fullname.length > 0)
            this.fname = fullname[0];
        else if(fullname.length > 1) {
            this.lname = fullname[1];
        }
        this.school = school;
        this.email = email;
        this.userId = uid;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<String> getWishlist() {
        return wishlist;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getRating() {
        return rating;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setWishlist(List<String> wishlist) {
        this.wishlist = wishlist;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchool() {
        return school;
    }

    public Date getCreatedOn() {
        return createdOn;
    }
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getUserId() {
        return userId;
    }
    public Map<String, Object> userAttributes() {
        Map<String, Object> user = new HashMap<>();
        user.put("fname", fname);
        user.put("lname", lname);
        user.put("contact", contact);
        user.put("school", school);
        user.put("email", email);
        user.put("userId", userId);

        return user;
    }
}

