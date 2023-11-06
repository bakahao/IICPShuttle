package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddScheduleHostelActivity extends AppCompatActivity {

    private Button addScheduleButton;
    private EditText addScheduleDate;
    private EditText addScheduleTime;
    private EditText addScheduleSeat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule_hostel);

        addScheduleButton = findViewById(R.id.addScheduleButton);
        addScheduleDate = findViewById(R.id.addScheduleDate);
        addScheduleTime = findViewById(R.id.addScheduleTime);
        addScheduleSeat = findViewById(R.id.addScheduleSeat);


        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = addScheduleDate.getText().toString();
                String time = addScheduleTime.getText().toString();
                String seat = addScheduleSeat.getText().toString();


                Intent intent = new Intent(AddScheduleHostelActivity.this, ManageScheduleHostelActivity.class);
                intent.putExtra("selectedDate", date); // Pass the date as an extra
                startActivity(intent);
            }
        });

        // Set the click listener for backToManageSchedule outside of the addScheduleButton click listener
        ImageView backToManageSchedule = findViewById(R.id.backToManageSchedule);
        backToManageSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddScheduleHostelActivity.this, ManageScheduleHostelActivity.class);
                startActivity(intent);
            }
        });


    }
}