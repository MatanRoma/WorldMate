package com.example.androidsecondproject.model;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    private String senderUid;
    private String text;
    private Date messageDate;

    public Message(String senderUid, String text) {
        this.senderUid = senderUid;
        this.text = text;
        messageDate=new Date();
    }

    public Message() {
    }
    @SuppressLint("SimpleDateFormat")
    public String getFormattedDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm | dd/MM");
        return simpleDateFormat.format(messageDate);
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
