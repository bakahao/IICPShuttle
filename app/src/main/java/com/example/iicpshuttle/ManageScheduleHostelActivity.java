package com.example.iicpshuttle;

import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;

public class ManageScheduleHostelActivity extends AppCompatActivity {
    private LinearLayout buttonContainer;
    private LinearLayout linear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedule_hostel);

        Intent intent = getIntent();
        String departure = intent.getStringExtra("Departure");

        linear = findViewById(R.id.buttonLayout);
        createButton(departure);

        buttonContainer = findViewById(R.id.buttonScheduleContainer);

        // Retrieve the selected date from the intent

        if (intent != null && intent.hasExtra("selectedDate")) {
            String selectedDate = intent.getStringExtra("selectedDate");
            addNewDateButton(selectedDate);
        }


        ImageView addScheduleImageView = findViewById(R.id.addScheduleImage);

        addScheduleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageScheduleHostelActivity.this, AddScheduleHostelActivity.class);
                intent.putExtra("Departure", departure);
                startActivity(intent);
            }
        });

        ImageView backToHome = findViewById(R.id.backToHome);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageScheduleHostelActivity.this, AdminHomePageActivity.class);
                startActivity(intent);
            }
        });

    }


    private void createButton(String departure){
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
//

                        AppCompatButton apc = new AppCompatButton(ManageScheduleHostelActivity.this);
                        apc.setId(dateSnapshot.getKey().hashCode());
                        apc.setBackgroundResource(R.drawable.rounded_home_button_background);
                        final int id_ = apc.getId();
                        apc.setText(dateSnapshot.getKey());
                        apc.setTextColor(Color.BLACK);
                        linear.addView(apc, params);
                        AppCompatButton btn = findViewById(id_);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Get the text (date) of the button
                                String date = dateSnapshot.getKey();
                                Toast.makeText(v.getContext(), "Button " + id_, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ManageScheduleHostelActivity.this, ViewScheduleHostelActivity.class);
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



        buttonContainer = findViewById(R.id.buttonScheduleContainer);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(this);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Set the top margin (adjust the value as needed)
        layoutParams.setMargins(0, 0, 0, 1500);
        button.setLayoutParams(layoutParams);
        button.setText(text);
        button.setBackgroundResource(R.drawable.rounded_home_button_background); // Set the background
        button.setTextSize(25);
        button.setMinHeight(55);
        button.setElevation(4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageScheduleHostelActivity.this, ViewScheduleHostelActivity.class);
                intent.putExtra("selectedDate", text); // Pass the selected date as an extra
                startActivity(intent);
            }
        });

        return button;
    }

    public void addNewDateButton(String selectedDate) {
        Button dateButton = createStyledButton(selectedDate);
        buttonContainer.addView(dateButton);
    }
}
