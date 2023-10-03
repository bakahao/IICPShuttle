package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;

public class AdminHomePageActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);

        AppCompatButton button5 = findViewById(R.id.button5);

        mAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.button6);

        btnLogout.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(AdminHomePageActivity.this, LoginActivity.class));
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePageActivity.this, AdminContactUsActivity.class);

                startActivity(intent);
            }
        });

    }
}