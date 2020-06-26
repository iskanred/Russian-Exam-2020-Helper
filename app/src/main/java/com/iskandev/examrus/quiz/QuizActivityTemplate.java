package com.iskandev.examrus.quiz;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ankushgrover.hourglass.Hourglass;
import com.iskandev.examrus.LogTag;
import com.iskandev.examrus.R;

import java.util.Locale;

public abstract class QuizActivityTemplate extends AppCompatActivity {

    private final static long SHOWING_TIME = 1000;
    private final static long SHOWING_INTERVAL = 500;

    private ExitQuizDialogFragment exitQuizDialogFragment;
    private Hourglass countDownTimer;

    protected TextView remainingTasksCounterText;
    private TextView correctAnswersCounterText;
    private TextView mistakeAnswersCounterText;

    Toolbar toolbar;

    protected abstract void setActivityLayout();

    protected abstract void startQuiz();

    protected abstract void finishQuiz();

    protected abstract void displayNextTaskData();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LogTag.INFO.toString(), "Activity ran");

        setContentView(R.layout.countdown_layout);
        TextView countDownText = findViewById(R.id.countdown_text);

        countDownTimer = new Hourglass(SHOWING_TIME, SHOWING_INTERVAL) {
            @Override
            public void onTimerTick(final long timeRemaining) {
                countDownText.setText(String.format(Locale.getDefault(), "%d", (timeRemaining / SHOWING_INTERVAL + 1)));
            }
            @Override
            public void onTimerFinish() {
                loadViewElements();
                startQuiz();
            }
        };
        countDownTimer.startTimer();

        exitQuizDialogFragment = new ExitQuizDialogFragment();
    }

    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_exam, menu);
        return true;
    }

    @Override
    public final boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public final void onBackPressed() {
        if (countDownTimer != null && countDownTimer.isRunning()) // timer is running even it is paused. It's not running only if it is finished
            super.onBackPressed();
        else
            exitQuizDialogFragment.show(getSupportFragmentManager(), "exitDialog");
    }

    @Override
    protected final void onPause() {
        Log.i(LogTag.INFO.toString(), "ExamActivity paused");
        if (countDownTimer != null && countDownTimer.isRunning())
            countDownTimer.pauseTimer();
        super.onPause();
    }

    @Override
    protected final void onResume() {
        Log.i(LogTag.INFO.toString(), "ExamActivity resumed");
        super.onResume();
        if (countDownTimer != null && countDownTimer.isPaused())
            countDownTimer.resumeTimer();
    }

    protected void startNextTask() {
        Log.i(LogTag.INFO.toString(), "next task was run");
        displayNextTaskData();
    }

    protected void finishCurrentTask(final int selectedOptionIndex) {
        int currentRemainingTasksCounter = Integer.parseInt(remainingTasksCounterText.getText().toString());
        remainingTasksCounterText.setText(String.format(Locale.getDefault(), "%d", --currentRemainingTasksCounter)); // converting Number to String

        Log.i(LogTag.INFO.toString(), "current task was finished");
    }

    protected void displaySuccess(final int selectedOptionIndex) {
        int currentCorrectAnswersCounter = Integer.parseInt(correctAnswersCounterText.getText().toString());
        correctAnswersCounterText.setText(String.format(Locale.getDefault(), "%d", ++currentCorrectAnswersCounter)); // converting Number to String

        Log.i(LogTag.INFO.toString(), "correct answer selected");
    }

    protected void displayFailure(final int selectedOptionIndex) {
        int currentMistakeAnswersCounter = Integer.parseInt(mistakeAnswersCounterText.getText().toString());
        mistakeAnswersCounterText.setText(String.format(Locale.getDefault(), "%d", ++currentMistakeAnswersCounter)); // converting Number to String

        Log.i(LogTag.INFO.toString(), "incorrect answer selected");
    }


    protected void loadViewElements() {
        setActivityLayout();

        toolbar = findViewById(R.id.toolbar_activity_exam);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        remainingTasksCounterText = findViewById(R.id.remaining_counter_text);
        correctAnswersCounterText = findViewById(R.id.correct_counter_text);
        mistakeAnswersCounterText = findViewById(R.id.mistake_counter_text);
    }
}
