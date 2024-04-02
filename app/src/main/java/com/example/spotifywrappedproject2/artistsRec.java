package com.example.spotifywrappedproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class artistsRec extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists_rec);

        TextView profTV = findViewById(R.id.profTV);

        String accessToken = getIntent().getStringExtra("accessToken");
        API spotApi = new API(accessToken);
        Button prof = findViewById(R.id.prof);

        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spotApi.getProf(new Callback() {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string();
                            try {
                                JSONObject json = new JSONObject(responseBody);
                                final String displayName = json.getString("display_name");
                                String output = "Hello, " + displayName;
                                // Update UI on the main thread
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        profTV.setText(output);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(artistsRec.this, "response unsuccessful",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.d("HTTP", "Failed to fetch data: " + e);
                        Toast.makeText(artistsRec.this, "Failed to fetch data, watch Logcat for more details",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

}