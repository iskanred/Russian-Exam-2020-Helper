package com.iskandev.examrus;

import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;


public abstract class ExamActivityTemplate extends AppCompatActivity {

    final int SHOWING_TIME = 1650;
    final int SHOWING_INTERVAL = 550;
    private CountDownTimer countDownTimer;
    Toolbar toolbar;


    protected void runActivity(final int activityLayoutId) {
        setContentView(R.layout.countdown_layout);
        final TextView countDownText = findViewById(R.id.countdown_text);

        countDownTimer = new CountDownTimer(SHOWING_TIME, SHOWING_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDownText.setText(String.format(Locale.getDefault(),
                        "%d", millisUntilFinished / 550 + 1));
            }

            @Override
            public void onFinish() {
                setContentView(activityLayoutId);
                setToolbar(activityLayoutId);
                loadViewElements();
                startQuiz();
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_exam, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_back) {
            onBackPressed();
        }
        return true;
    }

    protected abstract void finishQuiz();

    protected abstract void startQuiz();

    protected abstract void loadViewElements();


    private void setToolbar(int activityLayoutId) {
        toolbar = findViewById(R.id.toolbar_activity_exam);
        TextView toolbarTitle = toolbar.findViewById(R.id.title);

        if (activityLayoutId == R.layout.activity_stresses)
            toolbarTitle.setText(R.string.stresses_name);
        if (activityLayoutId == R.layout.activity_paronyms)
            toolbarTitle.setText(R.string.paromyms_name);

        setSupportActionBar(toolbar);
    }
}
