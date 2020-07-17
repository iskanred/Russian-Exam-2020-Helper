package com.iskandev.examrus.quiz.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.iskandev.examrus.LogTag;
import com.iskandev.examrus.R;
import com.iskandev.examrus.quiz.dataproviders.CardQuizDataProvider;

public abstract class CardQuizActivityTemplate extends QuizActivityTemplate {


    final static long FREEZING_TIME_IF_SUCCESS = 600; // time to show user correct answer
    final static long FREEZING_TIME_IF_FAILURE = 1400; // time to show user correct answer


    private boolean isOptionCardSelected;

    private Button[] answerOptionsCards;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isOptionCardSelected = false;
    }

    @Override
    void showNextTaskData() {
        quizText.setText(quizDataProvider.getQuizWord());
        fillAnswerOptionsCards();
    }

    public void onOptionCardClick(final View view) {
        if (isOptionCardSelected)
            return;

        isOptionCardSelected = true;

        Log.i(LogTag.INFO.toString(), "answer was selected");
        finishCurrentTask(getSelectedAnswerOptionCardIndex(view.getId()));
    }

    private void finishCurrentTask(final int selectedOptionIndex) {
        isTaskFinishing = true;

        if (selectedOptionIndex == ((CardQuizDataProvider) quizDataProvider).getCorrectOptionIndex())
            showSuccess(selectedOptionIndex);
        else
            showFailure(selectedOptionIndex);
    }

    private void showSuccess(final int selectedOptionIndex) {
        freezeTask(FREEZING_TIME_IF_SUCCESS);
        answerOptionsCards[selectedOptionIndex].
                setBackground(getResources().getDrawable(R.drawable.button_green_state, getTheme()));
        updateTasksCounters(true);
    }

    private void showFailure(final int selectedOptionIndex) {
        freezeTask(FREEZING_TIME_IF_FAILURE);
        answerOptionsCards[selectedOptionIndex].
                setBackground(getResources().getDrawable(R.drawable.button_red_state, getTheme()));
        answerOptionsCards[((CardQuizDataProvider) quizDataProvider).getCorrectOptionIndex()].
                setBackground(getResources().getDrawable(R.drawable.button_green_state, getTheme()));
        updateTasksCounters(false);
    }


    @Override
    void setViewsDefaultState() {
        isOptionCardSelected = false;
        for (Button answerOptionCard : answerOptionsCards) {
            answerOptionCard.setBackground(getResources().getDrawable(R.drawable.button_default_state, getTheme()));
            answerOptionCard.setClickable(true);
        }
    }

    @Override
    void setActivityLayout() {
        setContentView(R.layout.activity_card_quiz);
    }

    @Override
    void loadViewElements() {
        super.loadViewElements();
        quizText = findViewById(R.id.quiz_word);

        answerOptionsCards = new Button[]{
                findViewById(R.id.answer_options_buttons_table).findViewById(R.id.answer_option1_button),
                findViewById(R.id.answer_options_buttons_table).findViewById(R.id.answer_option2_button),
                findViewById(R.id.answer_options_buttons_table).findViewById(R.id.answer_option3_button),
                findViewById(R.id.answer_options_buttons_table).findViewById(R.id.answer_option4_button),
                findViewById(R.id.answer_options_buttons_table).findViewById(R.id.answer_option5_button),
                findViewById(R.id.answer_options_buttons_table).findViewById(R.id.answer_option6_button)
        };
    }



    private void fillAnswerOptionsCards() {
        CardQuizDataProvider cardQuizDataProvider = (CardQuizDataProvider) quizDataProvider;

        for (int optionPos = 0; optionPos < answerOptionsCards.length; ++optionPos) {
            if (optionPos < cardQuizDataProvider.getAnswerOptions().size()) {
                answerOptionsCards[optionPos].setText(cardQuizDataProvider.getAnswerOptions().get(optionPos));
            } else {
                answerOptionsCards[optionPos].setText("");
                answerOptionsCards[optionPos].setClickable(false);
            }
        }
    }

    private int getSelectedAnswerOptionCardIndex(final int selectedOptionId) {
        for (int optionPos = 0; optionPos < answerOptionsCards.length; ++optionPos) {
            if (answerOptionsCards[optionPos].getId() == selectedOptionId) {
                return optionPos;
            }
        }
        throw new ArrayIndexOutOfBoundsException("Selected card index out of bounds");
    }
}
