package com.example.spotifywrappedproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends AppCompatActivity {

    Class<?> className;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button updateAcc = findViewById(R.id.updateacc);
        ImageButton back = findViewById(R.id.backArrow);
        ImageButton delAcc = findViewById(R.id.delButton);
        Button signOut = findViewById(R.id.signout);

        Intent intent = getIntent();
        if (intent != null) {
            try {
                className = Class.forName(intent.getStringExtra("sourceClass"));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        updateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Settings.this, UpdateAcc.class);
                myIntent.putExtra("sourceClass", className.getName());
                if (myIntent != null) {
                    startActivity(myIntent);
                }
            }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, className);
                startActivity(intent);
            }
        });

        delAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new  AlertDialog.Builder(Settings.this)
                            .setMessage("Are you sure you want to delete your account?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Intent intent = new Intent(Settings.this, Register.class);
                                                            startActivity(intent);
                                                        } else {

                                                        }
                                                    }
                                                });
                                    }
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();

            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();

            }
        });

    }

    public void signOut() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Toast.makeText(getApplicationContext(), "Sign-out successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.this, Register.class);
                    startActivity(intent);
                }
            }
        });
    }


}