package com.example.spotifywrappedproject2;

import android.text.TextUtils;

import java.util.List;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class API {
    private String accessToken;
    private final OkHttpClient client = new OkHttpClient();

    public API(String accessToken) {
        this.accessToken = accessToken;
    }

    //api methods below

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


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
                .url("https://api.spotify.com/v1/me/top/tracks?limit=5") // Adjust limit as needed
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        client.newCall(request).enqueue(callback);
    }

    public void getRecommendations(List<String> seedTracks, Callback callback) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.spotify.com/v1/recommendations").newBuilder();
        urlBuilder.addQueryParameter("seed_tracks", TextUtils.join(",", seedTracks));

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        client.newCall(request).enqueue(callback);
    }
}
