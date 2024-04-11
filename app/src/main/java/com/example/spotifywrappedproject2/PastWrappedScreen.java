package com.example.spotifywrappedproject2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PastWrappedScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_wrapped_screen);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CardView cardViewWrap23 = (CardView) findViewById(R.id.wrap23);
        cardViewWrap23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastWrappedScreen.this, wrapped23.class);
                startActivity(intent);
            }
        });

        CardView cardViewWrap22 = (CardView) findViewById(R.id.wrap22);
        cardViewWrap22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastWrappedScreen.this, wrapped22.class);
                startActivity(intent);
            }
        });
        CardView cardViewWrap21 = (CardView) findViewById(R.id.wrap21);
        cardViewWrap21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastWrappedScreen.this, wrapped21.class);
                startActivity(intent);
            }
        });
        CardView cardViewWrap20 = (CardView) findViewById(R.id.wrap20);
        cardViewWrap20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastWrappedScreen.this, wrapped20.class);
                startActivity(intent);
            }
        });
        CardView cardViewWrap19 = (CardView) findViewById(R.id.wrap19);
        cardViewWrap19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastWrappedScreen.this, wrapped19.class);
                startActivity(intent);
            }
        });
        CardView cardViewWrap18 = (CardView) findViewById(R.id.wrap18);
        cardViewWrap18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastWrappedScreen.this, wrapped18.class);
                startActivity(intent);
            }
        });
    }
}