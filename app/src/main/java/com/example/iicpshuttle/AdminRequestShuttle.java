package com.example.iicpshuttle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AdminRequestShuttle extends AppCompatActivity {

    DatabaseReference databaseReference;
    List<ScheduleAdmin> list = new ArrayList<>();
    AdapterAdmin adapter;

    private LinearLayout buttonLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request_shuttle);

        buttonLayout = findViewById(R.id.buttonLayout);

        databaseReference = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("RequestShuttle");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();  // Clear the list before adding new items
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String studentId = dataSnapshot.getKey();
                    for (DataSnapshot requestSnapShot : dataSnapshot.getChildren()) {
                        ScheduleAdmin scheduleAdmin = requestSnapShot.getValue(ScheduleAdmin.class);
                        if (scheduleAdmin != null && "Pending".equals(scheduleAdmin.getStatus())) {
                            list.add(scheduleAdmin);
                            createButton(scheduleAdmin, requestSnapShot.getKey(), studentId);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });
    }

    private void createButton(ScheduleAdmin scheduleAdmin, String requestShuttle, String studentId) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 50;

        AppCompatButton button = new AppCompatButton(this);
        button.setId(View.generateViewId());
        button.setBackgroundResource(R.drawable.rounded_home_button_background);
        button.setText("Date: " + scheduleAdmin.getDate() + "\nTime: " + scheduleAdmin.getTime() + "\nDeparture: " + scheduleAdmin.getDeparture());
        button.setTextColor(Color.BLACK);

        button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

        buttonLayout.addView(button, params);

        // Other styling if needed
        button.setPadding(20, 20, 20, 20);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Schedule Selected", Toast.LENGTH_SHORT).show();

                // Use the shuttleId obtained from the method parameters
                Intent intent = new Intent(AdminRequestShuttle.this, AddRequestShuttle.class);
                intent.putExtra("studentId", studentId);
                intent.putExtra("shuttleId", requestShuttle);
                intent.putExtra("Date", scheduleAdmin.getDate());
                intent.putExtra("Time", scheduleAdmin.getTime());
                intent.putExtra("Departure", scheduleAdmin.getDeparture());
                intent.putExtra("status", scheduleAdmin.getStatus());

                startActivity(intent);
            }
        });
    }
}