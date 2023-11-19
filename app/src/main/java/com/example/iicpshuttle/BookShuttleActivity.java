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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

                    Date thisWeekSaturday = getThisWeekSaturday();
                    Calendar calendar = Calendar.getInstance();

                    // Set the time to midnight (00:00:00)
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    // Create a Date object with the modified time
                    Date currDate = calendar.getTime();
                    for (DataSnapshot dateSnapshot: snapshot.getChildren()){
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        try{
                            Date dbDate = sdf.parse(dateSnapshot.getKey());
                            if(dbDate.before(thisWeekSaturday) && (dbDate.after(currDate) || dbDate.equals(currDate))){
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
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Date getThisWeekSaturday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            // Add 7 days to get the next Saturday
            calendar.add(Calendar.DAY_OF_MONTH, 7);
        } else {
            // Calculate the days until Saturday
            int daysToSaturday = (Calendar.SATURDAY - dayOfWeek + 7) % 7;
            calendar.add(Calendar.DAY_OF_MONTH, daysToSaturday);
        }

        return calendar.getTime();
    }


}


