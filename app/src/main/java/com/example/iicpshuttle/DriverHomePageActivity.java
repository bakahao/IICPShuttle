package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.AppCompatButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class DriverHomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_homepage);

        AppCompatButton logoutButton = findViewById(R.id.button3);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();


                Intent intent = new Intent(DriverHomePageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }




    public void onAttendanceHostelClick(View view){
        Intent intent = new Intent(this, DriverScheduleActivity.class);
        intent.putExtra("filterDeparture", false);
        startActivity(intent);
    }

    public void onAttendanceCampusClick(View view){
        Intent intent = new Intent(this, DriverScheduleActivity.class);
        intent.putExtra("filterDeparture", true);
        startActivity(intent);
    }

}