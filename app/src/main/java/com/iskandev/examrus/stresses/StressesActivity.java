package com.iskandev.examrus.stresses;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.iskandev.examrus.ExamActivityTemplate;
import com.iskandev.examrus.R;

import java.util.Locale;

public class StressesActivity extends ExamActivityTemplate {

    private final long SHOWING_TIME = 1200; // milliseconds
    private final String LOG_TAG = "log_tag";

    private TextView quizWordText;
    private TextView scoreText;
    private MaterialCardView[] answerOptionsCards;

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
        for (MaterialCardView answerOptionsCard : answerOptionsCards)
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
        for (MaterialCardView answerOptionCard : answerOptionsCards) {
            answerOptionCard.setBackgroundColor(getResources().getColor
                    (com.google.android.material.R.color.cardview_dark_background, getTheme()));
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
        answerOptionsCards[selectedOptionIndex].setBackgroundColor(getResources().getColor(R.color.ColorGreenRightAnswer, getTheme()));
        ++score;
    }



    private void displayFailure(final int selectedOptionIndex) {
        answerOptionsCards[selectedOptionIndex].setBackgroundColor(getResources().getColor(R.color.ColorRedWrongAnswer, getTheme()));
        answerOptionsCards[currentWordStress.getRightOptionIndex()].setBackgroundColor(getResources().getColor(R.color.ColorGreenRightAnswer, getTheme()));
        score = 0;
    }



    private void setScoreText() {
        scoreText.setText(String.format(Locale.getDefault(), "%s %d",
                getString(R.string.score_header_text), score));
    }



    private void setOptionsCardsEnabled(final boolean enabledState) {
        for (MaterialCardView answerOptionCard : answerOptionsCards) {
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
            TextView optionText = answerOptionsCards[optionPos].findViewById(R.id.option_text);

            if (optionPos < currentWordStress.getWordOptions().size())
                optionText.setText(currentWordStress.getWordOptions().get(optionPos));
            else {
                optionText.setText("");
                answerOptionsCards[optionPos].setEnabled(false);
            }
        }
    }



    @Override
    protected void loadViewElements() {
        quizWordText = findViewById(R.id.quiz_word);
        scoreText = findViewById(R.id.score_text);

        answerOptionsCards = new MaterialCardView[]{
                findViewById(R.id.answer_options_cards_table).findViewById(R.id.answer_option1_card),
                findViewById(R.id.answer_options_cards_table).findViewById(R.id.answer_option2_card),
                findViewById(R.id.answer_options_cards_table).findViewById(R.id.answer_option3_card),
                findViewById(R.id.answer_options_cards_table).findViewById(R.id.answer_option4_card),
                findViewById(R.id.answer_options_cards_table).findViewById(R.id.answer_option5_card),
                findViewById(R.id.answer_options_cards_table).findViewById(R.id.answer_option6_card)
        };
    }
}
