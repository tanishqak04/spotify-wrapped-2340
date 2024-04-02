package com.example.spotifywrappedproject2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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

public class Login extends AppCompatActivity {

    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonReg;
    FirebaseAuth mAuth;
    String email, password;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null).
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //add implementation for user if they are already logged in.
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.email2);
        editTextPassword = findViewById(R.id.pw2);
        buttonReg = findViewById(R.id.button2);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());




                /*
                 * Code to login users to Firebase once email and password
                 * are obtained
                 *
                 * Needs Email and Password as Strings obtained from user input
                 *
                 *  */
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, move to next activity or show user login success
                                    FirebaseUser user = mAuth.getCurrentUser();



                                } else {
                                    // If sign in fails, display a toast
                                    Toast.makeText(Login.this, "Login failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }


        });


    }
}