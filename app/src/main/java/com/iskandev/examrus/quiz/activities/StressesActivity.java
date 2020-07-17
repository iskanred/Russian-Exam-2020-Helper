package com.iskandev.examrus.quiz.activities;

import android.os.Bundle;
import android.util.Log;

import com.iskandev.examrus.LogTag;
import com.iskandev.examrus.quiz.dataproviders.StressesDataProvider;

public class StressesActivity extends CardQuizActivityTemplate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.quizDataProvider = new StressesDataProvider(getApplicationContext());
        Log.i(LogTag.INFO.toString(), "Data for Stresses-quiz performed");
    }
}
