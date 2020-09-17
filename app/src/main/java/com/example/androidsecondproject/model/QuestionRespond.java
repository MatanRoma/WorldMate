package com.example.androidsecondproject.model;

import java.io.Serializable;

public class QuestionRespond implements Serializable {
    private int id;
    private int response;
    private String category;

    public QuestionRespond() {
    }

    public QuestionRespond(int id, int response,String category) {
        this.id = id;
        this.response = response;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}


