package com.iskandev.examrus.stresses;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iskandev.examrus.ExamActivityTemplate;
import com.iskandev.examrus.R;

import java.util.Locale;

public class StressesActivity extends ExamActivityTemplate {

    private final long SHOWING_TIME = 1400; // milliseconds
    private final String LOG_TAG = "log_tag";

    private TextView quizWordText;
    private TextView scoreText;
    private Button[] answerOptionsCards;

    private WordStress currentWordStress;
    private int score = 0;
    private int sessionTaskNumber;

    final StressesDataPerformer stressesDataPerformer = new StressesDataPerformer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runActivity(R.layout.activity_stresses);
        score = 0;
        sessionTaskNumber = 0;
    }

    @Override
    protected void runExam() {
        final OnClickListener onClickListener = (view) -> {
            Log.i(LOG_TAG, "answer option was clicked");
            // CountDown needs to show if whether the selected answer option is right
            new CountDownTimer(SHOWING_TIME, SHOWING_TIME) {
                @Override
                public void onTick(long millisUntilFinished) {
                    finishCurrentTask(getSelectedAnswerOptionCardIndex(view.getId()));
                }

                @Override
                public void onFinish() {
                    runNextTask();
                }
            }.start();
        };

        // just sets onClickListener to each optionCard
        for (Button answerOptionsCard : answerOptionsCards)
            answerOptionsCard.setOnClickListener(onClickListener);

        runNextTask();
        setScoreText();
    }



    private void runNextTask() {
        Log.i(LOG_TAG, "next task was run");

        if (sessionTaskNumber >= StressesDataPerformer.WORDS_AMOUNT) {
            Toast.makeText(getApplicationContext(), "!", Toast.LENGTH_SHORT).show();
            stressesDataPerformer.update();
            sessionTaskNumber %= StressesDataPerformer.WORDS_AMOUNT;
        }

        setOptionsCardsDefaultState();
        getNextTaskData();
        displayNextTaskData();
    }


    private void setOptionsCardsDefaultState() {
        setOptionsCardsEnabled(true);
        for (Button answerOptionCard : answerOptionsCards) {
            answerOptionCard.setBackground(getResources().getDrawable(R.drawable.button_default_state, getTheme()));
        }
    }


    private void finishCurrentTask(final int selectedOptionIndex) {
        setOptionsCardsEnabled(false);

        if (selectedOptionIndex == currentWordStress.getRightOptionIndex())
            displaySuccess(selectedOptionIndex);
        else
            displayFailure(selectedOptionIndex);

        ++sessionTaskNumber;
        setScoreText();
    }


    private void displaySuccess(final int selectedOptionIndex) {
        answerOptionsCards[selectedOptionIndex].setBackground(getResources().getDrawable(R.drawable.button_green_state, getTheme()));
        ++score;
    }


    private void displayFailure(final int selectedOptionIndex) {
        answerOptionsCards[selectedOptionIndex].setBackground(getResources().getDrawable(R.drawable.button_red_state, getTheme()));
        answerOptionsCards[currentWordStress.getRightOptionIndex()].setBackground(getResources().getDrawable(R.drawable.button_green_state, getTheme()));
        score = 0;
    }


    private void setScoreText() {
        scoreText.setText(String.format(Locale.getDefault(),
                "%s %d", getString(R.string.score_header_text), score));
    }


    private void setOptionsCardsEnabled(final boolean enabledState) {
        for (Button answerOptionCard : answerOptionsCards) {
            answerOptionCard.setEnabled(enabledState);
        }
    }


    private void getNextTaskData() {
        currentWordStress = stressesDataPerformer.getWordStress(sessionTaskNumber);
    }


    private void displayNextTaskData() {
        quizWordText.setText(currentWordStress.getWordLowerCase());
        fillAnswerOptions();
    }


    private int getSelectedAnswerOptionCardIndex(final int selectedOptionId) {
        for (int optionPos = 0; optionPos < answerOptionsCards.length; ++optionPos) {
            if (answerOptionsCards[optionPos].getId() == selectedOptionId) {
                return optionPos;
            }
        }
        throw new ArrayIndexOutOfBoundsException("Option index out of bounds");
    }


    private void fillAnswerOptions() {
        for (int optionPos = 0; optionPos < answerOptionsCards.length; ++optionPos) {
            if (optionPos < currentWordStress.getWordOptions().size())
                answerOptionsCards[optionPos].setText(currentWordStress.getWordOptions().get(optionPos));
            else {
                answerOptionsCards[optionPos].setText("");
                answerOptionsCards[optionPos].setEnabled(false);
            }
        }
    }


    @Override
    protected void loadViewElements() {
        quizWordText = findViewById(R.id.quiz_word);
        scoreText = findViewById(R.id.score_text);

        answerOptionsCards = new Button[]{
                findViewById(R.id.answer_options_buttons_table).findViewById(R.id.answer_option1_button),
                findViewById(R.id.answer_options_buttons_table).findViewById(R.id.answer_option2_button),
                findViewById(R.id.answer_options_buttons_table).findViewById(R.id.answer_option3_button),
                findViewById(R.id.answer_options_buttons_table).findViewById(R.id.answer_option4_button),
                findViewById(R.id.answer_options_buttons_table).findViewById(R.id.answer_option5_button),
                findViewById(R.id.answer_options_buttons_table).findViewById(R.id.answer_option6_button)
        };
    }
}
