package com.example.spotifywrappedproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Map;

public class wrapped5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapped5);
        Button backButton = findViewById(R.id.backButton);

        Intent intent = getIntent();
        try {
            Map<String, Object> receivedMap = (Map<String, Object>) intent.getSerializableExtra("map");
        } catch (Exception e) {
            Toast.makeText(wrapped5.this, "No data Available", Toast.LENGTH_SHORT).show();
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}