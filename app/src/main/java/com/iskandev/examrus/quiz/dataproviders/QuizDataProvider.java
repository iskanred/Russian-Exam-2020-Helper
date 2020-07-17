package com.iskandev.examrus.quiz.dataproviders;

import android.content.Context;

import com.iskandev.examrus.quiz.data.QuizDataUnit;

import java.util.ArrayList;
import java.util.Collections;

public abstract class QuizDataProvider {

    QuizDataUnit currentQuizDataUnit;
    private int currentTaskIndex;

    ArrayList<? extends QuizDataUnit> quizDataOrdered;
    private ArrayList<? extends QuizDataUnit> quizDataShuffled;

    QuizDataProvider(final Context context) {
        currentTaskIndex = -1;
        extractDataFromDatabase(context);
        shuffleQuizData();
    }

    public void performNextTaskData() throws IndexOutOfBoundsException {
        ++currentTaskIndex;
        if (currentTaskIndex >= quizDataShuffled.size()) {
            throw new IndexOutOfBoundsException();
        }
        currentQuizDataUnit = quizDataShuffled.get(currentTaskIndex);
    }

    abstract void extractDataFromDatabase(final Context context);

    private void shuffleQuizData() {
        quizDataShuffled = new ArrayList<>(quizDataOrdered);
        Collections.shuffle(quizDataShuffled);
    }

    public int getTasksCount() {
        return quizDataShuffled.size();
    }

    public String getQuizWord() {
        return currentQuizDataUnit.getQuizWord();
    }
}
