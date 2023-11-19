package com.example.iicpshuttle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddScheduleHostelActivity extends AppCompatActivity {

    private LinearLayout dLinear, wLinear;
    private RadioGroup dRadioGroup, wRadioGroup;
    private EditText dateEditText, timeEditText, seatEditText,  wSeatEditText, wTimeEditText;
    private EditText startDateEditText, endDateEditText, txtDate, txtTime;
    private TextView dateTextView, timeTextView, seatTextView, startDateTextView, endDateTextView, driverTextView;
    private ImageButton btnDatePicker, btnTimePicker, btnDatePicker1, btnDatePicker2, wBtnTimePicker;
    private int mYear, mMonth, mDay, mHour, mMinute, mAPM;
    private Calendar startCalendar, endCalendar;
    private Spinner dDepartureSpinner, wDepartureSpinner, dDriverSpinner, wDriverSpinner;
    private ArrayAdapter<CharSequence> adapter;
    private Map <String,String> driverMap, seatMap;
    private List<String> driver;

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

        dDriverSpinner = findViewById(R.id.dDriverSpinner);
        wDriverSpinner = findViewById(R.id.wDriverSpinner);

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
                String driver = dDriverSpinner.getSelectedItem().toString();
                String departureSpin = dDepartureSpinner.getSelectedItem().toString();

                
                Shuttle shuttle = new Shuttle(driver, departureSpin, seat, "carPlate",  date, time);

                DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                String uniqKey = db.push().getKey();
                db.child("ShuttleSchedule").child(departure).child(date).child(time).child(uniqKey).setValue(shuttle).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddScheduleHostelActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddScheduleHostelActivity.this, "Added Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                DriverShuttleSchedule drivers = new DriverShuttleSchedule(date, time, departureSpin, seat);
                db.child("DriverSchedule").child(driverMap.get(driver)).child("ShuttleList").child(uniqKey).setValue(drivers);
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
                    String driver = wDriverSpinner.getSelectedItem().toString();

                    DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();


                    // Add your weekly logic here
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar startCalendar = Calendar.getInstance();
                    Calendar endCalendar = Calendar.getInstance();
                    Calendar currentDate = Calendar.getInstance();
                    try {
                        startCalendar.setTime(sdf.parse(startDate));
                        endCalendar.setTime(sdf.parse(endDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    // Iterate through the days in the date range
                    while (!startCalendar.after(endCalendar) && !startCalendar.before(currentDate)) {
                        // Check if the current day is not Saturday (Calendar.SATURDAY) and not Sunday (Calendar.SUNDAY)
                        if (startCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                            String formattedDay = sdf.format(startCalendar.getTime());
                            String uniqKey = db.push().getKey();
                            Shuttle shuttle = new Shuttle(driver, departureSpin, seat, "carPlate",  formattedDay, time);
                            // Store data for each day, including date, time, and seat
                            db.child("ShuttleSchedule").child(departure).child(formattedDay).child(time).child(uniqKey).setValue(shuttle).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(AddScheduleHostelActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                            DriverShuttleSchedule drivers = new DriverShuttleSchedule(formattedDay, time, departureSpin, seat);
                            db.child("DriverSchedule").child(driverMap.get(driver)).child("ShuttleList").child(uniqKey).setValue(drivers);
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

        dDriverSpinner = findViewById(R.id.dDriverSpinner);
        wDriverSpinner = findViewById(R.id.wDriverSpinner);

        // Assuming you have Firebase Auth initialized, get the currently logged-in user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        // Assuming the database reference to Drivers node
        DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        seatMap = new HashMap<>();
        db.child("DriverShuttleSchedule").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot driverSnapshot : snapshot.getChildren()){
                    seatMap.put(driverSnapshot.getKey(),driverSnapshot.child("Shuttle").child("MaximumSeat").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                driver = new ArrayList<>();
                driverMap = new HashMap<>();


                for (String uid: seatMap.keySet()) {
                    DataSnapshot userSnapshot = dataSnapshot.child(uid);
                    String userKey = userSnapshot.getKey(); // Retrieve the unique key for each driver

                    // Fetch the userRole directly from the userSnapshot
                    String userRole = userSnapshot.child("userRole").getValue(String.class);

                    // Compare userRole to "DriverShuttleSchedule"

                    // If the userRole is "DriverShuttleSchedule", add the corresponding value to the list
                    String drivers = userSnapshot.child("userName").getValue(String.class);
                    driver.add(drivers);
                    driverMap.put(drivers,userSnapshot.getKey());


                }

                // Assuming dDriverSpinner is your Spinner in the activity layout
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddScheduleHostelActivity.this, android.R.layout.simple_spinner_item, driver);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dDriverSpinner.setAdapter(adapter);
                wDriverSpinner.setAdapter(adapter);

            }




            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle an error in fetching data
            }
        });

        dDriverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = driver.get(position);
                String uid = driverMap.get(name);
                seatEditText.setText(seatMap.get(uid));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        wDriverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = driver.get(position);
                String uid = driverMap.get(name);
                wSeatEditText.setText(seatMap.get(uid));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ImageView backToHome = findViewById(R.id.backToHome);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddScheduleHostelActivity.this, ManageScheduleHostelActivity.class);
                startActivity(intent);
            }
        });





    }

  }