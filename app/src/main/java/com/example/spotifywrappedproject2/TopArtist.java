package com.example.spotifywrappedproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class TopArtist extends AppCompatActivity {
    private String accessToken;

    private int[] imageViewIds = {R.id.artistPic1, R.id.artistPic2, R.id.artistPic3, R.id.artistPic4, R.id.artistPic5};
    private int[] textViewIds = {R.id.artistName1, R.id.artistName2, R.id.artistName3, R.id.artistName4, R.id.artistName5};
    private RelativeLayout relativeLayout;
    private boolean userInteracted = false;
    private Spinner spinner;
    private ArrayList<String> songs = new ArrayList<>();
    private ArrayList<String> urls = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topartist);

        //Initialize the spinner
        spinner = findViewById(R.id.spinnerartist);

        accessToken = getIntent().getStringExtra("accessToken");
        API api = new API(accessToken);

        List<String> pageOptions = new ArrayList<>();
        pageOptions.add("Select a recent wrapped!"); // Default option
        pageOptions.add("1 Week Wrapped");
        pageOptions.add("1 Month Wrapped");
        pageOptions.add("1 Year Wrapped");
        //Set up the adapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pageOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button saveButton = findViewById(R.id.saveButton);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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

        // export

        relativeLayout = findViewById(R.id.myLayout);
        Button exportButton = (Button) findViewById(R.id.exportButton);

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });
        api.getTopArtists5(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Wrapped", "Network error: ", e);
                runOnUiThread(() -> Toast.makeText(TopArtist.this, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseBody = response.body().string();
                Log.d("Wrapped", "API Response: " + responseBody);
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(TopArtist.this, "Error fetching top artists: " + response.message(), Toast.LENGTH_SHORT).show());
                    return;
                }

                try {
                    JSONObject jsonObject = new JSONObject(responseBody);
                    final JSONArray artists = jsonObject.getJSONArray("items");

                    runOnUiThread(() -> {
                        try {
                            for (int i = 0; i < artists.length() && i < imageViewIds.length; i++) {
                                JSONObject track = artists.getJSONObject(i);
                                //JSONObject album = track.getJSONObject("album");
                                JSONArray images = track.getJSONArray("images");
                                String imageUrl = images.getJSONObject(0).getString("url");
                                String trackName = track.getString("name");

                                urls.add(imageUrl);
                                songs.add(trackName);

                                ImageView imageView = findViewById(imageViewIds[i]);
                                TextView textView = findViewById(textViewIds[i]);

                                // Use an image loading library like Glide to load the image
                                Glide.with(TopArtist.this).load(imageUrl).into(imageView);

                                textView.setText(trackName);
                            }
                        } catch (JSONException e) {
                            Log.e("TopArtist", "JSON parsing error: " + e.getMessage());
                        }
                    });
                } catch (JSONException e) {
                    Log.e("TopArtist", "Error parsing top artists response: " + e.getMessage());
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();


                // Create a new Wrapped with a date, songs, and urls
                Map<String, Object> wrap = new HashMap<>();
                wrap.put("songs", songs);
                wrap.put("urls", urls);
                wrap.put("artist", true);
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
                                Toast.makeText(TopArtist.this, "Saved!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(TopArtist.this, "Failed to Save", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
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
            myIntent = new Intent(TopArtist.this, oneWeekArtist.class);
            myIntent.putExtra("accessToken", accessToken);
        } else if ("1 Month Wrapped".equals(page)) {
            myIntent = new Intent(TopArtist.this, oneMonthArtist.class);
            myIntent.putExtra("accessToken", accessToken);
        } else if ("1 Year Wrapped".equals(page)) {
            myIntent = new Intent(TopArtist.this, oneYearArtist.class);
            myIntent.putExtra("accessToken", accessToken);
        }
        // Add more else if statements for other pages

        if (myIntent != null) {
            startActivity(myIntent);
        }

    }
}