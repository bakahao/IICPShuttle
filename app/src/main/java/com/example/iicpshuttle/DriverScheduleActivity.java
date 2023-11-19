package com.example.iicpshuttle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DriverScheduleActivity extends AppCompatActivity {

    private LinearLayout buttonLayout;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_schedule);

        boolean filterDeparture = getIntent().getBooleanExtra("filterDeparture", false);

        // Get the current user from Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // If the current user is not null, get the user ID
            String userId = currentUser.getUid();

            // Your existing code to set up the database reference
            databaseReference = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("DriverSchedule").child(userId).child("ShuttleList");

            buttonLayout = findViewById(R.id.buttonLayout);

            // Pass the filterDeparture parameter to retrieveScheduleData
            retrieveScheduleData(filterDeparture);
        } else {

        }
    }


    private void retrieveScheduleData(boolean filterDeparture) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear existing buttons
                buttonLayout.removeAllViews();

                // Iterate through the schedule data and create buttons based on the filter
                for (DataSnapshot shuttleSnapshot : dataSnapshot.getChildren()) {
                    Schedule schedule = shuttleSnapshot.getValue(Schedule.class);

                    // Check the filter requirement
                    if (schedule != null && (filterDeparture && schedule.getShuttleDeparture().startsWith("IICP")) ||
                            (!filterDeparture && !schedule.getShuttleDeparture().startsWith("IICP"))) {
                        createButton(schedule, shuttleSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Handle errors
                Log.e("FirebaseError", "Failed to retrieve data: " + databaseError.getMessage());
                Toast.makeText(DriverScheduleActivity.this, "Failed to retrieve schedule data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createButton(Schedule schedule, String shuttleId) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 50;

        AppCompatButton button = new AppCompatButton(this);
        button.setId(View.generateViewId());
        button.setBackgroundResource(R.drawable.rounded_home_button_background);
        button.setText("Date: " + schedule.getShuttleDate() + "\nTime: " + schedule.getShuttleTime() + "\nDeparture: " + schedule.getShuttleDeparture());
        button.setTextColor(Color.BLACK);

        button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

        buttonLayout.addView(button, params);

        // Other styling if needed
        button.setPadding(20, 20, 20, 20);

        Log.d("ShuttleId", "Retrieved Shuttle ID: " + shuttleId);


        // Other styling if needed
        button.setPadding(20, 20, 20, 20);

        Log.d("ShuttleId", "Retrieved Shuttle ID: " + shuttleId);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Schedule Selected", Toast.LENGTH_SHORT).show();

                // Use the shuttleId obtained from the method parameters
                Intent intent = new Intent(DriverScheduleActivity.this, DriverStudentListActivity.class);
                intent.putExtra("shuttleId", shuttleId);

                String path;
                if (schedule.getShuttleDeparture().startsWith("IICP")){
                    path = "CampusShuttle" + "/" +schedule.getShuttleDate() + "/" + schedule.getShuttleTime();
                } else {
                    path = "HostelShuttle" + "/" +schedule.getShuttleDate() + "/" + schedule.getShuttleTime();
                }
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });
    }
    public void onIconClick(View view) {
        Intent intent = new Intent(this, DriverHomePageActivity.class);
        startActivity(intent);
    }
}