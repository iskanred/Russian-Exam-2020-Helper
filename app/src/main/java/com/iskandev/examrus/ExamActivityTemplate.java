package com.iskandev.examrus;

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
import com.iskandev.examrus.stresses.StressesActivity;

import java.util.Locale;

public abstract class ExamActivityTemplate extends AppCompatActivity {

    private final static long SHOWING_TIME = 1000;
    private final static long SHOWING_INTERVAL = 500;

    private ExitExamDialogFragment exitExamDialogFragment;
    private Hourglass countDownTimer;

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LogTag.INFO.toString(), "Activity ran");

        setContentView(R.layout.countdown_layout);
        final TextView countDownText = findViewById(R.id.countdown_text);

        countDownTimer = new Hourglass(SHOWING_TIME, SHOWING_INTERVAL) {
            @Override
            public void onTimerTick(long timeRemaining) {
                countDownText.setText(String.format(Locale.getDefault(), "%d", (timeRemaining / SHOWING_INTERVAL + 1)));
            }

            @Override
            public void onTimerFinish() {
                setContentView(getChildLayoutID());
                loadViewElements();
                startQuiz();
            }
        };
        countDownTimer.startTimer();

        exitExamDialogFragment = new ExitExamDialogFragment();
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
    public void onBackPressed() {
        if (countDownTimer != null && countDownTimer.isRunning()) // timer is running even it is paused. It's not running only if it is finished
            super.onBackPressed();
        else
            exitExamDialogFragment.show(getSupportFragmentManager(), "exitDialog");
    }

    @Override
    protected void onPause() {
        Log.i(LogTag.INFO.toString(), "ExamActivity paused");
        if (countDownTimer != null && countDownTimer.isRunning())
            countDownTimer.pauseTimer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i(LogTag.INFO.toString(), "ExamActivity resumed");
        super.onResume();
        if (countDownTimer != null && countDownTimer.isPaused())
            countDownTimer.resumeTimer();
    }


    private int getChildLayoutID() {
        if (this.getClass() == StressesActivity.class)
            return R.layout.activity_stresses;
        else if (this.getClass() == ParonymsActivity.class)
            return R.layout.activity_paronyms;
        else
            throw new IllegalStateException("it's forbidden to create only ExamActivityTemplate-instance, only with help of children");
    }


    protected void loadViewElements() {
        toolbar = findViewById(R.id.toolbar_activity_exam);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    protected abstract void startQuiz();

    protected abstract void finishQuiz();
}
