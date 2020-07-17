package com.iskandev.examrus.quiz.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.iskandev.examrus.LogTag;
import com.iskandev.examrus.quiz.dataproviders.StressesDataProvider;

public class ParonymsCardQuizActivity extends CardQuizActivityTemplate {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toast.makeText(getApplicationContext(), "Извините, раздел в разработке", Toast.LENGTH_SHORT).show();
        super.quizDataProvider = new StressesDataProvider(getApplicationContext());
        finish();
        Log.i(LogTag.INFO.toString(), "Data for Paronyms-quiz performed");
    }
}
