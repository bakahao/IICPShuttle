package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        View iconImageView;
        iconImageView = findViewById(R.id.iconImageView);

        iconImageView.setOnClickListener(view -> {
            // create an Intent to switch to LoginActivity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            // run LoginActivity
            startActivity(intent);
        });
    }
}