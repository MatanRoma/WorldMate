package com.example.androidsecondproject.model;

import java.io.Serializable;

public class Question implements Serializable {
    private String sentence;
    private AnswerOptions answers;

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
}
