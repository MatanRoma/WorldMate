package com.example.androidsecondproject.model;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private String sentence;
    //private AnswerOptions answers;
    private List<String> answers;
    private String category;
    private int id;

    public Question() {
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
