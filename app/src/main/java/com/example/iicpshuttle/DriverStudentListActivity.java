package com.example.iicpshuttle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;

public class DriverStudentListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<User> list;
    private DatabaseReference databaseReference;
    private DriverStudentListAdapter adapter;
    private FirebaseAuth mAuth;
    private String shuttleID, path;
    private ImageView scanQR;

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

        shuttleID = getIntent().getStringExtra("shuttleId");
        path = getIntent().getStringExtra("path");

        getStudentList();

        scanQR = findViewById(R.id.scanQR);
        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScanCode();
            }
        });
    }

    private void getStudentList() {
        if (shuttleID != null) {
            Log.d("FirebaseDebug", "Fetching student list for ShuttleID: " + shuttleID);
            DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("ShuttleSchedule");

            DatabaseReference shuttleRef = db.child(path).child(shuttleID);
            shuttleRef.child("shuttleStudentList").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String studentId = dataSnapshot.getKey();
                            getStudentDetails(studentId);
                        }
                    } else {
                        Log.d("FirebaseDebug", "There is no student booked");
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

    private void getStudentDetails(String studentId) {

        DatabaseReference userRef = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child(studentId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                if (userSnapshot.exists()) {
                    // Assuming StudentListDetails has a default constructor for deserialization
                    User studentDetails = userSnapshot.getValue(User.class);
                    if (studentDetails != null) {
                        list.add(studentDetails);
                        adapter.notifyDataSetChanged();
                        Log.d("FirebaseDebug", "Student details found: " + studentDetails.toString());
                    } else {

                    }
                } else {
                    Log.e("Firebase Error", "No data found for userID: " + studentId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase Error", "Error: " + error.getMessage());
            }
        });
    }

    private void onScanCode(){
        ScanOptions sOptions = new ScanOptions();
        sOptions.setPrompt("Volume up to turn on the flash");
        sOptions.setBeepEnabled(true);
        sOptions.setOrientationLocked(true);
        sOptions.setCaptureActivity(DriverScannerActivity.class);
        resultLauncher.launch(sOptions);
    }

    ActivityResultLauncher<ScanOptions> resultLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null){
            DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
            DatabaseReference shuttleRef = db.child("ShuttleSchedule").child(path).child(shuttleID);
            DatabaseReference userSchRef = db.child("UserSchedule").child(result.getContents()).child("ShuttleList").child(shuttleID);
            DatabaseReference studentRef = shuttleRef.child("shuttleStudentList").child(result.getContents());
            studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        studentRef.setValue(true);
                        userSchRef.child("attendance").setValue(true);
                        Toast.makeText(DriverStudentListActivity.this, "Student Checked In " , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DriverStudentListActivity.this, "Student Not Found " , Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    });
}
