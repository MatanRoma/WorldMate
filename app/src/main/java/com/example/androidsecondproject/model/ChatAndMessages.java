package com.example.androidsecondproject.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ChatAndMessages implements Serializable {
    Chat chat;
    HashMap<String,Message> Messages;

    public ChatAndMessages(Chat chat,HashMap<String,Message> messages) {
        this.chat = chat;
        Messages = messages;
    }

    public ChatAndMessages() {
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public HashMap<String, Message> getMessages() {
        return Messages;
    }

    public void setMessages(HashMap<String,Message> messages) {
        Messages = messages;
    }
}
