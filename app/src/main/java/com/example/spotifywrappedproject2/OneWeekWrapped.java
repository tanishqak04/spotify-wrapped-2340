package com.example.spotifywrappedproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class OneWeekWrapped extends AppCompatActivity {
    private API api;
    private String accessToken;
    private ImageView[] imageViews = new ImageView[5];
    private TextView[] textViews = new TextView[5];
    private RelativeLayout relativeLayout;
    private ArrayList<String> songs = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_week_wrapped);

        // Initialize Views
        initViews();

        Button saveButton = findViewById(R.id.saveButton);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        if (accessToken == null || accessToken.isEmpty()) {
            Toast.makeText(this, "Access token is missing", Toast.LENGTH_LONG).show();
            finish(); // End this activity due to lack of token
            return;
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();


                // Create a new Wrapped with a date, songs, and urls
                Map<String, Object> wrap = new HashMap<>();
                wrap.put("songs", songs);
                wrap.put("urls", urls);
                LocalDate currDate = LocalDate.now();
                int year = currDate.getYear();
                int month = currDate.getMonthValue();
                int day = currDate.getDayOfMonth();
                String dateString = String.format("%02d-%02d-%04d", month, day, year);
                wrap.put("date", dateString);

                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();


                // Add a new document with a generated ID
                db.collection("users").document(userID).collection("wrapped")
                        .add(wrap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(OneWeekWrapped.this, "Saved!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(OneWeekWrapped.this, "Failed to Save", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        api = new API(accessToken); // Initialize API with accessToken
        fetchAndDisplayTopTracks();


        relativeLayout = findViewById(R.id.myLayout);
        Button exportButton = (Button) findViewById(R.id.exportButton);

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("heya");
                saveImage();
            }
        });

    }

    private void initViews() {
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i] = findViewById(getResources().getIdentifier("albumCover" + (i + 1), "id", getPackageName()));
            textViews[i] = findViewById(getResources().getIdentifier("songTitle" + (i + 1), "id", getPackageName()));
        }
    }

    private void fetchAndDisplayTopTracks() {
        // Only fetch "short_term" top tracks
        api.getTopTracks("short_term", 5, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(OneWeekWrapped.this, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(OneWeekWrapped.this, "Error fetching top tracks: " + response.message(), Toast.LENGTH_SHORT).show());
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

                        urls.add(imageUrl);
                        songs.add(trackName);

                        Glide.with(OneWeekWrapped.this).load(imageUrl).into(imageViews[i]);
                        textViews[i].setText(trackName);
                    }
                } catch (JSONException e) {
                    Toast.makeText(OneWeekWrapped.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            runOnUiThread(() -> Toast.makeText(OneWeekWrapped.this, "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void saveImage() {
        relativeLayout.setDrawingCacheEnabled(true);
        relativeLayout.buildDrawingCache();
        relativeLayout.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = relativeLayout.getDrawingCache();

        save(bitmap);

    }

    private void save(Bitmap bitmap) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(root + "/Download");
        String fileName = "img" + Calendar.getInstance().get(Calendar.MILLISECOND) + ".jpg";
        File myFile = new File(file, fileName);

        if (myFile.exists()) {
            myFile.delete();
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(myFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            relativeLayout.setDrawingCacheEnabled(false);

        } catch (Exception e) {
            System.out.println(e.toString());
            Toast.makeText(this, "Error : " + e.toString(), Toast.LENGTH_SHORT).show();
        }


    }

}
