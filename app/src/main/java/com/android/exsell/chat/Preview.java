package com.android.exsell.chat;

import android.media.Image;

import java.util.Calendar;

public class Preview {
    String name;
    String message;
    Calendar timeStamp;
    Image profilePic;

    public Preview() {
    }

    public Preview(String name, String message, Calendar timeStamp, Image profilePic) {
        this.name = name;
        this.message = message;
        this.timeStamp = timeStamp;
        this.profilePic = profilePic;
    }
}
