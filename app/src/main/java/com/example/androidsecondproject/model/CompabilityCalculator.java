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
        final int POINTS_PER_QUESTION_LESS_ANSWERS=5;
        final int POINTS_PER_QUESTION_MORE_ANSWERS=5;
        List<QuestionRespond> filteredQuestions  = createFilteredList(categories, myQuestions);

        for (QuestionRespond myQuestion : filteredQuestions) {
            for (QuestionRespond otherQuestion : otherQuestions) {

                if (myQuestion.getId() == otherQuestion.getId()) {
                    switch (myQuestion.getAnswersNum()) {
                        case 2:
                            matchedQuestions += POINTS_PER_QUESTION_LESS_ANSWERS;
                            if (myQuestion.getResponse() == otherQuestion.getResponse()) {
                                matchedAnswer += POINTS_PER_QUESTION_LESS_ANSWERS;
                            }
                            break;
                        case 3:
                            matchedQuestions += POINTS_PER_QUESTION_LESS_ANSWERS;
                            if (myQuestion.getResponse() == otherQuestion.getResponse()) {
                                matchedAnswer += POINTS_PER_QUESTION_LESS_ANSWERS;
                            } else if (Math.abs(myQuestion.getResponse() - otherQuestion.getResponse()) == 1) {
                                matchedAnswer += 2;
                            }
                            break;
                        case 4:
                            matchedQuestions += POINTS_PER_QUESTION_MORE_ANSWERS;
                            if (myQuestion.getResponse() == otherQuestion.getResponse()) {
                                matchedAnswer += POINTS_PER_QUESTION_MORE_ANSWERS;
                            } else if (Math.abs(myQuestion.getResponse() - otherQuestion.getResponse()) == 1) {
                                matchedAnswer += 5;
                            }
                            break;
                        case 5:
                            matchedQuestions += POINTS_PER_QUESTION_MORE_ANSWERS;
                            if (myQuestion.getResponse() == otherQuestion.getResponse()) {
                                matchedAnswer += POINTS_PER_QUESTION_MORE_ANSWERS;
                            } else if (Math.abs(myQuestion.getResponse() - otherQuestion.getResponse()) == 1) {
                                matchedAnswer += 6;
                            } else if (Math.abs(myQuestion.getResponse() - otherQuestion.getResponse()) == 2) {
                                matchedAnswer += 3;
                            }
                            break;
                    }
                }

            }
        }
        int compability = 0;
        if(matchedQuestions != 0){
            compability = matchedAnswer * 100/matchedQuestions;
        }
        return compability==100?99:compability;
    }
}
 /*if(myQuestion.getAnswersNum() > 2 &&Math.abs(myQuestion.getResponse() - otherQuestion.getResponse()) <=1)
                        {
                            matchedAnswer++;
                        }
                        else if(myQuestion.getResponse() == otherQuestion.getResponse())
                        {
                            matchedAnswer++;
                        }*/