package com.example.iicpshuttle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        numberOfSeats = getSeatCountFromDatabase();
        numberOfButtons = getNumberOfPeopleFromDatabase();
        linear = findViewById(R.id.driverTimeLayout);

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
                for (DataSnapshot timeSnapShot: snapshot.getChildren()){
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
                    ImageView seatIcon = new ImageView(AvailableTimeActivity.this);
                    seatIcon.setId(View.generateViewId());
                    seatIcon.setLayoutParams(new LinearLayout.LayoutParams(
                            50,50));

                    seatIcon.setImageResource(R.drawable.seat); // 设置座位图标

                    // Create the seat count text
                    TextView seatCountText = new TextView(AvailableTimeActivity.this);
                    seatCountText.setId(View.generateViewId());
                    seatCountText.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    int seatCount = getNumberOfPeopleFromDatabase();// 查询数据库获取座位数量
                    seatCountText.setPadding(15,40,40,0);
                    seatCountText.setText("Seats: " + timeSnapShot.child("PGE902").getValue(String.class));




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
                                int buttonId = id_;
                                Intent intent = new Intent(AvailableTimeActivity.this, ConfirmationActivity.class);
                                intent.putExtra("buttonId", buttonId);
                                startActivity(intent);
                            }
                        });
                    } else {
                        // Disable the button
                        apc.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }




    private void createButtons(int count) {
        LinearLayout linear = findViewById(R.id.driverTimeLayout);


        for (int i = 1; i <= count; i++) {
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 55;

            ConstraintLayout buttonLayout = new ConstraintLayout(this);
            //buttonLayout.setOrientation(LinearLayout .HORIZONTAL);
            buttonLayout.setLayoutParams(params);

            // Create the button
            AppCompatButton apc = new AppCompatButton(this);
            apc.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            apc.setId(i);
            apc.setBackgroundResource(R.drawable.rounded_home_button_background); // 设置按钮背景
            apc.setGravity(Gravity.START);
            final int id_ = apc.getId();
            apc.setTextColor(Color.BLACK);
            apc.setPadding(40, 40, 0,0);
            apc.setText("Date and Time " + i);


            // Create the seat icon
            ImageView seatIcon = new ImageView(this);
            seatIcon.setId(View.generateViewId());
            seatIcon.setLayoutParams(new LinearLayout.LayoutParams(
                    50,50));

            seatIcon.setImageResource(R.drawable.seat); // 设置座位图标

            // Create the seat count text
            TextView seatCountText = new TextView(this);
            seatCountText.setId(View.generateViewId());
            seatCountText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            int seatCount = getNumberOfPeopleFromDatabase();// 查询数据库获取座位数量
            seatCountText.setPadding(15,40,40,0);
            seatCountText.setText("Seats: " + seatCount);




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
                        int buttonId = id_;
                        Intent intent = new Intent(AvailableTimeActivity.this, ConfirmationActivity.class);
                        intent.putExtra("buttonId", buttonId);
                        startActivity(intent);
                    }
                });
            } else {
                // Disable the button
                apc.setEnabled(false);
            }
        }
    }


    // 查询数据库获取座位数量的示例方法
    private int getSeatCountFromDatabase() {
        // 在这里执行数据库查询并返回座位数量
        return 5; // 这里使用示例值，您需要根据实际情况进行查询
    }

    private int getNumberOfPeopleFromDatabase() {

        return 5;
    }






}
