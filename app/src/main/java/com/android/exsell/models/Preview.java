package com.android.exsell.models;

import android.media.Image;

import java.util.Calendar;

public class Preview {
    String messageId;
    String name;
    String message;
    Calendar timeStamp;
    Image profilePic;

    public Preview(String messageId, String name, String message, Calendar timeStamp, Image profilePic) {
        this.messageId = messageId;
        this.name = name;
        this.message = message;
        this.timeStamp = timeStamp;
        this.profilePic = profilePic;
    }

    public Preview() {

    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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
