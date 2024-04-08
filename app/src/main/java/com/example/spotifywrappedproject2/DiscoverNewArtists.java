package com.example.spotifywrappedproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.Button;

public class DiscoverNewArtists extends AppCompatActivity {
    private LinearLayout recommendationsLayout;
    private API api;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to your layout
        setContentView(R.layout.activity_discover_new_artists);
        recommendationsLayout = findViewById(R.id.recommendationsLayout);

        // Retrieve the access token from the intent
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");

        if (accessToken == null || accessToken.isEmpty()) {
            Toast.makeText(this, "Access token is missing", Toast.LENGTH_LONG).show();
            finish(); // End this activity due to lack of token
            return;
        }

        // Since we have the accessToken now, we pass it to the API constructor
        api = new API(accessToken); // Correctly passing the accessToken here
        fetchUserTopTracks();

        // Assuming you have a button with ID backButtonDiscover in your layout
        Button backButton = findViewById(R.id.backButtonDiscover);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This will close the current activity and take you back to the previous one
                finish();
            }
        });

        // You can initialize other UI elements and set up event listeners here as needed
    }

    private void fetchUserTopTracks() {
        api.getTopTracks(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // This is a network failure
                Log.e("DiscoverNewArtists", "Network error when fetching top tracks", e);
                runOnUiThread(() -> Toast.makeText(DiscoverNewArtists.this, "Network error", Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                Log.d("DiscoverNewArtists", "API Response: " + responseData); // Log the response data
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(responseData);
                        JSONArray tracks = json.getJSONArray("items");
                        List<String> seedTracks = new ArrayList<>();
                        for (int i = 0; i < tracks.length(); i++) {
                            seedTracks.add(tracks.getJSONObject(i).getString("id"));
                        }
                        getArtistRecommendations(seedTracks);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // This is an API error
                    Log.e("DiscoverNewArtists", "API error when fetching top tracks: " + responseData);
                    runOnUiThread(() -> Toast.makeText(DiscoverNewArtists.this, "Failed to fetch top tracks: " + response.code(), Toast.LENGTH_LONG).show());
                }
            }
        });
    }

    private void getArtistRecommendations(List<String> seedTracks) {
        api.getRecommendations(seedTracks, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // This is called if the request was not successfully executed
                runOnUiThread(() -> Toast.makeText(DiscoverNewArtists.this, "Error fetching recommendations: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // This is called when the request is successfully executed
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            // Parse the JSON response to get the recommendations
                            JSONObject json = new JSONObject(responseData);
                            JSONArray jsonArray = json.getJSONArray("tracks");
                            for (int i = 0; i < jsonArray.length() && i < 5; i++) { // Limit to top 5 artists
                                JSONObject trackJson = jsonArray.getJSONObject(i);
                                JSONArray artists = trackJson.getJSONArray("artists");
                                if (artists.length() > 0) {
                                    String artistName = artists.getJSONObject(0).getString("name");

                                    addArtistToLayout(artistName, i + 1);
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(DiscoverNewArtists.this, "Failed to parse recommendations", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    // Handle the case where the server response was not successful
                    runOnUiThread(() -> Toast.makeText(DiscoverNewArtists.this, "Failed to get recommendations", Toast.LENGTH_LONG).show());
                }
            }
        });
    }

    private void addArtistToLayout(String artistName, int rank) {
        // Create a horizontal LinearLayout for each artist entry
        LinearLayout artistEntryLayout = new LinearLayout(this);
        artistEntryLayout.setOrientation(LinearLayout.HORIZONTAL);
        artistEntryLayout.setGravity(Gravity.START);

        // Create TextView for the rank
        TextView rankView = new TextView(this);
        rankView.setText(rank + ". ");
        rankView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 55);
        rankView.setTypeface(rankView.getTypeface(), Typeface.BOLD_ITALIC);
        // Adjust the styling as needed

        // Create TextView for the artist name
        TextView artistNameView = new TextView(this);
        artistNameView.setText(artistName);
        artistNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        artistNameView.setTypeface(artistNameView.getTypeface(), Typeface.BOLD_ITALIC);
        // Adjust the styling as needed

        // Add the rankView and artistNameView to the artistEntryLayout
        artistEntryLayout.addView(rankView);
        artistEntryLayout.addView(artistNameView);

        // Add padding or layout parameters as needed
        artistEntryLayout.setPadding(0, 20, 0, 20);

        // Add the artistEntryLayout to your recommendationsLayout
        recommendationsLayout.addView(artistEntryLayout);
    }
}
