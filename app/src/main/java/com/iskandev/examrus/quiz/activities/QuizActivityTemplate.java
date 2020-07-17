package com.iskandev.examrus.quiz.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ankushgrover.hourglass.Hourglass;
import com.iskandev.examrus.LogTag;
import com.iskandev.examrus.R;
import com.iskandev.examrus.quiz.ExitQuizDialogFragment;
import com.iskandev.examrus.quiz.dataproviders.QuizDataProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public abstract class QuizActivityTemplate extends AppCompatActivity {

    /**
     * Freeze answer option selection buttons to show user correct answer
     */
    private final class FreezeTask extends AsyncTask<Void, Void, Void> {
        private long freezingTime;

        FreezeTask(final long freezingTime) {
            super();
            this.freezingTime = freezingTime;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                TimeUnit.MILLISECONDS.sleep(freezingTime);
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



    private final static long SHOWING_COUNTDOWN_TIME = 1000;
    private final static long SHOWING_COUNTDOWN_INTERVAL = 500;


    private ExitQuizDialogFragment exitQuizDialogFragment;
    private Hourglass countDownTimer;
    private FreezeTask freezeTask;

    private TextView remainingTasksCounterText;
    private TextView correctAnswersCounterText;
    private TextView mistakeAnswersCounterText;

    Toolbar toolbar;
    TextView quizText;

    QuizDataProvider quizDataProvider;

    boolean isTaskFinishing;


    abstract void showNextTaskData();
    abstract void setViewsDefaultState();
    abstract void setActivityLayout();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LogTag.INFO.toString(), "QuizActivity was started");

        isTaskFinishing = false;

        setContentView(R.layout.countdown_layout);
        TextView countDownText = findViewById(R.id.countdown_text);

        countDownTimer = new Hourglass(SHOWING_COUNTDOWN_TIME, SHOWING_COUNTDOWN_INTERVAL) {
            @Override
            public void onTimerTick(final long timeRemaining) {
                countDownText.setText(String.format(Locale.getDefault(), "%d", (timeRemaining / SHOWING_COUNTDOWN_INTERVAL + 1)));
            }
            @Override
            public void onTimerFinish() {
                setActivityLayout();
                loadViewElements();
                startNextTask();
            }
        };
        countDownTimer.startTimer();

        exitQuizDialogFragment = new ExitQuizDialogFragment();
    }

    @Override
    protected void onDestroy() {
        if (freezeTask != null)
            freezeTask.cancel(true);
        if (countDownTimer != null && countDownTimer.isRunning())
            countDownTimer.stopTimer();
        super.onDestroy();
        Log.i(LogTag.INFO.toString(), "QuizActivity was destroyed, its processes was finished");
    }

    @Override
    protected final void onPause() {
        Log.i(LogTag.INFO.toString(), "QuizActivity paused");
        if (countDownTimer != null && countDownTimer.isRunning())
            countDownTimer.pauseTimer();
        super.onPause();
    }

    @Override
    protected final void onResume() {
        Log.i(LogTag.INFO.toString(), "QuizActivity resumed");
        super.onResume();
        if (countDownTimer != null && countDownTimer.isPaused())
            countDownTimer.resumeTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.skip_task && !isTaskFinishing) {
            int currentRemainingTasksCounter = Integer.parseInt(remainingTasksCounterText.getText().toString());
            remainingTasksCounterText.setText(String.format(Locale.getDefault(), "%d", --currentRemainingTasksCounter)); // converting Number to String
            startNextTask();
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


    void startNextTask() {
        isTaskFinishing = false;
        try {
            quizDataProvider.performNextTaskData();
        } catch (IndexOutOfBoundsException e) {
            finishQuiz();
        }
        setViewsDefaultState();
        showNextTaskData();
        Log.i(LogTag.INFO.toString(), "next task was started");
    }

    void freezeTask(final long freezingTime) {
        freezeTask = new FreezeTask(freezingTime);
        freezeTask.execute();
        Log.i(LogTag.INFO.toString(), "freezing was started");
    }

    void finishQuiz() {
        finish();
        Toast.makeText(getApplicationContext(), "Поздравляем! Сессия успешно завершена!", Toast.LENGTH_LONG).show();
    }

    void updateTasksCounters(boolean isAnswerCorrect) {
        int currentRemainingTasksCounter = Integer.parseInt(remainingTasksCounterText.getText().toString());
        remainingTasksCounterText.setText(String.format(Locale.getDefault(), "%d", --currentRemainingTasksCounter)); // converting Number to String

        if (isAnswerCorrect) {
            Log.i(LogTag.INFO.toString(), "answer is correct");
            int currentCorrectAnswersCounter = Integer.parseInt(correctAnswersCounterText.getText().toString());
            correctAnswersCounterText.setText(String.format(Locale.getDefault(), "%d", ++currentCorrectAnswersCounter)); // converting Number to String
        }
        else {
            Log.i(LogTag.INFO.toString(), "answer is incorrect");
            int currentMistakeAnswersCounter = Integer.parseInt(mistakeAnswersCounterText.getText().toString());
            mistakeAnswersCounterText.setText(String.format(Locale.getDefault(), "%d", ++currentMistakeAnswersCounter)); // converting Number to String
        }
    }


    void loadViewElements() {
        toolbar = findViewById(R.id.toolbar_activity_quiz);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        remainingTasksCounterText = findViewById(R.id.remaining_counter_text);
        correctAnswersCounterText = findViewById(R.id.correct_counter_text);
        mistakeAnswersCounterText = findViewById(R.id.mistake_counter_text);

        remainingTasksCounterText.setText(String.format(Locale.getDefault(), "%d", quizDataProvider.getTasksCount()));
    }
}
