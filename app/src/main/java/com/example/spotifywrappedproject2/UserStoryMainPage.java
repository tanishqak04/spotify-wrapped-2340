package com.example.spotifywrappedproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserStoryMainPage extends AppCompatActivity {
    private boolean userInteracted = false;
    private Spinner spinner;
    private String accessToken;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_story_main_page);

        //Initialize the spinner
        spinner = findViewById(R.id.spinner2);

        ImageButton buttonSetting = (ImageButton) findViewById(R.id.settingsGear);

        accessToken = getIntent().getStringExtra("accessToken");

        List<String> pageOptions = new ArrayList<>();
        pageOptions.add("Select an option"); // Default option
        pageOptions.add("Past Wrapped");
        pageOptions.add("Discover New Artists");
        pageOptions.add("Top Songs");
        pageOptions.add("Top Artists");
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

        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserStoryMainPage.this, Settings.class);
                intent.putExtra("sourceClass", UserStoryMainPage.class.getName());
                startActivity(intent);
            }
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
        } else if ("Top Songs".equals(page)) {
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
        } else if ("Top Artists".equals(page)) {
            myIntent = new Intent(UserStoryMainPage.this, TopArtist.class);
            myIntent.putExtra("accessToken", accessToken);
        }
        // Add more else if statements for other pages

        if (myIntent != null) {
            startActivity(myIntent);
        }

    }
}