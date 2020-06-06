package com.iskandev.examrus.stresses;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.iskandev.examrus.ExamActivityTemplate;
import com.iskandev.examrus.LogTag;
import com.iskandev.examrus.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StressesActivity extends ExamActivityTemplate {

    private final static long SHOWING_TIME = 1000; // time to show user correct answer

    private StressesDataPerformer stressesDataPerformer;
    private FreezeTask freezeTask;
    private boolean isOptionCardSelected;

    private int score = 0;

    private TextView quizWordText;
    private TextView scoreText;
    private Button[] answerOptionsCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        score = 0;
        isOptionCardSelected = false;
        stressesDataPerformer = new StressesDataPerformer(getApplicationContext());
        Log.i(LogTag.INFO.toString(), "Data performed");
    }

    @Override
    protected void onDestroy() {
        if (freezeTask != null)
            freezeTask.cancel(true);
        super.onDestroy();
        Log.i(LogTag.INFO.toString(), "StressesActivity was destroyed, its processes was finished");
    }


    /**
     * Freeze answer option selection buttons to show user correct answer
     */
    private final class FreezeTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                TimeUnit.MILLISECONDS.sleep(SHOWING_TIME);
            } catch (InterruptedException ie) {
                Log.e(LogTag.ERROR.toString(), "Interrupted Exception of TimeUnit in FreezeTask");
                ie.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i(LogTag.INFO.toString(), "freezing was stopped");
            startNextTask();
        }
    }


    @Override
    protected void startQuiz() {
        final OnClickListener onClickListener = (view) -> {
            if (isOptionCardSelected) {
                return;
            }

            isOptionCardSelected = true;
            Log.i(LogTag.INFO.toString(), "option card was clicked");

            finishCurrentTask(getSelectedAnswerOptionCardIndex(view.getId()));
            freezeTask = new FreezeTask();
            freezeTask.execute();
        };

        // just sets onClickListener to each optionCard
        for (Button answerOptionsCard : answerOptionsCards)
            answerOptionsCard.setOnClickListener(onClickListener);

        startNextTask();
        updateScoreText();
    }

    @Override
    protected void finishQuiz() {
        Toast.makeText(getApplicationContext(), "СЕССИЯ ПОВТОРЕНИЯ ВЫПОЛНЕНА!", Toast.LENGTH_LONG).show();
        finish();
    }


    private void startNextTask() {
        Log.i(LogTag.INFO.toString(), "next task was run");

        setOptionsButtonsDefaultState();
        try {
            stressesDataPerformer.performNextTaskData();
        } catch (IndexOutOfBoundsException e) {
            finishQuiz();
        }
        displayNextTaskData();
    }

    private void displayNextTaskData() {
        quizWordText.setText(stressesDataPerformer.getQuizWord());
        fillAnswerOptions(stressesDataPerformer.getWordOptions());
    }


    private void finishCurrentTask(final int selectedOptionIndex) {
        if (selectedOptionIndex == stressesDataPerformer.getCorrectOptionIndex())
            displaySuccess(selectedOptionIndex);
        else
            displayFailure(selectedOptionIndex);

        updateScoreText();
        Log.i(LogTag.INFO.toString(), "current task was finished");
    }

    private void displaySuccess(final int selectedOptionIndex) {
        answerOptionsCards[selectedOptionIndex].setBackground(getResources().getDrawable(R.drawable.button_green_state, getTheme()));
        ++score;
    }

    private void displayFailure(final int selectedOptionIndex) {
        answerOptionsCards[selectedOptionIndex].setBackground(getResources().getDrawable(R.drawable.button_red_state, getTheme()));
        answerOptionsCards[stressesDataPerformer.getCorrectOptionIndex()].setBackground(getResources().getDrawable(R.drawable.button_green_state, getTheme()));
        score = 0;
    }


    private void fillAnswerOptions(final ArrayList<String> wordOptions) {
        for (int optionPos = 0; optionPos < answerOptionsCards.length; ++optionPos) {
            if (optionPos < wordOptions.size()) {
                answerOptionsCards[optionPos].setText(wordOptions.get(optionPos));
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
        Log.e(LogTag.ERROR.toString(), "Option cards index out of bounds");
        throw new ArrayIndexOutOfBoundsException("Option index out of bounds");
    }

    private void updateScoreText() {
        scoreText.setText(String.format(Locale.getDefault(), "%s %d", getString(R.string.score_header_text), score));
    }

    private void setOptionsButtonsDefaultState() {
        isOptionCardSelected = false;
        for (Button answerOptionCard : answerOptionsCards) {
            answerOptionCard.setBackground(getResources().getDrawable(R.drawable.button_default_state, getTheme()));
            answerOptionCard.setClickable(true);
        }
    }


    @Override
    protected void loadViewElements() {
        super.loadViewElements();
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
