package com.example.iicpshuttle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AvailableTimeActivity extends AppCompatActivity {

    private ConstraintLayout constraintLayout;
    private LinearLayout linear;
    private int numberOfSeats;
    private int numberOfButtons;
    private int seatCount=20;

    private String date;
    private String departure;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_time);

        constraintLayout = findViewById(R.id.drivertime);
        linear = findViewById(R.id.studentAvailableTimeLayout);

        // Retrieve time and seat count from the intent
        Intent intent = getIntent();
        date = intent.getStringExtra("Date");
        departure = intent.getStringExtra("Departure");

        createButton();

    }

    private void createButton(){
        DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ShuttleSchedule");
        db.child(departure).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> timeStr = new ArrayList<>();
                for (DataSnapshot timeSnapShot: snapshot.getChildren()){
                    timeStr.add(timeSnapShot.getKey());
                }

                Collections.sort(timeStr, new Comparator<String>() {
                    SimpleDateFormat sdf = new SimpleDateFormat("h:mmaa", Locale.US);

                    @Override
                    public int compare(String time1, String time2) {
                        try {
                            Date date1 = sdf.parse(time1);
                            Date date2 = sdf.parse(time2);
                            return Objects.requireNonNull(date1).compareTo(date2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return 0;
                        }
                    }
                });


                for (String time : timeStr){
                    DataSnapshot timeSnapShot = snapshot.child(time);
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 55;

                    ConstraintLayout buttonLayout = new ConstraintLayout(AvailableTimeActivity.this);
                    //buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
                    buttonLayout.setLayoutParams(params);

                    // Create the button
                    AppCompatButton apc = new AppCompatButton(AvailableTimeActivity.this);
                    apc.setLayoutParams(new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));
                    apc.setId(timeSnapShot.getKey().hashCode());
                    apc.setBackgroundResource(R.drawable.rounded_home_button_background); // 设置按钮背景
                    apc.setGravity(Gravity.START);
                    final int id_ = apc.getId();
                    apc.setTextColor(Color.BLACK);
                    apc.setPadding(40, 40, 0,0);
                    apc.setText(timeSnapShot.getKey());


                    // Create the seat icon
                    ImageView shuttleIcon = new ImageView(AvailableTimeActivity.this);
                    shuttleIcon.setId(View.generateViewId());
                    shuttleIcon.setLayoutParams(new LinearLayout.LayoutParams(
                            70,70));

                    shuttleIcon.setImageResource(R.drawable.shuttle);

                    // Create the seat count text
                    TextView seatCountText = new TextView(AvailableTimeActivity.this);
                    seatCountText.setId(View.generateViewId());
                    seatCountText.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    seatCountText.setPadding(15,40,40,0);
                    seatCountText.setText("Shuttle: " + timeSnapShot.getChildrenCount());
                    // Add views to button layout
                    buttonLayout.addView(apc);
                    buttonLayout.addView(shuttleIcon);
                    buttonLayout.addView(seatCountText);

                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(buttonLayout);
                    constraintSet.connect(seatCountText.getId(), ConstraintSet.END, apc.getId(), ConstraintSet.END);
                    //constraintSet.connect(seatCountText.getId(), ConstraintSet.TOP, apc.getId(), ConstraintSet.TOP);

                    constraintSet.connect(shuttleIcon.getId(), ConstraintSet.END, seatCountText.getId(), ConstraintSet.START);
                    constraintSet.centerVertically(shuttleIcon.getId(), ConstraintSet.PARENT_ID);



                    constraintSet.applyTo(buttonLayout);

                    linear.addView(buttonLayout);
                    apc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int buttonId = id_;
                            Intent intent = new Intent(AvailableTimeActivity.this, AvailableDepartureActivity.class);
                            intent.putExtra("path", departure + "/" + date + "/" + timeSnapShot.getKey());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
