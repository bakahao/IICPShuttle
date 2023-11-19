package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminHomePageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ScheduleAdmin> list;
    DatabaseReference databaseReference;
    AdapterAdmin adapter;
    FirebaseAuth mAuth;
    Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);


        AppCompatButton button5 = findViewById(R.id.button5);
        AppCompatButton buttonManageScheduleHostel = findViewById(R.id.buttonManageScheduleHostel);
        AppCompatButton buttonManageScheduleCampus = findViewById(R.id.buttonManageScheduleCampus);

        mAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.button6);

        btnLogout.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(AdminHomePageActivity.this, LoginActivity.class));
        });



        AppCompatButton button3 = findViewById(R.id.button3);


        AppCompatButton button4 = findViewById(R.id.button4);


        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePageActivity.this, AdminContactUsActivity.class);

                startActivity(intent);
            }
        });


        buttonManageScheduleHostel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePageActivity.this, ManageScheduleHostelActivity.class);
                intent.putExtra("Departure", "HostelShuttle");
                startActivity(intent);
            }
        });


        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePageActivity.this, DriverRegisterActivity.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePageActivity.this, AdminRequestShuttle.class);
        buttonManageScheduleCampus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePageActivity.this, ManageScheduleHostelActivity.class);
                intent.putExtra("Departure", "CampusShuttle");
                startActivity(intent);
            }
        });
        

        recyclerView = findViewById(R.id.RecyclerViewScheduleAdmin);
        databaseReference  = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("RequestShuttle");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new AdapterAdmin(this,list);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    for (DataSnapshot requestSnapShot : dataSnapshot.getChildren()){
                        ScheduleAdmin scheduleAdmin  = requestSnapShot.getValue(ScheduleAdmin.class);

                        if (scheduleAdmin != null && "Pending".equals(scheduleAdmin.getStatus())) {
                            list.add(scheduleAdmin);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase Error", "Error: " + error.getMessage());
            }

        });

    }
}




