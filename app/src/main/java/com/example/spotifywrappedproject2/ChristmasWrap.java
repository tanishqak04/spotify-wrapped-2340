package com.example.spotifywrappedproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChristmasWrap extends AppCompatActivity {
    private String accessToken;

    private int[] imageViewIds = {R.id.albumCover1, R.id.albumCover2, R.id.albumCover3, R.id.albumCover4, R.id.albumCover5};
    private int[] textViewIds = {R.id.songTitle1, R.id.songTitle2, R.id.songTitle3, R.id.songTitle4, R.id.songTitle5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_christmas_wrap);

        accessToken = getIntent().getStringExtra("accessToken");
        API api = new API(accessToken);

        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        api.getTopTracks10(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Wrapped", "Network error: ", e);
                runOnUiThread(() -> Toast.makeText(ChristmasWrap.this, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                Log.d("Wrapped", "API Response: " + responseBody);
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(ChristmasWrap.this, "Error fetching top tracks: " + response.message(), Toast.LENGTH_SHORT).show());
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    final JSONArray tracks = jsonObject.getJSONArray("items");

                    runOnUiThread(() -> {
                        try {
                            for (int i = 0; i < tracks.length() && i < imageViewIds.length; i++) {
                                JSONObject track = tracks.getJSONObject(i);
                                JSONObject album = track.getJSONObject("album");
                                JSONArray images = album.getJSONArray("images");
                                String imageUrl = images.getJSONObject(0).getString("url");
                                String trackName = track.getString("name");

                                ImageView imageView = findViewById(imageViewIds[i]);
                                TextView textView = findViewById(textViewIds[i]);

                                // Use an image loading library like Glide to load the image
                                Glide.with(ChristmasWrap.this).load(imageUrl).into(imageView);

                                textView.setText(trackName);
                            }
                        } catch (JSONException e) {
                            Log.e("Wrapped", "JSON parsing error: " + e.getMessage());
                        }
                    });
                } catch (JSONException e) {
                    Log.e("Wrapped", "Error parsing top tracks response: " + e.getMessage());
                }
            }
        });
    }
}