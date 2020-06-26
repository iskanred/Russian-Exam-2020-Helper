package com.iskandev.examrus.quiz;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;

class QuizTaskData {

    private String quizWord;

    private int correctOptionIndex;

    /**
     * {@code answerOptions} options of answer. Will be shuffled.
     */
    private ArrayList<String> answerOptions;


    /**
     *
     * @param quizWord is question text
     * @param answerOptions options of answer. Right answer must be the zero-element in {@code answerOptions}
     */
    QuizTaskData(@NonNull final String quizWord, @NonNull final ArrayList<String> answerOptions) {
        if (answerOptions.isEmpty() || quizWord.isEmpty())
            throw new IllegalArgumentException("Empty answer options or quiz word");

        this.quizWord = quizWord;
        this.answerOptions = new ArrayList<>(answerOptions);
        shuffleAnswerOptions();
    }

    private void shuffleAnswerOptions() {
        final String rightAnswerOption = answerOptions.get(0);
        Collections.shuffle(answerOptions);
        correctOptionIndex = answerOptions.indexOf(rightAnswerOption);
    }

    @NonNull
    String getQuizWord() {
        return quizWord;
    }

    @NonNull
    ArrayList<String> getAnswerOptions() {
        return answerOptions;
    }

    int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}
