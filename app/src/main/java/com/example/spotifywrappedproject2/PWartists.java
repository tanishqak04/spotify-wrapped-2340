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

public class PWartists extends AppCompatActivity {
    private int[] imageViewIds = {R.id.artistPic1, R.id.artistPic2, R.id.artistPic3, R.id.artistPic4, R.id.artistPic5};
    private int[] textViewIds = {R.id.artistName1, R.id.artistName2, R.id.artistName3, R.id.artistName4, R.id.artistName5};
    Map<String, Object> receivedMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwartists);

        Intent intent = getIntent();
        try {
            receivedMap = (Map<String, Object>) intent.getSerializableExtra("map");
        } catch (Exception e) {
            Toast.makeText(PWartists.this, "No data Available", Toast.LENGTH_SHORT).show();
        }

        if (receivedMap != null) {
            String date = (String) receivedMap.get("date");
            TextView header = findViewById(R.id.past_wrap_title);
            String headText = "Your " + date + " Wrapped";
            header.setText(headText);

            ArrayList<String> songs = (ArrayList<String>) receivedMap.get("songs");
            ArrayList<String> urls = (ArrayList<String>) receivedMap.get("urls");

            for (int i = 0; i < 5; i++) {
                ImageView img = findViewById(imageViewIds[i]);
                TextView artist = findViewById(textViewIds[i]);

                artist.setText(songs.get(i));
                Glide.with(PWartists.this).load(urls.get(i)).into(img);
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