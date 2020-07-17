package com.iskandev.examrus.quiz.activities;

import android.os.Bundle;
import android.util.Log;

import com.iskandev.examrus.LogTag;
import com.iskandev.examrus.quiz.dataproviders.ParonymsInputQuizDataProvider;


public class ParonymsInputQuizActivity extends InputQuizActivityTemplate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.quizDataProvider = new ParonymsInputQuizDataProvider(getApplicationContext());
        Log.i(LogTag.INFO.toString(), "Data for Paronyms-input-quiz performed");
    }
}
