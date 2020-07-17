package com.iskandev.examrus;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.iskandev.examrus.quiz.activities.ParonymsCardQuizActivity;
import com.iskandev.examrus.quiz.activities.ParonymsInputQuizActivity;


public class ParonymsChoosingQuizTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_paronyms_choosing_quiz_type);

        Toolbar toolbar = findViewById(R.id.toolbar_activity_paronyms_choosing_quiz_type_activity);
        setSupportActionBar(toolbar); // sets Toolbar as an ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Button btnCardQuiz = findViewById(R.id.paronyms_card_quiz_button);
        final Button btnInputQuiz = findViewById(R.id.paronyms_input_quiz_button);
        final Intent toParonymsCardQuizActivity = new Intent(this, ParonymsCardQuizActivity.class);
        final Intent toParonymsInputQuizActivity = new Intent(this, ParonymsInputQuizActivity.class);

        btnCardQuiz.setOnClickListener( (view) -> startActivity(toParonymsCardQuizActivity));
        btnInputQuiz.setOnClickListener( (view) -> startActivity(toParonymsInputQuizActivity));
    }

    @Override
    public final boolean onOptionsItemSelected(@NonNull final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
