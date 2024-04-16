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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OneWeekWrapped extends AppCompatActivity {
    private API api;
    private String accessToken;
    private int[] imageViewIds = {R.id.albumCover1, R.id.albumCover2, R.id.albumCover3, R.id.albumCover4, R.id.albumCover5};
    private int[] textViewIds = {R.id.songTitle1, R.id.songTitle2, R.id.songTitle3, R.id.songTitle4, R.id.songTitle5};
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_week_wrapped);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");

        if (accessToken == null || accessToken.isEmpty()) {
            Toast.makeText(this, "Access token is missing", Toast.LENGTH_LONG).show();
            finish(); // End this activity due to lack of token
            return;
        }
        api = new API(accessToken); // Correctly passing the accessToken here

        String[] timeRanges = {"short_term", "medium_term", "long_term"};
        for (int i = 0; i < timeRanges.length; i++) {
            final int index = i;
            api.getTopTracks(timeRanges[i], 5, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("Wrapped", "Network error: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        Log.e("Wrapped", "API error: " + response.message());
                        return;
                    }
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        JSONArray items = jsonResponse.getJSONArray("items");
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject track = items.getJSONObject(i);
                            String trackName = track.getString("name");
                            String imageUrl = track.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ImageView imageView = findViewById(imageViewIds[index]);
                                    TextView textView = findViewById(textViewIds[index]);

                                    // Use an image loading library like Glide to load the image
                                    Glide.with(OneWeekWrapped.this).load(imageUrl).into(imageView);

                                    textView.setText(trackName);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        Log.e("Wrapped", "JSON parsing error: " + e.getMessage());
                    }
                }
            });
        }
    }
}
