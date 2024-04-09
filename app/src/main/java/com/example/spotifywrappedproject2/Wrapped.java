package com.example.spotifywrappedproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class Wrapped extends AppCompatActivity {
    private boolean userInteracted = false;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapped);

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