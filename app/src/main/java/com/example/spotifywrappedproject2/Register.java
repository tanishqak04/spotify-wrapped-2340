package com.example.spotifywrappedproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonReg, buttonLog;
    FirebaseAuth mAuth;
    String email, password;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null).
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(Register.this, SpotifyLogin.class);
            startActivity(intent);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.email1);
        editTextPassword = findViewById(R.id.pw1);
        buttonReg = findViewById(R.id.button1);
        buttonLog = findViewById(R.id.button3);


        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });


        buttonReg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    // Handle the case where email or password is empty
                    Toast.makeText(Register.this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                /*
                 * Code to register users to Firebase once email and password
                 * are obtained.
                 *
                 * Needs Email and Password as Strings obtained from user input
                 *
                 *  */
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User is registered successfully
                                    FirebaseUser user = mAuth.getCurrentUser(); //Optionally get ref to user

                                    //******Take user to next activity and/or display toast confirming******
                                    Intent intent = new Intent(Register.this, SpotifyLogin.class);
                                    startActivity(intent);


                                } else {
                                    if (password.length() < 6) {
                                        Toast.makeText(Register.this, "Password is less then 6 Characters.", Toast.LENGTH_SHORT).show();
                                    }
                                    // If sign in fails, displays a toast
                                    Toast.makeText(Register.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}