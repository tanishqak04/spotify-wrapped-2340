package com.example.spotifywrappedproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PastWrappedScreen extends AppCompatActivity {

    Map<String, Object> map1;
    Map<String, Object> map2;
    Map<String, Object> map3;
    Map<String, Object> map4;
    Map<String, Object> map5;
    Map<String, Object> map6;

    Map<String, Object>[] arr = new HashMap[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_wrapped_screen);


        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CardView cardViewWrap23 = (CardView) findViewById(R.id.wrap1);
        cardViewWrap23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastWrappedScreen.this, wrapped1.class);
                intent.putExtra("map", (Serializable) map1);
                if (map1 != null) {
                    startActivity(intent);
                }
            }
        });

        CardView cardViewWrap22 = (CardView) findViewById(R.id.wrap2);
        cardViewWrap22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastWrappedScreen.this, wrapped2.class);
                intent.putExtra("map", (Serializable) map2);
                if (map2 != null) {
                    startActivity(intent);
                }
            }
        });
        CardView cardViewWrap21 = (CardView) findViewById(R.id.wrap3);
        cardViewWrap21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastWrappedScreen.this, wrapped3.class);
                intent.putExtra("map", (Serializable) map3);
                if (map3 != null) {
                    startActivity(intent);
                }
            }
        });
        CardView cardViewWrap20 = (CardView) findViewById(R.id.wrap4);
        cardViewWrap20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastWrappedScreen.this, wrapped4.class);
                intent.putExtra("map", (Serializable) map4);
                if (map4 != null) {
                    startActivity(intent);
                }
            }
        });
        CardView cardViewWrap19 = (CardView) findViewById(R.id.wrap5);
        cardViewWrap19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastWrappedScreen.this, wrapped5.class);
                intent.putExtra("map", (Serializable) map5);
                if (map5 != null) {
                    startActivity(intent);
                }
            }
        });
        CardView cardViewWrap18 = (CardView) findViewById(R.id.wrap6);
        cardViewWrap18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PastWrappedScreen.this, wrapped6.class);
                intent.putExtra("map", (Serializable) map6);
                if (map6 != null) {
                    startActivity(intent);
                }
            }
        });

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Reference to the "Wrapped" collection
        db.collection("users").document(userID).collection("wrapped")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                if (map1 == null) {
                                    map1 = map;
                                    arr[0] = map1;
                                } else if (map2 == null) {
                                    map2 = map;
                                    arr[1] = map2;
                                } else if (map3 == null) {
                                    map3 = map;
                                    arr[2] = map3;
                                } else if (map4 == null) {
                                    map4 = map;
                                    arr[3] = map4;
                                } else if (map5 == null) {
                                    map5 = map;
                                    arr[4] = map5;
                                } else if (map6 == null) {
                                    map6 = map;
                                    arr[5] = map6;
                                }
                            }
                            int count = 1;
                            for(Map map : arr) {
                                if (map != null) {
                                    String date = (String) map.get("date");
                                    //System.out.println(date);

                                    String viewIdString = "date" + count;
                                    //System.out.println(viewIdString);
                                    int viewId = getResources().getIdentifier(viewIdString, "id", getPackageName());

                                    if (viewId != 0) {
                                        TextView textView = findViewById(viewId);
                                        textView.setText(date);
                                    } else {
                                        throw new RuntimeException("Broken date Loop :(");
                                    }
                                }
                                count++;
                            }
                        } else {
                            Toast.makeText(PastWrappedScreen.this, "No past Wraps Available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    
}