package com.example.spotifywrappedproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class Wrapped extends AppCompatActivity {
    private String accessToken;

    private int[] imageViewIds = {R.id.albumCover1, R.id.albumCover2, R.id.albumCover3, R.id.albumCover4, R.id.albumCover5};
    private int[] textViewIds = {R.id.songTitle1, R.id.songTitle2, R.id.songTitle3, R.id.songTitle4, R.id.songTitle5};

    private boolean userInteracted = false;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapped);

        accessToken = getIntent().getStringExtra("accessToken");
        API api = new API(accessToken);


        api.getTopTracks(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Wrapped", "Network error: ", e);
                runOnUiThread(() -> Toast.makeText(Wrapped.this, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                Log.d("Wrapped", "API Response: " + responseBody);
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(Wrapped.this, "Error fetching top tracks: " + response.message(), Toast.LENGTH_SHORT).show());
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
                                Glide.with(Wrapped.this).load(imageUrl).into(imageView);

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

        //Initialize the spinner
        spinner = findViewById(R.id.spinner);

        String accessToken = getIntent().getStringExtra("accessToken");

        List<String> pageOptions = new ArrayList<>();
        pageOptions.add("Select a recent wrapped!"); // Default option
        pageOptions.add("1 Week Wrapped");
        pageOptions.add("1 Month Wrapped");
        pageOptions.add("1 Year Wrapped");
        pageOptions.add("All Time Wrapped");

        //Set up the adapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pageOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Set the spinner item selection listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Avoid automatic navigation on initial spinner selection
                if (userInteracted && position > 0) {
                    String selectedPage = pageOptions.get(position);
                    navigateToPage(selectedPage);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset user interaction flag and spinner position
        userInteracted = false;
        spinner.setSelection(0); // Set the spinner to the default
    }

    public void onUserInteraction() {
        super.onUserInteraction();
        userInteracted = true;
    }



    private void navigateToPage(String page) {
        if (!userInteracted) return;

        Intent myIntent = null;
        if ("1 Week Wrapped".equals(page)) {
            myIntent = new Intent(Wrapped.this, OneWeekWrapped.class);
        } else if ("1 Month Wrapped".equals(page)) {
            myIntent = new Intent(Wrapped.this, OneMonthWrapped.class);
        } else if ("1 Year Wrapped".equals(page)) {
            myIntent = new Intent(Wrapped.this, YearWrapped.class);
        } else if ("All Time Wrapped".equals(page)) {
            myIntent = new Intent(Wrapped.this, AllTimeWrapped.class);
        }
        // Add more else if statements for other pages

        if (myIntent != null) {
            startActivity(myIntent);
        }

    }
}