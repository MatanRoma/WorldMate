package com.example.androidsecondproject.model;

import java.util.ArrayList;
import java.util.List;

public class CompabilityCalculator {
    private List<String> categories;
    private List<QuestionRespond> myAnsweredQuestions;
    private List<QuestionRespond> otherAnsweredQuestions;
    private List<List<String>> categoriesList;
    private List<QuestionRespond> filteredQuestions;
    private int compability;

    public CompabilityCalculator(List<String> categories, List<QuestionRespond> myQuestions, List<QuestionRespond> otherQuestions) {
        this.categories = categories;
        this.myAnsweredQuestions = myQuestions;
        this.otherAnsweredQuestions = otherQuestions;
        caculateCompability();
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<QuestionRespond> getQuestions() {
        return myAnsweredQuestions;
    }

    public void setQuestions(List<QuestionRespond> questions) {
        this.myAnsweredQuestions = questions;
    }

    public int getCompability() {
        return compability;
    }

    public List<List<String>> getCategoriesList() {
        return categoriesList;
    }

    public void setCategoriesList(List<List<String>> categoriesList) {
        this.categoriesList = categoriesList;
    }

    public void setCompability(int compability) {
        this.compability = compability;
    }

    private void createFilteredList() {

        filteredQuestions = new ArrayList<>();
        for (QuestionRespond question : myAnsweredQuestions) {
            if (categories.contains(question.getCategory())) {
                filteredQuestions.add(question);
            }
        }
    }

    public int caculateCompability() {
        int matchedQuestions = 0;
        int matchedAnswer = 0;
        createFilteredList();

        for (QuestionRespond myQuestion : filteredQuestions) {
            for (QuestionRespond otherQuestion : otherAnsweredQuestions) {

                if (myQuestion.getId() == otherQuestion.getId()) {
                    matchedQuestions++;

                    {
                        if(myQuestion.getResponse() == otherQuestion.getResponse())
                        {
                            matchedAnswer++;
                        }
                    }
                }
            }

        }

        if(matchedQuestions != 0){
            compability = matchedAnswer * 100/matchedQuestions;
        }
        return compability;
    }
}
