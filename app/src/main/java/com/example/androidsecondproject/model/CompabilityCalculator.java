package com.example.androidsecondproject.model;

import java.util.ArrayList;
import java.util.List;

public class CompabilityCalculator {


    private static List<QuestionRespond> createFilteredList(List<String> categories, List<QuestionRespond> myQuestions) {

        List<QuestionRespond> filteredQuestions = new ArrayList<>();
        for (QuestionRespond question : myQuestions) {
            if (categories.contains(question.getCategory())) {
                filteredQuestions.add(question);
            }
        }
        return  filteredQuestions;
    }

    public static int caculateCompability(List<String> categories,List<QuestionRespond> otherQuestions, List<QuestionRespond> myQuestions) {

        int matchedQuestions = 0;
        int matchedAnswer = 0;


        List<QuestionRespond> filteredQuestions  = createFilteredList(categories, myQuestions);

        for (QuestionRespond myQuestion : filteredQuestions) {
            for (QuestionRespond otherQuestion : otherQuestions) {

                if (myQuestion.getId() == otherQuestion.getId()) {
                    matchedQuestions++;
                    {
                        if(myQuestion.getAnswersNum() > 2 &&Math.abs(myQuestion.getResponse() - otherQuestion.getResponse()) <=1)
                        {
                            matchedAnswer++;
                        }
                        else if(myQuestion.getResponse() == otherQuestion.getResponse())
                        {
                            matchedAnswer++;
                        }
                    }
                }
            }

        }
        int compability = 0;
        if(matchedQuestions != 0){
            compability = matchedAnswer * 100/matchedQuestions;
        }
        return compability;
    }
}
