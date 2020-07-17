package com.iskandev.examrus.quiz.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public final class InputQuizDataUnit extends QuizDataUnit {

    private ArrayList<String> answers;

    InputQuizDataUnit(@NonNull final String quizWord, @NonNull final ArrayList<String> answers) {
        super.quizWord = quizWord;
        this.answers = new ArrayList<>(answers);
    }

    @NonNull
    public ArrayList<String> getAnswers() {
        return new ArrayList<>(answers);
    }
}
