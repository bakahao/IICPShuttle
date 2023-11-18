package com.example.iicpshuttle;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DriverStudentListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<StudentListDetails> list;
    DatabaseReference databaseReference;
    DriverStudentListAdapter adapter;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_student_list);

        recyclerView = findViewById(R.id.RecyclerViewStudentList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new DriverStudentListAdapter(this, list);
        recyclerView.setAdapter(adapter);

        String shuttleId = getIntent().getStringExtra("shuttleId");

        DatabaseReference shuttleRef = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ShuttleSchedule");

        shuttleRef.child("CampusShuttle").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot timeSnapshot : dateSnapshot.getChildren()) {
                            String shuttleId = timeSnapshot.getKey();
                            getStudentList(shuttleId);
                        }
                    }
                } else {
                    Log.e("Firebase Error", "No shuttles found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase Error", "Error: " + error.getMessage());
            }
        });
    }

    private void getStudentList(String shuttleID) {
        if (shuttleID != null) {
            Log.d("FirebaseDebug", "Fetching student list for ShuttleID: " + shuttleID);
            DatabaseReference shuttleRef = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("ShuttleSchedule");

            DatabaseReference campusShuttleRef = shuttleRef.child("HostelShuttle");
            campusShuttleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot campusSnapshot) {
                    if (campusSnapshot.exists()) {
                        DataSnapshot studentListSnapshot = campusSnapshot.child(shuttleID).child("ShuttleStudentList");
                        for (DataSnapshot dataSnapshot : studentListSnapshot.getChildren()) {
                            String studentId = dataSnapshot.getKey();
                            getStudentDetails(studentId);
                        }
                    } else {
                        DatabaseReference hostelShuttleRef = shuttleRef.child("CampusShuttle").child(shuttleID);
                        hostelShuttleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot hostelSnapshot) {
                                if (hostelSnapshot.exists()) {
                                    // Shuttle found under HostelShuttle, fetch student list
                                    DataSnapshot studentListSnapshot = hostelSnapshot.child("ShuttleStudentList");
                                    for (DataSnapshot dataSnapshot : studentListSnapshot.getChildren()) {
                                        String studentId = dataSnapshot.getKey();
                                        getStudentDetails(studentId);
                                    }
                                } else {
                                    Log.e("Firebase Error", "Shuttle not found for ID: " + shuttleID);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("Firebase Error", "Error: " + error.getMessage());
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase Error", "Error: " + error.getMessage());
                }
            });
        } else {
            Log.e("FirebaseDebug", "shuttleID is null");
        }
    }

    private void getStudentDetails(String shuttleId) {
        Log.d("FirebaseDebug", "Fetching details for shuttleId: " + shuttleId);

        DatabaseReference userRef = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child("ShuttleSchedule").child("CampusShuttle").child(shuttleId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                if (userSnapshot.exists()) {
                    // Assuming StudentListDetails has a default constructor for deserialization
                    StudentListDetails studentDetails = userSnapshot.getValue(StudentListDetails.class);
                    if (studentDetails != null) {
                        list.add(studentDetails);
                        adapter.notifyDataSetChanged();
                        Log.d("FirebaseDebug", "Student details found: " + studentDetails.toString());
                    } else {

                    }
                } else {
                    Log.e("Firebase Error", "No data found for shuttleId: " + shuttleId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase Error", "Error: " + error.getMessage());
            }
        });
    }

    private void fetchStudentDetailsFromUsers(String studentId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child(studentId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                if (userSnapshot.exists()) {
                    StudentListDetails studentDetails = userSnapshot.getValue(StudentListDetails.class);
                    if (studentDetails != null) {
                        list.add(studentDetails);
                        adapter.notifyDataSetChanged();
                        Log.d("FirebaseDebug", "Student details found: " + studentDetails.toString());
                    } else {
                        Log.e("Firebase Error", "Student details is null for studentId: " + studentId);
                    }
                } else {
                    Log.e("Firebase Error", "No data found for studentId: " + studentId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase Error", "Error: " + error.getMessage());
            }
        });
    }
}
