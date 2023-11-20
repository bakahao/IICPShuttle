package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRequestShuttle extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private String shuttleId;
    private String studentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request_schedule);

        databaseReference = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("RequestShuttle");



        Intent intent = getIntent();
        if (intent != null) {
            // Retrieve extra data
            studentId = intent.getStringExtra("studentId");
            shuttleId = intent.getStringExtra("shuttleId");  // Remove String declaration here
            String Date = intent.getStringExtra("Date");
            String Time = intent.getStringExtra("Time");
            String Departure = intent.getStringExtra("Departure");
            String status = intent.getStringExtra("status");

            EditText departureEditText = findViewById(R.id.departureEditText);
            if (departureEditText != null) {
                departureEditText.setText(Departure);
            }

            EditText dateEditText = findViewById(R.id.dateEditText);
            if (dateEditText != null) {
                dateEditText.setText(Date);
            }

            EditText timeEditText = findViewById(R.id.timeEditText);
            if (timeEditText != null) {
                timeEditText.setText(Time);
            }
            Button button1 = findViewById(R.id.button1);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Update the status to "Rejected"
                    updateStatus("Rejected");

                }
            });
        }
    }



    private void updateStatus(String newStatus) {
        // Update the status in your Firebase Realtime Database
        databaseReference.child(studentId).child(shuttleId).child("status").setValue("Reject");

        finish();

        Intent intent = new Intent(AddRequestShuttle.this, AdminRequestShuttle.class);
        startActivity(intent);
    }
}