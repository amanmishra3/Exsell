package com.android.exsell.models;

import android.media.Image;

import java.util.Calendar;

public class Preview {
    String uid;
    String name;
    String message;
    Calendar timeStamp;
    Image profilePic;

    public Preview(String uid, String name, String message, Calendar timeStamp, Image profilePic) {
        this.uid = uid;
        this.name = name;
        this.message = message;
        this.timeStamp = timeStamp;
        this.profilePic = profilePic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Calendar getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Calendar timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Image getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Image profilePic) {
        this.profilePic = profilePic;
    }
}
