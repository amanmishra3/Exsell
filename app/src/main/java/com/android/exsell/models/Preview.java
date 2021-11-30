package com.android.exsell.models;

import android.media.Image;

import java.util.Calendar;

public class Preview {
    String messageId;
    String name;
    String message;
    Calendar timeStamp;
    String profilePic;

    public Preview(String messageId, String name, String message, Calendar timeStamp, String profilePic) {
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public boolean isSame(Preview preview) {
        boolean result = this.messageId.compareTo(preview.getMessageId()) == 0
                && this.name.compareTo(preview.getName()) == 0
                && this.message.compareTo(preview.getMessage()) == 0
                && this.timeStamp.getTime().toString().compareTo(preview.getTimeStamp().getTime().toString()) == 0;
        return result;
    }
}
