package com.iskandev.examrus.quiz.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;

public final class CardQuizDataUnit extends QuizDataUnit {

    /**
     * {@code answerOptions} options of answer. Will be shuffled.
     */
    private ArrayList<String> answerOptions;

    /**
     * {@code answerOptions} index of correct answer option in {@code answerOptions}.
     */
    private int correctOptionIndex;


    /**
     *
     * @param quizWord is question text
     * @param answerOptions options of answer. Correct answer must be the zero-element in {@code answerOptions}
     */
    CardQuizDataUnit(@NonNull final String quizWord, @NonNull final ArrayList<String> answerOptions) {
        if (answerOptions.isEmpty() || quizWord.isEmpty())
            throw new IllegalArgumentException("Empty answer options or quiz word");

        super.quizWord = quizWord;
        this.answerOptions = new ArrayList<>(answerOptions);
        shuffleAnswerOptions();
    }

    private void shuffleAnswerOptions() {
        final String rightAnswerOption = answerOptions.get(0);
        Collections.shuffle(answerOptions);
        correctOptionIndex = answerOptions.indexOf(rightAnswerOption);
    }

    @NonNull
    public ArrayList<String> getAnswerOptions() {
        return answerOptions;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}
