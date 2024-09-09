package com.personal.MyQuizApp;

import android.content.Context;

public class Question {
    String questionText;
    String [] choices;
    int answerIdx;
    public boolean answered = false;
    public boolean revealed = false;
    public int selectedId = -1;


    public Question(Context context, int currIdx){
        String [] questions = context.getResources().getStringArray(R.array.questions);
        this.questionText = questions[currIdx];

        String[] allChoices = context.getResources().getStringArray(R.array.all_choices);
        this.choices = allChoices[currIdx].split(",");

        int[] answerIndices = context.getResources().getIntArray(R.array.answer_indices);
        this.answerIdx = answerIndices[currIdx];
    }

    public String getQuestionText(){
        return questionText;
    }
    public String[] getChoices(){
        return choices;
    }
    public int getAnswerIdx(){
        return answerIdx;
    }
    public String getAnswer(){
        return choices[answerIdx];
    }

}
