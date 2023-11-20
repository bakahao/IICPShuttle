package com.example.iicpshuttle;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewScheduleActivity extends AppCompatActivity {
    private String date, shuttleTime, shuttleUid, shuttleSeat;
    private String departure;
    private ImageView backViewSchedule;
    private LinearLayout linear;
    private int seatCount=10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        // Initialize the backViewSchedule button and set an onClickListener
        backViewSchedule = findViewById(R.id.backViewSchedule);
        linear = findViewById(R.id.showButton);

        Intent intent = getIntent();
        date = intent.getStringExtra("Date");
        departure = intent.getStringExtra("Departure");

        backViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewScheduleActivity.this, ManageScheduleActivity.class);
                startActivity(intent);
            }
        });


        createButton();


    }



    private void createButton(){

        DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ShuttleSchedule");
        ArrayList<String> shuttleIdList = new ArrayList<>();
        db.child(departure).child(date).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot timeSnapShot: snapshot.getChildren()){
                    for(DataSnapshot dataSnapshot: timeSnapShot.getChildren()){
                        shuttleTime = dataSnapshot.child("shuttleTime").getValue(String.class);
                        if(shuttleTime != null){
                            final String shuttleUid = dataSnapshot.getKey();
                            //shuttleUid = dataSnapshot.getKey();
                            //shuttleIdList.add(shuttleUid);
                            shuttleSeat = dataSnapshot.child("shuttleSeat").getValue(String.class);
                            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
                            params.topMargin = 55;

                            ConstraintLayout buttonLayout = new ConstraintLayout(ViewScheduleActivity.this);
                            //buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
                            buttonLayout.setLayoutParams(params);

                            // Create the button
                            AppCompatButton apc = new AppCompatButton(ViewScheduleActivity.this);
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
                            ImageView seatIcon = new ImageView(ViewScheduleActivity.this);
                            seatIcon.setId(View.generateViewId());
                            seatIcon.setLayoutParams(new LinearLayout.LayoutParams(100,80));

                            seatIcon.setImageResource(R.drawable.seat); // 设置座位图标

                            // Create the seat count text
                            TextView seatCountText = new TextView(ViewScheduleActivity.this);
                            seatCountText.setId(View.generateViewId());
                            seatCountText.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));
                            seatCountText.setPadding(15, 40, 40, 0);
                            seatCountText.setText("Seats: " + shuttleSeat); // Set shuttleSeat value

                            // Add views to button layout
                            buttonLayout.addView(apc);
                            buttonLayout.addView(seatIcon);
                            buttonLayout.addView(seatCountText);

                            ConstraintSet constraintSet = new ConstraintSet();
                            constraintSet.clone(buttonLayout);
                            constraintSet.connect(seatCountText.getId(), ConstraintSet.END, apc.getId(), ConstraintSet.END);
                            constraintSet.connect(seatCountText.getId(), ConstraintSet.TOP, apc.getId(), ConstraintSet.TOP);

                            constraintSet.connect(seatIcon.getId(), ConstraintSet.END, seatCountText.getId(), ConstraintSet.START);
                            constraintSet.connect(seatIcon.getId(), ConstraintSet.TOP, apc.getId(), ConstraintSet.TOP);



                            constraintSet.applyTo(buttonLayout);

                            linear.addView(buttonLayout);
                            // Check seat count and update button availability
                            if (seatCount > 0) {
                                // Enable the button
                                apc.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Get the selected shuttleUid
                                        String selectedShuttleUid = shuttleUid;

                                        String selectedTime = timeSnapShot.getKey(); // Get the selected time from the button
                                        Intent intent = new Intent(ViewScheduleActivity.this, AdminAssignScheduleActivity.class);
                                        intent.putExtra("selectedTime", selectedTime); // Pass the selected time as an extra
                                        intent.putExtra("Date", date);
                                        intent.putExtra("Departure", departure);
                                        intent.putExtra("selectedShuttleUid", selectedShuttleUid);
                                        startActivity(intent);
                                    }
                                });

                            } else {
                                // Disable the button
                                apc.setEnabled(false);
                            }

                        }

                    }





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
