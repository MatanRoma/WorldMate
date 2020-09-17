package com.example.androidsecondproject.model;

import java.io.Serializable;

public class Question implements Serializable {
    private String sentence;
    private AnswerOptions answers;
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

    public AnswerOptions getAnswers() {
        return answers;
    }

    public void setAnswers(AnswerOptions answers) {
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
