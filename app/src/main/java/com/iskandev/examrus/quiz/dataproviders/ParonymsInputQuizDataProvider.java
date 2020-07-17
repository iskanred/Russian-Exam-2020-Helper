package com.iskandev.examrus.quiz.dataproviders;


import android.content.Context;
import android.util.Log;

import com.iskandev.examrus.LogTag;
import com.iskandev.examrus.quiz.data.DatabaseHelper;

public class ParonymsInputQuizDataProvider extends InputQuizDataProvider {

    public ParonymsInputQuizDataProvider(Context context) {
        super(context);
    }

    @Override
    void extractDataFromDatabase(Context context) {
        final DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.openDataBase();
        super.quizDataOrdered = databaseHelper.getParonymsCardQuizData();
        databaseHelper.close();
        Log.i(LogTag.INFO.toString(), "Paronyms Input Quiz Data was extracted successfully");
    }
}
