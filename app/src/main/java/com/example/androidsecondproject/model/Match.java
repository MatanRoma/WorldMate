package com.example.androidsecondproject.model;

import java.io.Serializable;

public class Match implements Serializable {
    private String email;
    private String id;

    public Match() {
    }

    public Match(String email, String id) {
        this.email = email;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
