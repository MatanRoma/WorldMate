package com.example.androidsecondproject.model;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private String id;
    private String firstEmail;
    private String secondEmail;
    private List<Message> messages = new ArrayList<>();

    public Chat(String id, String firstEmail, String secondEmail) {
        this.id = id;
        this.firstEmail = firstEmail;
        this.secondEmail = secondEmail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstEmail() {
        return firstEmail;
    }

    public void setFirstEmail(String firstEmail) {
        this.firstEmail = firstEmail;
    }

    public String getSecondEmail() {
        return secondEmail;
    }

    public void setSecondEmail(String secondEmail) {
        this.secondEmail = secondEmail;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
