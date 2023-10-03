package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class StudentContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_contact_us);

        ImageView backIconImageView = findViewById(R.id.backIconImageView);


        backIconImageView.setOnClickListener(v -> {
            Intent backIntent = new Intent(StudentContactUsActivity.this, HomeActivity.class);
            startActivity(backIntent);
        });
    }
}