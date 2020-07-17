package com.iskandev.examrus.quiz.dataproviders;

import android.content.Context;
import android.util.Log;

import com.iskandev.examrus.LogTag;
import com.iskandev.examrus.quiz.data.DatabaseHelper;

public final class StressesDataProvider extends CardQuizDataProvider {

    public StressesDataProvider(Context context) {
        super(context);
    }

    @Override
    void extractDataFromDatabase(Context context) {
        final DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.openDataBase();
        super.quizDataOrdered = databaseHelper.getStressesCardQuizData();
        databaseHelper.close();
        Log.i(LogTag.INFO.toString(), "Stresses Cards Data was extracted successfully");
    }
}
