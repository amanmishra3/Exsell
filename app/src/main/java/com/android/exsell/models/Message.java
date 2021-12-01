package com.android.exsell.models;

import android.util.Log;

import java.util.Calendar;

public class Message {
    String message;
    String sender; // 0: other, 1: self
    Calendar timeStamp;
    String messageId;

    public Message(String message, String senderUid, Calendar timeStamp) {
        this.message = message;
        this.sender = senderUid;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Calendar getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Calendar timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public boolean isSame(Message message) {
        boolean result = this.sender.compareTo(message.getSender()) == 0
                && this.message.compareTo(message.getMessage()) == 0
                && this.timeStamp.getTime().toString().compareTo(message.getTimeStamp().getTime().toString()) == 0;
        return result;
    }
}
