package com.example.androidsecondproject.model;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String id;
    private String firstUid;
    private String secondUid;
    private List<Message> messages = new ArrayList<>();

    public Chat(String id, String firstUid, String secondEmail) {
        this.id = id;
        this.firstUid = firstUid;
        this.secondUid = secondEmail;
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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
