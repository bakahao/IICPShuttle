package com.example.iicpshuttle;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AvailableDepartureActivity extends AppCompatActivity {

    private String path, departurePath;
    private LinearLayout linear;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_departure);

        linear = findViewById(R.id.studentAvailableDepartureLayout);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        createButton();

        ImageView backToHome = findViewById(R.id.backToHome);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AvailableDepartureActivity.this, AvailableTimeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createButton(){
        DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ShuttleSchedule");
        db.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot shuttleSnapshot:snapshot.getChildren()){
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 55;

                    ConstraintLayout buttonLayout = new ConstraintLayout(AvailableDepartureActivity.this);
                    //buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
                    buttonLayout.setLayoutParams(params);

                    // Create the button
                    AppCompatButton apc = new AppCompatButton(AvailableDepartureActivity.this);
                    apc.setLayoutParams(new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    ));
                    apc.setId(shuttleSnapshot.getKey().hashCode());
                    apc.setBackgroundResource(R.drawable.rounded_home_button_background); // 设置按钮背景
                    apc.setGravity(Gravity.START);
                    final int id_ = apc.getId();
                    apc.setTextColor(Color.BLACK);
                    apc.setPadding(40, 40, 0,0);
                    apc.setText(shuttleSnapshot.child("shuttleDeparture").getValue(String.class));


                    // Create the seat icon
                    ImageView shuttleIcon = new ImageView(AvailableDepartureActivity.this);
                    shuttleIcon.setId(View.generateViewId());
                    shuttleIcon.setLayoutParams(new LinearLayout.LayoutParams(
                            70,70));

                    shuttleIcon.setImageResource(R.drawable.student_seat);

                    // Create the seat count text
                    TextView seatCountText = new TextView(AvailableDepartureActivity.this);
                    seatCountText.setId(View.generateViewId());
                    seatCountText.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    seatCountText.setPadding(15,40,40,0);
                    seatCountText.setText("Seat: " + shuttleSnapshot.child("shuttleSeat").getValue());
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
                            String date = shuttleSnapshot.child("shuttleDate").getValue(String.class);
                            String time = shuttleSnapshot.child("shuttleTime").getValue(String.class);
                            String departure = shuttleSnapshot.child("shuttleDeparture").getValue(String.class);
                            String stuID = HomeActivity.user.getStudentID();
                            String shuttleUID = shuttleSnapshot.getKey();

                            showConfirmation(date, time, departure, stuID, shuttleUID);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showConfirmation(String date, String time, String departure, String stuID, String shuttleUID){
        ConfirmationPopupView confirmationPopupView = new ConfirmationPopupView(this, null);

        // Set the confirmation message
        confirmationPopupView.setConfirmationMessage(date, time, departure, stuID);

        // Create a PopupWindow
        popupWindow = new PopupWindow(
                confirmationPopupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Set listeners for "Yes" and "No" buttons
        confirmationPopupView.setOnConfirmationListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle "Yes" button click
                        DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                        db.child("ShuttleSchedule").child(path).child(shuttleUID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    int seat = Integer.parseInt(snapshot.child("shuttleSeat").getValue(String.class)) - 1;
                                    db.child("ShuttleSchedule").child(path).child(shuttleUID).child("shuttleSeat").setValue(String.valueOf(seat));
                                    db.child("ShuttleSchedule").child(path).child(shuttleUID).child("shuttleStudentList").child(HomeActivity.userUID).setValue(false);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        String dateTimeStr = date + " " + time;
                        try {
                            long timeStamp = convertToTimestamp(dateTimeStr);
                            StudentReminderShuttle studentReminderShuttle = new StudentReminderShuttle(date, time, departure, false, timeStamp);
                            db.child("UserSchedule").child(HomeActivity.userUID).child("ShuttleList").child(shuttleUID).setValue(studentReminderShuttle);

                            Toast.makeText(AvailableDepartureActivity.this, "Confirmed", Toast.LENGTH_SHORT).show();
                            popupWindow.dismiss();
                            Intent intent = new Intent(AvailableDepartureActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle "No" button click
                        Toast.makeText(AvailableDepartureActivity.this, "Canceled", Toast.LENGTH_SHORT).show();

                        popupWindow.dismiss();

                    }
                }
        );

        // Set background drawable to allow dismissal on outside touch
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Set focusability to allow interaction with the PopupWindow
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(findViewById(R.id.studentAvailableDepartureLayout), Gravity.CENTER, 0, 0);
    }

    private long convertToTimestamp(String dateTimeString)throws ParseException {
        dateTimeString = dateTimeString.replaceAll("([APMapm]{2})", "");
        dateTimeString = dateTimeString.replaceAll("\\b(\\d):(\\d{2})", "0$1:$2");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyHH:mm");
        Date date = dateFormat.parse(dateTimeString);

        // Convert Date to timestamp (milliseconds since January 1, 1970, 00:00:00 GMT)
        return date.getTime();
    }
}
