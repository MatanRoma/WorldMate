package com.example.androidsecondproject.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Chat implements Comparable<Chat> {
    private String id;
    private String firstUid;
    private String secondUid;
    private  Message lastMessage;


    public Chat(String id, String firstUid, String secondUid,Message message) {
        this.id = id;
        this.firstUid = firstUid;
        this.secondUid = secondUid;
        this.lastMessage=message;
    }
    public Chat(String id, String firstUid, String secondUid) {
        this.id = id;
        this.firstUid = firstUid;
        this.secondUid = secondUid;

    }

    public Chat() {

    }

    @Override
    public int compareTo(Chat o) {
        Log.d("cmp","cmp");
        return this.getLastMessage().getMessageDate().compareTo(o.getLastMessage().getMessageDate());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstUid() {
        return firstUid;
    }

    public void setFirstUid(String firstUid) {
        this.firstUid = firstUid;
    }

    public String getSecondUid() {
        return secondUid;
    }

    public void setSecondUid(String secondUid) {
        this.secondUid = secondUid;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
