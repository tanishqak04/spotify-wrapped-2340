package com.example.spotifywrappedproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;


public class Settings extends AppCompatActivity {

    Switch toggle;
    boolean nightTog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Class<?> className;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button updateAcc = findViewById(R.id.updateacc);
        ImageButton back = findViewById(R.id.backArrow);

        getSupportActionBar().hide();

        toggle = findViewById(R.id.toggle);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightTog = sharedPreferences.getBoolean("night", false);

        if (nightTog) {
            toggle.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        toggle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (nightTog) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);
                }
                editor.apply();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            try {
                className = Class.forName(intent.getStringExtra("sourceClass"));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        updateAcc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent myIntent = null;
                myIntent = new Intent(Settings.this, UpdateAcc.class);
                if (myIntent != null) {
                    startActivity(myIntent);
                }
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, className);
                startActivity(intent);
            }
        });
    }

}