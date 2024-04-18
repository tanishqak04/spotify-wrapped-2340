package com.example.spotifywrappedproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

public class wrapped1 extends AppCompatActivity {

    private int[] imageViewIds = {R.id.albumCover1, R.id.albumCover2, R.id.albumCover3, R.id.albumCover4, R.id.albumCover5};
    private int[] textViewIds = {R.id.songTitle1, R.id.songTitle2, R.id.songTitle3, R.id.songTitle4, R.id.songTitle5};
    Map<String, Object> receivedMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapped1);

        Intent intent = getIntent();
        try {
            receivedMap = (Map<String, Object>) intent.getSerializableExtra("map");
        } catch (Exception e) {
            Toast.makeText(wrapped1.this, "No data Available", Toast.LENGTH_SHORT).show();
        }

        if (receivedMap != null) {
            String date = (String) receivedMap.get("date");
            ArrayList<String> songs = (ArrayList<String>) receivedMap.get("songs");
            ArrayList<String> urls = (ArrayList<String>) receivedMap.get("urls");

            for (int i = 0; i < 5; i++) {
                ImageView album = findViewById(imageViewIds[i]);
                TextView title = findViewById(textViewIds[i]);

                title.setText(songs.get(i));
                Glide.with(wrapped1.this).load(urls.get(i)).into(album);
            }
        }


        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}