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
                // Sign out the current user
                FirebaseAuth.getInstance().signOut();

                // Redirect to the login page or perform other actions
                Intent intent = new Intent(DriverHomePageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optional: finish the current activity to prevent going back to it
            }
        });
    }




    public void onAttendanceHostelClick(View view){
        // Pass an extra parameter indicating no filter requirement
        Intent intent = new Intent(this, DriverScheduleActivity.class);
        intent.putExtra("filterDeparture", false); // false for no filter
        startActivity(intent);
    }

    public void onAttendanceCampusClick(View view){
        // Pass an extra parameter indicating the filter requirement for IICP
        Intent intent = new Intent(this, DriverScheduleActivity.class);
        intent.putExtra("filterDeparture", true); // true for IICP filter
        startActivity(intent);
    }

    public void onViewScheduleCLick(View view){
        Intent intent = new Intent(this, DriverScheduleActivity.class);
        startActivity(intent);
    }
}