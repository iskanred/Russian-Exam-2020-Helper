package com.iskandev.examrus.quiz.dataproviders;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iskandev.examrus.quiz.data.InputQuizDataUnit;

import java.util.ArrayList;

public abstract class InputQuizDataProvider extends QuizDataProvider {

    private ArrayList<String> remainingAnswers;
    private ArrayList<String> formattedRemainingAnswers;


    InputQuizDataProvider(Context context) {
        super(context);
    }

    @Override
    public void performNextTaskData() throws IndexOutOfBoundsException {
        super.performNextTaskData();
        remainingAnswers = new ArrayList<>( ((InputQuizDataUnit) currentQuizDataUnit).getAnswers() );
        setFormattedRemainingAnswers();
    }

    @Nullable
    public String checkAnswer(@NonNull final String usersAnswer) {
        String formattedUsersAnswer = getFormattedAnswer(usersAnswer);

        if (formattedRemainingAnswers.contains(formattedUsersAnswer)) {
            final String currentAnswer = remainingAnswers.get(formattedRemainingAnswers.indexOf(formattedUsersAnswer));
            formattedRemainingAnswers.remove(formattedUsersAnswer);
            remainingAnswers.remove(currentAnswer);

            return currentAnswer;
        }
        return null;
    }

    @NonNull
    public ArrayList<String> getRemainingAnswers() {
        return remainingAnswers;
    }

    public int getCorrectAnswersCount() {
        return ((InputQuizDataUnit) currentQuizDataUnit).getAnswers().size();
    }

    @NonNull
    private String getFormattedAnswer(@NonNull final String answer) {
        return answer.toLowerCase().replace('ё', 'e');
    }

    private void setFormattedRemainingAnswers() {
        formattedRemainingAnswers = new ArrayList<>();

        for (int answerIndex = 0; answerIndex < remainingAnswers.size(); ++answerIndex) {
            final String answer = remainingAnswers.get(answerIndex);
            final String formattedAnswer = getFormattedAnswer(answer);
            formattedRemainingAnswers.add(formattedAnswer);
        }
    }
}
