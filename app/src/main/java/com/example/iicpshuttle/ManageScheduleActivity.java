package com.example.iicpshuttle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ManageScheduleActivity extends AppCompatActivity {
    private LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedule);

        Intent intent = getIntent();
        String departure = intent.getStringExtra("Departure");
        linear = findViewById(R.id.buttonScheduleContainer);

        createButton(departure);

        ImageView addScheduleImageView = findViewById(R.id.addScheduleImage);

        addScheduleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageScheduleActivity.this, AddScheduleActivity.class);
                intent.putExtra("Departure", departure);
                startActivity(intent);
            }
        });

        ImageView backToHome = findViewById(R.id.backToHome);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageScheduleActivity.this, AdminHomePageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createButton(String departure){
        Log.d("ViewScheduleHostel", "ViewScheduleHostelActivity onCreate");

        DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ShuttleSchedule");
        db.child(departure).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dateSnapshot: snapshot.getChildren()){

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.topMargin = 50;

                        final String date = dateSnapshot.getKey();

                        AppCompatButton apc = new AppCompatButton(ManageScheduleActivity.this);
                        apc.setId(date.hashCode());
                        apc.setLayoutParams(params);
                        apc.setBackgroundResource(R.drawable.rounded_home_button_background);
                        final int id_ = apc.getId();
                        apc.setText(date);
                        apc.setTextColor(Color.BLACK);

                        linear.addView(apc);
                        //AppCompatButton btn = findViewById(id_);
                        apc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Get the text (date) of the button from the final variable
                                Toast.makeText(v.getContext(), "Button " + id_, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ManageScheduleActivity.this, ViewScheduleActivity.class);
                                intent.putExtra("Departure", departure);
                                // Add the date as an extra to the Intent
                                intent.putExtra("Date", date);
                                // Add debugging output to check if the date is being passed correctly
                                startActivity(intent);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
