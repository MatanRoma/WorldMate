package com.example.androidsecondproject.model;

import java.io.Serializable;

public class Message implements Serializable {
    private String senderEmail;
    private String text;

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
