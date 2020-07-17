package com.iskandev.examrus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.iskandev.examrus.quiz.activities.StressesActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        Toolbar toolbar = findViewById(R.id.toolbar_activity_main);
        setSupportActionBar(toolbar); // sets Toolbar as an ActionBar

        final Button btnParonyms = findViewById(R.id.paronyms_button);
        final Button btnStresses = findViewById(R.id.stresses_button);
        final Intent toParonymsChoosingQuizTypeActivity = new Intent(this, ParonymsChoosingQuizTypeActivity.class);
        final Intent toStressesActivity = new Intent(this, StressesActivity.class);

        btnParonyms.setOnClickListener( (view) -> startActivity(toParonymsChoosingQuizTypeActivity) );
        btnStresses.setOnClickListener( (view) -> startActivity(toStressesActivity) );
    }
}
