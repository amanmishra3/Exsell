package com.android.exsell.models;

import java.util.Calendar;

public class Message {
    String message;
    int sender; // 0: other, 1: self
    Calendar timeStamp;

    public Message(String message, int sender, Calendar timeStamp) {
        this.message = message;
        this.sender = sender;
        this.timeStamp = timeStamp;
    }

    public Message() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public Calendar getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Calendar timeStamp) {
        this.timeStamp = timeStamp;
    }
}
