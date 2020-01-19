package com.iskandev.examrus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.iskandev.examrus.stresses.StressesActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnParonyms = findViewById(R.id.paronyms_button);
        final Button btnStresses = findViewById(R.id.stresses_button);
        final Intent toParonymsActivity = new Intent(this, ParonymsActivity.class);
        final Intent toStressesActivity = new Intent(this, StressesActivity.class);

        btnParonyms.setOnClickListener( (view) -> startActivity(toParonymsActivity) );
        btnStresses.setOnClickListener( (view) -> startActivity(toStressesActivity) );
    }
}
