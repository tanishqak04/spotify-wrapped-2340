package com.example.spotifywrappedproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class PastWrappedScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_wrapped_screen);

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Reference to the "Wrapped" collection
        CollectionReference wrappedCollectionRef = db.collection("Wrapped");

        // Query to retrieve all documents from the "Wrapped" collection
        wrappedCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                        // Loop through each document in the collection
                        for (DocumentSnapshot document : documents) {
                            // Retrieve the image data from the document
                            Blob blob = document.getBlob("imageData");
                            if (blob != null) {
                                byte[] imageData = blob.toBytes();

                                // Convert byte array to Bitmap
                                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                                // Display the Bitmap in an ImageView
                                ImageView imageView = new ImageView(PastWrappedScreen.this);
                                imageView.setImageBitmap(bitmap);

                                // Add the ImageView to your layout (e.g., a LinearLayout)
                                LinearLayout layout = findViewById(R.id.imageGrid);
                                layout.addView(imageView);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle errors
                    }
                });


    }
}