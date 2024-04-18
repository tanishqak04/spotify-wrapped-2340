package com.example.spotifywrappedproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class oneWeekArtist extends AppCompatActivity {
    private API api;
    private String accessToken;
    private ImageView[] imageViews = new ImageView[5];
    private TextView[] textViews = new TextView[5];
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_week_wrapped);

        // Initialize Views
        initViews();

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        if (accessToken == null || accessToken.isEmpty()) {
            Toast.makeText(this, "Access token is missing", Toast.LENGTH_LONG).show();
            finish(); // End this activity due to lack of token
            return;
        }

        api = new API(accessToken); // Initialize API with accessToken
        fetchAndDisplayTopArtists();
    }

    private void initViews() {
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i] = findViewById(getResources().getIdentifier("albumCover" + (i + 1), "id", getPackageName()));
            textViews[i] = findViewById(getResources().getIdentifier("songTitle" + (i + 1), "id", getPackageName()));
        }
    }

    private void fetchAndDisplayTopArtists() {
        // Only fetch "short_term" top tracks
        api.getTopArtists("short_term", 5, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(oneWeekArtist.this, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(oneWeekArtist.this, "Error fetching top tracks: " + response.message(), Toast.LENGTH_SHORT).show());
                    return;
                }
                String responseData = response.body().string();
                updateUIWithTracks(responseData);
            }
        });
    }

    private void updateUIWithTracks(String responseData) {
        try {
            JSONObject jsonResponse = new JSONObject(responseData);
            JSONArray items = jsonResponse.getJSONArray("items");
            runOnUiThread(() -> {
                try {
                    for (int i = 0; i < items.length() && i < imageViews.length; i++) {
                        JSONObject track = items.getJSONObject(i);
                        String trackName = track.getString("name");
                        String imageUrl = track.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");

                        Glide.with(oneWeekArtist.this).load(imageUrl).into(imageViews[i]);
                        textViews[i].setText(trackName);
                    }
                } catch (JSONException e) {
                    Toast.makeText(oneWeekArtist.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            runOnUiThread(() -> Toast.makeText(oneWeekArtist.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}