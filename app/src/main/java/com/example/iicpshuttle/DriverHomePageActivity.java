package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DriverHomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_homepage);
    }

    public void onAttendanceHostelCLick(View view){
        Intent intent = new Intent(this, DriverScheduleActivity.class);
        startActivity(intent);
    }

    public void onAttendanceCampusClick(View view){
        Intent intent = new Intent(this, DriverScheduleActivity.class);
        startActivity(intent);
    }
}