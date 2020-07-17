package com.iskandev.examrus.quiz.activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.iskandev.examrus.LogTag;
import com.iskandev.examrus.R;
import com.iskandev.examrus.quiz.dataproviders.InputQuizDataProvider;

import java.util.Iterator;

public abstract class InputQuizActivityTemplate extends QuizActivityTemplate {


    private class InputFieldListener implements TextWatcher {
        private int inputFieldIndex;

        InputFieldListener(final int inputFieldIndex) {
            this.inputFieldIndex = inputFieldIndex;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (!shouldInputFieldListenerCheckText || isTaskFinishing || s == null || s.toString().isEmpty())
                return;

            final String checkedAnswer = ((InputQuizDataProvider) quizDataProvider).checkAnswer(s.toString());

            if (checkedAnswer == null)
                return;

            showAnswerCorrect(inputFieldIndex, checkedAnswer);

            if (((InputQuizDataProvider) quizDataProvider).getRemainingAnswers().isEmpty())
                finishCurrentTask(true);
            else
                focusOnNextInputField();
        }

        @Override
        public void afterTextChanged(Editable s) { }
    }



    final static long FREEZING_TIME_IF_SUCCESS = 1000; // time to show user correct answer
    final static long FREEZING_TIME_IF_FAILURE = 2000; // time to show user correct answer


    private boolean shouldInputFieldListenerCheckText;


    private CardView[] inputFieldsCards;
    private EditText[] inputFields;
    private Button skipTaskButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void showNextTaskData() {
        quizText.setText(quizDataProvider.getQuizWord());
        disableExcessInputFields();
    }

    public void onSkipTaskButtonClick(final View view) {

        skipTaskButton.setClickable(false);
        finishCurrentTask(false);
    }

    void finishCurrentTask(final boolean isAnswerCorrect) {
        isTaskFinishing = true;

        if (isAnswerCorrect) {
            Log.i(LogTag.INFO.toString(), "task is succeed");
            freezeTask(FREEZING_TIME_IF_SUCCESS);
        }
        else {
            Log.i(LogTag.INFO.toString(), "task is failed");
            showAnswers();
            freezeTask(FREEZING_TIME_IF_FAILURE);
        }

        updateTasksCounters(isAnswerCorrect);
    }


    @Override
    void setViewsDefaultState() {
        skipTaskButton.setClickable(true);
        for (int inputFieldIndex = 0; inputFieldIndex < inputFields.length; ++inputFieldIndex) {
            inputFieldsCards[inputFieldIndex].setBackground(getResources().getDrawable(R.drawable.button_default_state, getTheme()));

            shouldInputFieldListenerCheckText = false;
            inputFields[inputFieldIndex].setText("");
            shouldInputFieldListenerCheckText = true;

            setInputFieldEnabled(inputFieldIndex, true);
        }
        shouldInputFieldListenerCheckText = true;
    }

    @Override
    void setActivityLayout() {
        setContentView(R.layout.activity_input_quiz);
    }

    @Override
    void loadViewElements() {
        super.loadViewElements();
        quizText = findViewById(R.id.quiz_word);
        skipTaskButton = findViewById(R.id.not_know_button);

        inputFieldsCards = new CardView[] {
                findViewById(R.id.input_field_card1),
                findViewById(R.id.input_field_card2),
                findViewById(R.id.input_field_card3),
                findViewById(R.id.input_field_card4),
                findViewById(R.id.input_field_card5),
        };

        inputFields = new EditText[] {
                inputFieldsCards[0].findViewById(R.id.input_field_text1),
                inputFieldsCards[1].findViewById(R.id.input_field_text2),
                inputFieldsCards[2].findViewById(R.id.input_field_text3),
                inputFieldsCards[3].findViewById(R.id.input_field_text4),
                inputFieldsCards[4].findViewById(R.id.input_field_text5),
        };

        InputFieldListener[] inputFieldsListeners = new InputFieldListener[]{
                new InputFieldListener(0),
                new InputFieldListener(1),
                new InputFieldListener(2),
                new InputFieldListener(3),
                new InputFieldListener(4),
        };

        for (int inputFieldIndex = 0; inputFieldIndex < inputFields.length; ++inputFieldIndex) {
            inputFields[inputFieldIndex].addTextChangedListener(inputFieldsListeners[inputFieldIndex]);
        }
    }



    private void showAnswerCorrect(final int inputFieldIndex, @NonNull final String correctAnswer) {
        setInputFieldEnabled(inputFieldIndex, false);

        shouldInputFieldListenerCheckText = false;
        inputFields[inputFieldIndex].setText(correctAnswer);
        shouldInputFieldListenerCheckText = true;

        inputFieldsCards[inputFieldIndex].
                setBackground(getResources().getDrawable(R.drawable.button_green_state, getTheme()));

        Log.i(LogTag.INFO.toString(), "correct answer was input");
    }

    private void showAnswers() {
        final Iterator <String> remainingCorrectAnswersIterator =
                ((InputQuizDataProvider) quizDataProvider).getRemainingAnswers().iterator();

        for (int inputFieldIndex = 0; inputFieldIndex < inputFields.length; ++inputFieldIndex) {
            if (inputFields[inputFieldIndex].isEnabled()) {

                shouldInputFieldListenerCheckText = false;
                inputFields[inputFieldIndex].setText(remainingCorrectAnswersIterator.next());
                shouldInputFieldListenerCheckText = true;

                setInputFieldEnabled(inputFieldIndex, false);
                inputFieldsCards[inputFieldIndex].
                        setBackground(getResources().getDrawable(R.drawable.button_red_state, getTheme()));
            }
        }
    }

    private void disableExcessInputFields() {
        final int correctAnswersCount = ((InputQuizDataProvider) quizDataProvider).getCorrectAnswersCount();

        for (int inputFieldIndex = correctAnswersCount; inputFieldIndex < inputFields.length; ++inputFieldIndex) {
            setInputFieldEnabled(inputFieldIndex, false);
        }
    }

    private void setInputFieldEnabled(final int inputFieldIndex, final boolean enabled) {
        inputFields[inputFieldIndex].setClickable(enabled);
        inputFields[inputFieldIndex].setFocusableInTouchMode(enabled);
        inputFields[inputFieldIndex].setFocusable(enabled);
        inputFields[inputFieldIndex].setEnabled(enabled);
        if (enabled) {
            inputFields[inputFieldIndex].setHint(R.string.paronyms_input_field_hint);
        }
        else {
            inputFields[inputFieldIndex].clearComposingText();
            inputFields[inputFieldIndex].setHint("");
        }
    }

    private void focusOnNextInputField() {
        for (EditText inputField : inputFields) {
            if (inputField.isEnabled()) {
                inputField.requestFocus();

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.showSoftInput(inputField, InputMethodManager.SHOW_IMPLICIT);

                break;
            }
        }
    }
}
