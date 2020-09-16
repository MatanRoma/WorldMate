package com.example.androidsecondproject.model;

import java.io.Serializable;

public class AnswerOptions implements Serializable {
    private String option1;
    private String option2;

    public AnswerOptions() {
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }
}
