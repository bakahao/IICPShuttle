package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, phoneEditText;
    private Button registerButton;
    private TextView nameErrorTextView, studentIdErrorTextView, passwordErrorTextView, phoneErrorTextView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        registerButton = findViewById(R.id.button1);

        nameErrorTextView = findViewById(R.id.nameErrorTextView);
        studentIdErrorTextView = findViewById(R.id.studentIdErrorTextView);
        passwordErrorTextView = findViewById(R.id.passwordErrorTextView);
        phoneErrorTextView = findViewById(R.id.phoneErrorTextView);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String phoneNumber = phoneEditText.getText().toString().trim();
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
                    if (name.isEmpty()) {
                        nameErrorTextView.setVisibility(View.VISIBLE);
                    } else {
                        nameErrorTextView.setVisibility(View.GONE);
                    }

                    if (email.isEmpty()) {
                        studentIdErrorTextView.setVisibility(View.VISIBLE);
                    } else {
                        studentIdErrorTextView.setVisibility(View.GONE);
                    }

                    if (password.isEmpty()) {
                        passwordErrorTextView.setVisibility(View.VISIBLE);
                    } else {
                        passwordErrorTextView.setVisibility(View.GONE);
                    }

                    if (phoneNumber.isEmpty()) {
                        phoneErrorTextView.setVisibility(View.VISIBLE);

                    } else {
                        phoneErrorTextView.setVisibility(View.GONE);
                    }
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()){
                            Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
    }
    public void onIconClick(View view) {
        // 创建 Intent 以启动 LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
