package com.example.iicpshuttle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddScheduleHostelActivity extends AppCompatActivity {


    private LinearLayout dLinear, wLinear;
    private RadioGroup dRadioGroup, wRadioGroup;
    private EditText dateEditText, timeEditText, seatEditText, driverEditText, wSeatEditText, wTimeEditText;
    private EditText startDateEditText, endDateEditText, txtDate, txtTime;
    private TextView dateTextView, timeTextView, seatTextView, startDateTextView, endDateTextView, driverTextView;
    private ImageButton btnDatePicker, btnTimePicker, btnDatePicker1, btnDatePicker2, wBtnTimePicker;
    private int mYear, mMonth, mDay, mHour, mMinute, mAPM;
    private Calendar startCalendar, endCalendar;
    private Spinner dDepartureSpinner, wDepartureSpinner;
    private ArrayAdapter<CharSequence> adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule_campus);

        Intent intent = getIntent();
        String departure = intent.getStringExtra("Departure");

        dDepartureSpinner = findViewById(R.id.departureSpinner);
        wDepartureSpinner = findViewById(R.id.wDepartureSpinner);

        if (departure.equals("HostelShuttle")){
            adapter=ArrayAdapter.createFromResource(this, R.array.departureHostel, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }else if(departure.equals("CampusShuttle")){
            adapter=ArrayAdapter.createFromResource(this, R.array.departureCampus, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        dDepartureSpinner.setAdapter(adapter);
        wDepartureSpinner.setAdapter(adapter);

        // Find the UI components
        dRadioGroup = findViewById(R.id.RadioButton);
        wRadioGroup = findViewById(R.id.wRadioButton);
        dateEditText = findViewById(R.id.myEditText1);
        timeEditText = findViewById(R.id.myEditText2);
        wTimeEditText = findViewById(R.id.wTimeEditText);
        seatEditText = findViewById(R.id.myEditText4);
        wSeatEditText = findViewById(R.id.wSeatEditText);
        startDateEditText = findViewById(R.id.wStartDate);
        endDateEditText = findViewById(R.id.wEndDate);
        driverEditText = findViewById(R.id.DriverEditText);
        dateTextView = findViewById(R.id.date);
        timeTextView = findViewById(R.id.time);
        seatTextView = findViewById(R.id.seattext);
        startDateTextView = findViewById(R.id.wStartDateText);
        endDateTextView = findViewById(R.id.wEndDateText);
        driverTextView = findViewById(R.id.drivertext);
        dLinear = findViewById(R.id.DailyContainer);
        wLinear = findViewById(R.id.WeeklyContainer);


        // Find the date picker and time picker UI components
        btnDatePicker = (ImageButton) findViewById(R.id.DatePicker);
        btnDatePicker1 = (ImageButton) findViewById(R.id.wDatePicker);
        btnDatePicker2 = (ImageButton) findViewById(R.id.wDatePicker1);
        btnTimePicker = (ImageButton) findViewById(R.id.TimePicker);
        txtDate = (EditText) findViewById(R.id.myEditText1);
        txtTime = (EditText) findViewById(R.id.myEditText2);
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();


        // Add a listener to the RadioGroup
        dRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which radio button is selected
                if (checkedId == R.id.RadioButton1) { // Daily selected
                    // Show the daily fields and hide the weekly fields
                    //dLinear = findViewById(R.id.DailyContainer);
                    dLinear.setVisibility(View.VISIBLE);
                    wLinear.setVisibility(View.GONE);

                } else if (checkedId == R.id.RadioButton2) { // Weekly selected
                    // Show the weekly fields and hide the daily fields
                    //wLinear = findViewById(R.id.WeeklyContainer);
                    wRadioGroup.check(R.id.wRadioButton2);
                    wLinear.setVisibility(View.VISIBLE);
                    dLinear.setVisibility(View.GONE);

                }
            }
        });

        wRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which radio button is selected
                if (checkedId == R.id.wRadioButton1) { // Daily selected
                    // Show the daily fields and hide the weekly fields
                    //dLinear = findViewById(R.id.DailyContainer);
                    dRadioGroup.check(R.id.RadioButton1);
                    dLinear.setVisibility(View.VISIBLE);
                    wLinear.setVisibility(View.GONE);

                } else if (checkedId == R.id.wRadioButton2) { // Weekly selected
                    // Show the weekly fields and hide the daily fields
                    //wLinear = findViewById(R.id.WeeklyContainer);
                    wLinear.setVisibility(View.VISIBLE);
                    dLinear.setVisibility(View.GONE);

                }
            }
        });



        Button addButton = findViewById(R.id.AddButton);
        Button wAddButton = findViewById(R.id.wAddButton);
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String date = dateEditText.getText().toString();
                String time = timeEditText.getText().toString();
                String seat = seatEditText.getText().toString();
                String departureSpin = dDepartureSpinner.getSelectedItem().toString();

                Shuttle shuttle = new Shuttle("driver", departureSpin, seat, "carPLate", false, date, time);

                DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ShuttleSchedule");
                String uniqKey = db.push().getKey();
                db.child(departure).child(date).child(time).child(uniqKey).setValue(shuttle).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddScheduleHostelActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddScheduleHostelActivity.this, "Added Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
                });


        wAddButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String startDate = startDateEditText.getText().toString();
                    String endDate = endDateEditText.getText().toString();
                    String time = wTimeEditText.getText().toString();
                    String seat = wSeatEditText.getText().toString();
                    String departureSpin = wDepartureSpinner.getSelectedItem().toString();



                    DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ShuttleSchedule");
                    String uniqKey = db.push().getKey();

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
                            Shuttle shuttle = new Shuttle("driver", departureSpin, seat, "carPLate", false, formattedDay, time);
                            // Store data for each day, including date, time, and seat
                            db.child(departure).child(formattedDay).child(time).child(uniqKey).child(seat).setValue(shuttle).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(AddScheduleHostelActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        // Move to the next day
                        startCalendar.add(Calendar.DATE, 1);
                    }


                    Toast.makeText(AddScheduleHostelActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
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


        ImageButton btnDatePicker1 = findViewById(R.id.wDatePicker);
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

        ImageButton btnDatePicker2 = findViewById(R.id.wDatePicker1);
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

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(AddScheduleHostelActivity.this,
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

                                    txtTime.setText(hourOfDay + ":" + String.format("%02d", minute) + amPm);
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
            }
        });

        wBtnTimePicker =findViewById(R.id.wTimePicker);
        wBtnTimePicker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == wBtnTimePicker) {

                    // Get Current Time
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(AddScheduleHostelActivity.this,
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

                                    wTimeEditText.setText(hourOfDay + ":" + String.format("%02d", minute) + amPm);
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