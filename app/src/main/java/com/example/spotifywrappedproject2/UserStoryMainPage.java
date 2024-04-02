package com.example.spotifywrappedproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class UserStoryMainPage extends AppCompatActivity {
    private boolean userInteracted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_story_main_page);

        //Initialize the spinner
        Spinner spinner = findViewById(R.id.spinner2);

        String accessToken = getIntent().getStringExtra("accessToken");

        List<String> pageOptions = new ArrayList<>();
        pageOptions.add("Select an option"); // Default option
        pageOptions.add("Past Wrapped");
        pageOptions.add("User Story 2");
        //pageOptions.add("User Story 3");
        //pageOptions.add("User Story 4");
        // add more user stories if needed

        //Set up the adapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pageOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Set the spinner item selection listner
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

    public void onUserInteracion() {
        super.onUserInteraction();
        userInteracted = true;
    }



    private void navigateToPage(String page) {
        Intent myIntent;
        switch(page) {
            case "Past Wrapped":
                Log.d("Navigation", "Navigating to PastWrappedScreen");
                myIntent = new Intent(UserStoryMainPage.this, PastWrappedScreen.class);
                startActivity(myIntent);
                break;

//            case "User Story 2":
//                myIntent = new Intent(UserStoryMainPage.this, UserStory2.class);
//                break;
//
//            case "User Story 3":
//                myIntent = new Intent(UserStoryMainPage.this, UserStory3.class);
//                break;
//
//            case "User Story 4":
//                myIntent = new Intent(UserStoryMainPage.this, UserStory4.class);
//                break;

            default:
                myIntent = null;
                break;
        }
        if (myIntent != null) {
            startActivity(myIntent);
        }

    }
}