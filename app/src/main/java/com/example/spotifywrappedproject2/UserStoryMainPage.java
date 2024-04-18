package com.example.spotifywrappedproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserStoryMainPage extends AppCompatActivity {
    private boolean userInteracted = false;
    private Spinner spinner;
    private String accessToken;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String date;


    // code for getting username
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_story_main_page);
        TextView welcomeTextView = findViewById(R.id.greeting);

        accessToken = getIntent().getStringExtra("accessToken");
        API api = new API(accessToken);

        api.getProf(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("tag", "msg", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        final String displayName = jsonObject.getString("display_name");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println(displayName);
                                welcomeTextView.setText("Welcome, " + displayName);
                            }
                        });
                    } catch (JSONException e) {
                        Log.e("JSON Parsing Error", "Error parsing JSON", e);
                    }
                }
            }
        });

        // image buttons

        ImageButton artists = findViewById(R.id.dbartists);
        artists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                navigateToPage("");
            }
        });

        ImageButton tracks = findViewById(R.id.dbtracks);
        artists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPage("Wrapped");
            }
        });

        ImageButton past = findViewById(R.id.dbpast);
        past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPage("Past Wrapped");
            }
        });

        ImageButton discover = findViewById(R.id.dbdiscover);
        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToPage("Discover New Artists");
            }
        });

        //Initialize the spinner
        spinner = findViewById(R.id.spinner2);

        accessToken = getIntent().getStringExtra("accessToken");

        List<String> pageOptions = new ArrayList<>();
        pageOptions.add("Select an option"); // Default option
        pageOptions.add("Past Wrapped");
        pageOptions.add("Discover New Artists");
        pageOptions.add("Wrapped");
        //pageOptions.add("User Story 3");
        //pageOptions.add("User Story 4");
        // add more user stories if needed

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

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM");
        date = simpleDateFormat.format(calendar.getTime());

        Intent myIntent = null;
        if ("Past Wrapped".equals(page)) {
            myIntent = new Intent(UserStoryMainPage.this, PastWrappedScreen.class);
        } else if ("Discover New Artists".equals(page)) {
            myIntent = new Intent(UserStoryMainPage.this, DiscoverNewArtists.class);
            myIntent.putExtra("accessToken", accessToken);
        } else if ("Wrapped".equals(page)) {
            if (date.equals("31-10")) {
                myIntent = new Intent(UserStoryMainPage.this, HalloweenWrap.class);
                myIntent.putExtra("accessToken", accessToken);
            } else if (date.equals("25-12")) {
                myIntent = new Intent(UserStoryMainPage.this, ChristmasWrap.class);
                myIntent.putExtra("accessToken", accessToken);
            } else if (date.equals("31-03")) {
                myIntent = new Intent(UserStoryMainPage.this, EasterWrap.class);
                myIntent.putExtra("accessToken", accessToken);
            } else {
                myIntent = new Intent(UserStoryMainPage.this, Wrapped.class);
                myIntent.putExtra("accessToken", accessToken);
            }
        }
        // Add more else if statements for other pages

        if (myIntent != null) {
            startActivity(myIntent);
        }

    }



}