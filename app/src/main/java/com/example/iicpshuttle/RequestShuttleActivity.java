package com.example.iicpshuttle;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RequestShuttleActivity extends AppCompatActivity {
    private TextView textViewStudentID;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView dateTextView, timeTextView;
    private Spinner departureSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_shuttle);

        textViewStudentID = findViewById(R.id.textViewStudentID);
        textViewStudentID.setText("Student ID: " + HomeActivity.user.getStudentID());
        departureSpinner = findViewById(R.id.departureSpinner);
        dateTextView = findViewById(R.id.dateEditText);
        timeTextView = findViewById(R.id.timeEditText);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.departure, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departureSpinner.setAdapter(adapter);
    }

    public void onDatePicker(View v){
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(RequestShuttleActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        dateTextView.setText(String.format("%02d", dayOfMonth) + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    public void onTimePicker(View v){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(RequestShuttleActivity.this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        String amPm;
                        if(hourOfDay < 12){
                            amPm = "AM";
                        } else {
                            amPm = "PM";
                        }

                        timeTextView.setText(hourOfDay + ":" + String.format("%02d", minute) + amPm);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void onRequestClick(View view){
        String departure = departureSpinner.getSelectedItem().toString();
        String date = dateTextView.getText().toString();
        String time = timeTextView.getText().toString();
        String studentID = textViewStudentID.getText().toString().substring("Student ID: ".length());

        RequestShuttle requestShuttle = new RequestShuttle(departure, date, time);

        DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("RequestShuttle");
        db.child(studentID).setValue(requestShuttle).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RequestShuttleActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RequestShuttleActivity.this, "Added Unsuccessfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
