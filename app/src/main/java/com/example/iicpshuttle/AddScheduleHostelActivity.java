package com.example.iicpshuttle;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddScheduleHostelActivity extends AppCompatActivity {


    private RadioGroup radioGroup;
    private EditText dateEditText, timeEditText, seatEditText, driverEditText;
    private EditText startDateEditText, endDateEditText, txtDate, txtTime;
    private TextView dateTextView, timeTextView, seatTextView, startDateTextView, endDateTextView, driverTextView;
    private ImageButton btnDatePicker, btnTimePicker, btnDatePicker1, btnDatePicker2;
    private int mYear, mMonth, mDay, mHour, mMinute, mAPM;

    private Calendar startCalendar, endCalendar;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule_campus);

        // Find the UI components
        radioGroup = findViewById(R.id.RadioButton);
        dateEditText = findViewById(R.id.myEditText1);
        timeEditText = findViewById(R.id.myEditText2);
        seatEditText = findViewById(R.id.myEditText3);
        startDateEditText = findViewById(R.id.StartDate);
        endDateEditText = findViewById(R.id.EndDate);
        driverEditText = findViewById(R.id.DriverEditText);
        dateTextView = findViewById(R.id.date);
        timeTextView = findViewById(R.id.time);
        seatTextView = findViewById(R.id.seattext);
        startDateTextView = findViewById(R.id.StartDateText);
        endDateTextView = findViewById(R.id.EndDateText);
        driverTextView = findViewById(R.id.drivertext);

        // Find the date picker and time picker UI components
        btnDatePicker = (ImageButton) findViewById(R.id.DatePicker);
        btnDatePicker1 = (ImageButton) findViewById(R.id.DatePicker1);
        btnDatePicker2 = (ImageButton) findViewById(R.id.DatePicker2);
        btnTimePicker = (ImageButton) findViewById(R.id.TimePicker);
        txtDate = (EditText) findViewById(R.id.myEditText1);
        txtTime = (EditText) findViewById(R.id.myEditText2);

        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();


        // Add a listener to the RadioGroup
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which radio button is selected
                if (checkedId == R.id.RadioButton1) { // Daily selected
                    // Show the daily fields and hide the weekly fields
                    dateEditText.setVisibility(View.VISIBLE);
                    timeEditText.setVisibility(View.VISIBLE);
                    seatEditText.setVisibility(View.VISIBLE);
                    startDateEditText.setVisibility(View.GONE);
                    endDateEditText.setVisibility(View.GONE);
                    driverEditText.setVisibility(View.VISIBLE);
                    dateTextView.setVisibility(View.VISIBLE);
                    timeTextView.setVisibility(View.VISIBLE);
                    seatTextView.setVisibility(View.VISIBLE);
                    startDateTextView.setVisibility(View.GONE);
                    endDateTextView.setVisibility(View.GONE);
                    driverTextView.setVisibility(View.VISIBLE);
                    btnDatePicker.setVisibility(View.VISIBLE);
                    btnDatePicker1.setVisibility(View.GONE);
                    btnDatePicker2.setVisibility(View.GONE);
                    btnTimePicker.setVisibility(View.VISIBLE);

                } else if (checkedId == R.id.RadioButton2) { // Weekly selected
                    // Show the weekly fields and hide the daily fields
                    dateEditText.setVisibility(View.GONE);
                    timeEditText.setVisibility(View.VISIBLE);
                    seatEditText.setVisibility(View.VISIBLE);
                    startDateEditText.setVisibility(View.VISIBLE);
                    endDateEditText.setVisibility(View.VISIBLE);
                    driverEditText.setVisibility(View.VISIBLE);
                    dateTextView.setVisibility(View.GONE);
                    timeTextView.setVisibility(View.VISIBLE);
                    seatTextView.setVisibility(View.VISIBLE);
                    startDateTextView.setVisibility(View.VISIBLE);
                    endDateTextView.setVisibility(View.VISIBLE);
                    driverTextView.setVisibility(View.VISIBLE);
                    btnDatePicker.setVisibility(View.GONE);
                    btnDatePicker1.setVisibility(View.VISIBLE);
                    btnDatePicker2.setVisibility(View.VISIBLE);
                    btnTimePicker.setVisibility(View.VISIBLE);
                }
            }
        });



        Button addButton = findViewById(R.id.AddButton);

        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String startDate = startDateEditText.getText().toString();
                String endDate = endDateEditText.getText().toString();
                String date = dateEditText.getText().toString();
                String time = timeEditText.getText().toString();
                String seat = seatEditText.getText().toString();

                DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ShuttleSchedule");

                if (radioGroup.getCheckedRadioButtonId() == R.id.RadioButton1) { // Daily selected
                    // Add your daily logic here
                    db.child(date).child(time).child("PGE902").setValue(seat).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddScheduleHostelActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AddScheduleHostelActivity.this, "Added Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.RadioButton2) { // Weekly selected
                    // Add your weekly logic here
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar startCalendar = Calendar.getInstance();
                    Calendar endCalendar = Calendar.getInstance();
                    try {
                        startCalendar.setTime(sdf.parse(startDate));
                        endCalendar.setTime(sdf.parse(endDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // Iterate through the days in the date range
                    while (!startCalendar.after(endCalendar)) {
                        // Check if the current day is not Saturday (Calendar.SATURDAY) and not Sunday (Calendar.SUNDAY)
                        if (startCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                            String formattedDay = sdf.format(startCalendar.getTime());
                            // Store data for each day, including date, time, and seat
                            DatabaseReference dayRef = db.child(formattedDay).child(time);
                            dayRef.child("PGE902").setValue(seat);
                        }
                        // Move to the next day
                        startCalendar.add(Calendar.DATE, 1);
                    }

                    Toast.makeText(AddScheduleHostelActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                }
                }

        });


                ImageButton btnDatePicker = findViewById(R.id.DatePicker);
                btnDatePicker.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v == btnDatePicker) {

                            // Get Current Date
                            final Calendar c = Calendar.getInstance();
                            mYear = c.get(Calendar.YEAR);
                            mMonth = c.get(Calendar.MONTH);
                            mDay = c.get(Calendar.DAY_OF_MONTH);


                            DatePickerDialog datePickerDialog = new DatePickerDialog(AddScheduleHostelActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {

                                            txtDate.setText(String.format("%02d", dayOfMonth) + "-" + (monthOfYear + 1) + "-" + year);

                                        }
                                    }, mYear, mMonth, mDay);
                            datePickerDialog.show();
                        }
                    }
                });


                ImageButton btnDatePicker1 = findViewById(R.id.DatePicker1);
                btnDatePicker1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v == btnDatePicker1) {

                            // Get Current Date
                            final Calendar c = Calendar.getInstance();
                            mYear = c.get(Calendar.YEAR);
                            mMonth = c.get(Calendar.MONTH);
                            mDay = c.get(Calendar.DAY_OF_MONTH);


                            DatePickerDialog datePickerDialog = new DatePickerDialog(AddScheduleHostelActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {

                                            startDateEditText.setText(String.format("%02d", dayOfMonth) + "-" + (monthOfYear + 1) + "-" + year);

                                        }
                                    }, mYear, mMonth, mDay);
                            datePickerDialog.show();
                        }
                    }
                });

                ImageButton btnDatePicker2 = findViewById(R.id.DatePicker2);
                btnDatePicker2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v == btnDatePicker2) {

                            // Get Current Date
                            final Calendar c = Calendar.getInstance();
                            mYear = c.get(Calendar.YEAR);
                            mMonth = c.get(Calendar.MONTH);
                            mDay = c.get(Calendar.DAY_OF_MONTH);


                            DatePickerDialog datePickerDialog = new DatePickerDialog(AddScheduleHostelActivity.this,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {

                                            endDateEditText.setText(String.format("%02d", dayOfMonth) + "-" + (monthOfYear + 1) + "-" + year);

                                        }
                                    }, mYear, mMonth, mDay);
                            datePickerDialog.show();
                        }
                    }

                });


                btnTimePicker.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view == btnTimePicker) {

                            // Get Current Time
                            final Calendar c = Calendar.getInstance();
                            mHour = c.get(Calendar.HOUR_OF_DAY);
                            mMinute = c.get(Calendar.MINUTE);
                            mAPM = c.get(Calendar.AM);

                            String amPm;
                            if (mAPM == Calendar.AM) {
                                amPm = "AM";
                            } else {
                                amPm = "PM";
                            }

                            // Launch Time Picker Dialog
                            TimePickerDialog timePickerDialog = new TimePickerDialog(AddScheduleHostelActivity.this,
                                    new TimePickerDialog.OnTimeSetListener() {

                                        @Override
                                        public void onTimeSet(TimePicker view, int hourOfDay,
                                                              int minute) {

                                            txtTime.setText(hourOfDay + ":" + minute + amPm);
                                        }
                                    }, mHour, mMinute, false);
                            timePickerDialog.show();
                        }
                    }
                });

                btnDatePicker1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get Current Date
                        int year = startCalendar.get(Calendar.YEAR);
                        int month = startCalendar.get(Calendar.MONTH);
                        int day = startCalendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(AddScheduleHostelActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        startCalendar.set(year, monthOfYear, dayOfMonth);
                                        startDateEditText.setText(String.format("%02d", dayOfMonth) + "-" + (monthOfYear + 1) + "-" + year);
                                    }
                                }, year, month, day);
                        datePickerDialog.show();
                    }
                });

                // In the onClick listener for the end date image button
                btnDatePicker2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get Current Date
                        int year = endCalendar.get(Calendar.YEAR);
                        int month = endCalendar.get(Calendar.MONTH);
                        int day = endCalendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(AddScheduleHostelActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                        endCalendar.set(year, monthOfYear, dayOfMonth);
                                        endDateEditText.setText(String.format("%02d", dayOfMonth) + "-" + (monthOfYear + 1) + "-" + year);
                                    }
                                }, year, month, day);
                        datePickerDialog.show();
                    }
                });


            }

        }
