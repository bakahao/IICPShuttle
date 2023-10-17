package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //For testing -- cf
        mAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.button6);

        btnLogout.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        });

        AppCompatButton button5 = findViewById(R.id.button5);

        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, StudentContactUsActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onScanMeClicked(View view){
        Intent intent = new Intent(this, ScanMeActivity.class);
        startActivity(intent);
    }
}