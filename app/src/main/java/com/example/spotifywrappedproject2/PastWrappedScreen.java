package com.example.spotifywrappedproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PastWrappedScreen extends AppCompatActivity {

    Map<String, Object> map1;
    Map<String, Object> map2;
    Map<String, Object> map3;
    Map<String, Object> map4;
    Map<String, Object> map5;
    Map<String, Object> map6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_wrapped_screen);

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "Wrapped" collection
        db.collection("wrapped")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                if (map1 == null) {
                                    map1 = map;
                                } else if (map2 == null) {
                                    map2 = map;
                                    ArrayList<String> str2 = (ArrayList<String>) map2.get("urls");
                                    System.out.println(str2.get(3));
                                } else if (map3 == null) {
                                    map3 = map;
                                } else if (map4 == null) {
                                    map4 = map;
                                } else if (map5 == null) {
                                    map5 = map;
                                } else if (map6 == null) {
                                    map6 = map;
                                }
                                ArrayList<String> strs = (ArrayList<String>) map1.get("songs");

                                System.out.println(strs.get(0));

                            }
                        } else {
                            Toast.makeText(PastWrappedScreen.this, "No past Wraps Available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}