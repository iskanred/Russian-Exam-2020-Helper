package com.iskandev.examrus;

import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;


public abstract class ExamActivityTemplate extends AppCompatActivity {

    final int SHOWING_TIME = 1650;
    final int SHOWING_INTERVAL = 550;

    protected void runActivity(final int newViewId) {
        setContentView(R.layout.countdown_layout);
        final TextView countDownText = findViewById(R.id.countdown_text);

        new CountDownTimer(SHOWING_TIME, SHOWING_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDownText.setText(String.format(Locale.getDefault(),
                        "%d", millisUntilFinished / 550 + 1));
            }

            @Override
            public void onFinish() {
                setContentView(newViewId);
                loadViewElements();
                runExam();
            }
        }.start();
    }

    protected abstract void runExam();

    protected abstract void loadViewElements();
}
