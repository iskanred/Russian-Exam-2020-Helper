package com.iskandev.examrus.quiz.dataproviders;

import android.content.Context;

import com.iskandev.examrus.quiz.data.CardQuizDataUnit;

import java.util.ArrayList;

public abstract class CardQuizDataProvider extends QuizDataProvider{


    CardQuizDataProvider(Context context) {
        super(context);
    }

    public ArrayList<String> getAnswerOptions() {
        return new ArrayList<>( ((CardQuizDataUnit) currentQuizDataUnit).getAnswerOptions() );
    }

    public int getCorrectOptionIndex () {
        return ((CardQuizDataUnit) currentQuizDataUnit).getCorrectOptionIndex();
    }
}



