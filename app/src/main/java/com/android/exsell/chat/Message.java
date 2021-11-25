package com.android.exsell.chat;

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
}
