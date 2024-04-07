package com.example.spotifywrappedproject2;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class API {
    private String accessToken;
    private final OkHttpClient client = new OkHttpClient();

    public API(String accessToken) {
        this.accessToken = accessToken;
    }

    //api methods below


    public void getProf(Callback callback) {
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .header("Authorization", "Bearer " + accessToken)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(callback);
    }

    public void getTopTracks(Callback callback) {
        Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks?limit=10")
                .header("Authorization", "Bearer " + accessToken)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(callback);
    }


    

}
