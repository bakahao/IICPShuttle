package com.example.iicpshuttle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {

    private EditText emailEditText, passwordEditText;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.idEditText);
        passwordEditText = findViewById(R.id.idEditText2);
        Button loginButton = findViewById(R.id.button2);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Button registerButton = findViewById(R.id.button1);

        registerButton.setOnClickListener(v -> {
            // when click on Register button ，go to RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String enteredPassword = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || enteredPassword.isEmpty()) {
                //Display error message
                Toast.makeText(LoginActivity.this, "Please enter Student ID and Password", Toast.LENGTH_SHORT).show();
            } else {
                if (email.equals("admin") && enteredPassword.equals("admin")) {
                    Intent intent = new Intent(LoginActivity.this, AdminHomePageActivity.class);
                    startActivity(intent);
                }else if(email.equals("driver") && enteredPassword.equals("driver")){
                    Intent intent = new Intent(LoginActivity.this, DriverHomePageActivity.class);
                    startActivity(intent);
                }else {
                    // Check the input exist in database or not
                    mAuth.signInWithEmailAndPassword(email, enteredPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, "User log in successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "Log in Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(this, HomeActivity.class));
        }
    }
}