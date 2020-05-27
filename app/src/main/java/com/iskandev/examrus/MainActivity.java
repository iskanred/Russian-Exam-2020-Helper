package com.iskandev.examrus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.iskandev.examrus.stresses.StressesActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_activity_main);
        setSupportActionBar(toolbar); // sets Toolbar as an ActionBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        final Button btnParonyms = findViewById(R.id.paronyms_button);
        final Button btnStresses = findViewById(R.id.stresses_button);
        final Intent toParonymsActivity = new Intent(this, ParonymsActivity.class);
        final Intent toStressesActivity = new Intent(this, StressesActivity.class);

        btnParonyms.setOnClickListener( (view) -> startActivity(toParonymsActivity) );
        btnStresses.setOnClickListener( (view) -> startActivity(toStressesActivity) );
    }
}
