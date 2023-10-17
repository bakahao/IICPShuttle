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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends Activity {

    private EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.idEditText);
        passwordEditText = findViewById(R.id.idEditText2);
        Button loginButton = findViewById(R.id.button2);

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
                // Check the input exist in database or not
                mAuth.signInWithEmailAndPassword(email, enteredPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            checkRole();
                        } else {
                            Toast.makeText(LoginActivity.this, "Log in Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            checkRole();
        }
    }

    private void checkRole(){
        databaseReference = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        FirebaseUser mUser = mAuth.getCurrentUser();

        databaseReference.child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String role = snapshot.child("userRole").getValue(String.class);

                    if (role != null){
                        if (role.equals("Student")){
                            Toast.makeText(LoginActivity.this, "User log in successfully", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } else if (role.equals("Admin")){
                            Toast.makeText(LoginActivity.this, "User log in successfully", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, AdminHomePageActivity.class));
                        } else if (role.equals("Driver")) {
                            Toast.makeText(LoginActivity.this, "User log in successfully", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, DriverHomePageActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Role not found for this user", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "User not found in the database", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
