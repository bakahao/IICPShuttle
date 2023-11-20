package com.example.iicpshuttle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminAssignScheduleActivity extends Activity {
    private Spinner spinner;
    private TextView viewCarPlate, viewSeat;
    private DatabaseReference databaseReference, driverDatabaseReference, shuttleScheduleDatabaseReference, driverScheduleRef;
    private ImageView backViewSchedule, deleteIcon;
    private Button assignButton;
    private String selectedTime, selectedDate, departure;
    private String uid, shuttleUid; // Declare uid as a class-level variable
    private Map<String, String> driverMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assign_schedule);

        // Retrieve the selected time and date from the intent
        Intent intent = getIntent();
        selectedTime = intent.getStringExtra("selectedTime");
        selectedDate = intent.getStringExtra("Date");
        departure = intent.getStringExtra("Departure");
        shuttleUid = intent.getStringExtra("selectedShuttleUid");

        spinner = findViewById(R.id.spinner);
        viewCarPlate = findViewById(R.id.textView6);
        viewSeat = findViewById(R.id.viewSeat);
        assignButton = findViewById(R.id.myButton);

        databaseReference = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users");
        driverDatabaseReference = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Driver");
        shuttleScheduleDatabaseReference = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("ShuttleSchedule");
        driverScheduleRef = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("DriverSchedule");

        backViewSchedule = findViewById(R.id.backViewSchedule);
        deleteIcon = findViewById(R.id.deleteIcon);

        backViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminAssignScheduleActivity.this, ViewScheduleActivity.class);
                startActivity(intent);
            }
        });

        // Fetch data from Firebase
        fetchDataFromFirebase();

        // Add an OnItemSelectedListener to the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected driver's name from the spinner
                String selectedDriverName = spinner.getSelectedItem().toString();

                // Use the selected driver's name to find their UID in the database
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String userName = userSnapshot.child("userName").getValue(String.class);

                            if (userName != null && userName.equals(selectedDriverName)) {
                                uid = userSnapshot.getKey(); // Set uid here

                                // Use the UID to retrieve car plate information from the Driver database
                                driverDatabaseReference.child(uid).child("Shuttle").child("NumberPlate").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String carPlate = dataSnapshot.getValue(String.class);
                                        if (carPlate != null) {
                                            viewCarPlate.setText(carPlate); // Update the car plate TextView
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle database read error
                                    }
                                });

                                // Use the UID to retrieve MaximumSeat information from the Driver database
                                driverDatabaseReference.child(uid).child("Shuttle").child("MaximumSeat").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String maximumSeat = dataSnapshot.getValue(String.class);
                                        if (maximumSeat != null) {
                                            viewSeat.setText(maximumSeat.toString()); // Update the seat TextView
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle database read error
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database read error
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected in the spinner
            }
        });

        // Add a click listener to the delete icon
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Construct the correct path to the data to be deleted
                DatabaseReference dataRef = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()
                        .child("ShuttleSchedule").child(departure).child(selectedDate).child(selectedTime).child(shuttleUid);
                dataRef.child("shuttleDriver").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String driverName = snapshot.getValue(String.class);
                            // Attempt to delete the data
                            dataRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AdminAssignScheduleActivity.this, "Time slot deleted successfully from ShuttleSchedule", Toast.LENGTH_SHORT).show();
                                        driverScheduleRef.child(driverMap.get(driverName)).child("ShuttleList").child(shuttleUid).removeValue();
                                    } else {
                                        Toast.makeText(AdminAssignScheduleActivity.this, "Failed to delete time slot from ShuttleSchedule", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected driver's name from the spinner
                String selectedDriverName = spinner.getSelectedItem().toString();
                String carPlate = viewCarPlate.getText().toString();
                String seat = viewSeat.getText().toString();

                shuttleScheduleDatabaseReference.child(departure).child(selectedDate).child(selectedTime).child(shuttleUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Update the driverName and shuttleCarPlate in the ShuttleSchedule
                            String currentDriver = dataSnapshot.child("shuttleDriver").getValue(String.class);
                            long leftSeat = Integer.parseInt(seat) - dataSnapshot.child("shuttleStudentList").getChildrenCount();

                            shuttleScheduleDatabaseReference.child(departure).child(selectedDate).child(selectedTime).child(shuttleUid)
                                    .child("shuttleDriver").setValue(selectedDriverName);
                            shuttleScheduleDatabaseReference.child(departure).child(selectedDate).child(selectedTime).child(shuttleUid)
                                    .child("shuttleCarPlate").setValue(carPlate);
                            shuttleScheduleDatabaseReference.child(departure).child(selectedDate).child(selectedTime).child(shuttleUid)
                                    .child("shuttleSeat").setValue(String.valueOf(leftSeat));

                            driverScheduleRef.child(driverMap.get(currentDriver)).child("ShuttleList").child(shuttleUid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        DriverShuttleSchedule driverShuttleSchedule = snapshot.getValue(DriverShuttleSchedule.class);
                                        driverScheduleRef.child(driverMap.get(currentDriver)).child("ShuttleList").child(shuttleUid).removeValue();
                                        driverScheduleRef.child(driverMap.get(selectedDriverName)).child("ShuttleList").child(shuttleUid).setValue(driverShuttleSchedule);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database read error
                    }
                });
            }
        });

    }

    private void fetchDataFromFirebase() {
        // Fetch driver names from Firebase and populate the spinner
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> driverNames = new ArrayList<>();
                driverMap = new HashMap<>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userRole = userSnapshot.child("userRole").getValue(String.class);
                    String userName = userSnapshot.child("userName").getValue(String.class);
                    String userUID = userSnapshot.getKey();

                    if (userRole != null && userRole.equals("Driver") && userName != null) {
                        driverNames.add(userName);
                        driverMap.put(userName, userUID);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AdminAssignScheduleActivity.this, android.R.layout.simple_spinner_item, driverNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database read error
                Toast.makeText(AdminAssignScheduleActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
