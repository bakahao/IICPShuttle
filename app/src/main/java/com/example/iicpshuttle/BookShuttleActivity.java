package com.example.iicpshuttle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookShuttleActivity extends AppCompatActivity {


    private LinearLayout linear;
    private ConstraintLayout constraintLayout;
    private int numberOfButtons;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookshuttle);

        constraintLayout = findViewById(R.id.hello);
        numberOfButtons = getNumberOfPeopleFromDatabase();
        linear = findViewById(R.id.buttonLayout);

        // Create Button
        Intent intent = getIntent();
        String departure = intent.getStringExtra("Departure");

        createButton(departure);
    }


    private int getNumberOfPeopleFromDatabase() {

        return 10;
    }

    private void createButton(String departure){


        DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ShuttleSchedule");
        db.child(departure).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dateSnapshot: snapshot.getChildren()){

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.topMargin = 50;
//

                        AppCompatButton apc = new AppCompatButton(BookShuttleActivity.this);
                        apc.setId(dateSnapshot.getKey().hashCode());
                        apc.setBackgroundResource(R.drawable.rounded_home_button_background);
                        final int id_ = apc.getId();
                        apc.setText(dateSnapshot.getKey());
                        apc.setTextColor(Color.BLACK);
                        linear.addView(apc, params);
                        AppCompatButton btn = findViewById(id_);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "Button " + id_, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BookShuttleActivity.this, AvailableTimeActivity.class);
                                intent.putExtra("Departure", departure);
                                intent.putExtra("Date", dateSnapshot.getKey());
                                startActivity(intent);

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void createButtons(int count) {
        LinearLayout linear = findViewById(R.id.buttonLayout);


        for (int i = 1; i <= count; i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 50;
//

            AppCompatButton apc = new AppCompatButton(this);
            apc.setId(i);
            apc.setBackgroundResource(R.drawable.rounded_home_button_background);
            final int id_ = apc.getId();
            apc.setText("Button " + id_);
            apc.setTextColor(Color.BLACK);
            linear.addView(apc, params);
            AppCompatButton btn = findViewById(id_);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Button " + id_, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BookShuttleActivity.this, AvailableTimeActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

}


