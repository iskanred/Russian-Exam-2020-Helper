package com.iskandev.examrus.quiz;

import android.content.Context;
import android.util.Log;

import com.iskandev.examrus.LogTag;

import java.util.ArrayList;
import java.util.Collections;

final class QuizDataProvider {

    private int currentTaskIndex;
    private QuizTaskData currentTaskData;

    private ArrayList<QuizTaskData> quizDataOrdered;
    private ArrayList<QuizTaskData> quizDataShuffled;

    QuizDataProvider(final Context context) {
        currentTaskIndex = 0;
        extractDataFromDatabase(context);
        shuffleQuizData();
    }

    /**
     *
     * @throws IndexOutOfBoundsException if tasks is run out
     */
    void performNextTaskData() throws IndexOutOfBoundsException {
        currentTaskData = quizDataShuffled.get(currentTaskIndex);
        ++currentTaskIndex;
    }

    private void extractDataFromDatabase(final Context context) {
        final DatabaseHelper databaseHelper = new DatabaseHelper(context);
        databaseHelper.openDataBase();
        quizDataOrdered = databaseHelper.getStressesData();
        Log.i(LogTag.INFO.toString(), "Stresses words extracted from database");
    }

    private void shuffleQuizData() {
        quizDataShuffled = new ArrayList<>(quizDataOrdered);
        Collections.shuffle(quizDataShuffled);
    }

    int getTasksCount() {
        return quizDataShuffled.size();
    }

    String getQuizWord() {
        return currentTaskData.getQuizWord();
    }

    ArrayList<String> getAnswerOptions() {
        return currentTaskData.getAnswerOptions();
    }

    int getCorrectOptionIndex() {
        return currentTaskData.getCorrectOptionIndex();
    }
}