package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RequestShuttleHomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Button requestShuttle;
    private LinearLayout reminderLayout;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_shuttle_home);

        requestShuttle = findViewById(R.id.button1);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        reminderLayout = findViewById(R.id.reminderLinearLayout);

        requestShuttle.setOnClickListener(view -> {
            startActivity(new Intent(this, RequestShuttleActivity.class));
        });

        swipeRefreshLayout.setOnRefreshListener(this); // Set the listener for SwipeRefreshLayout
        getReminder();
    }

    @Override
    public void onRefresh() {

        getReminder();
    }

    public void getReminder(){
        reminderLayout.removeAllViews();
        DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("RequestShuttle");
        db.child(HomeActivity.userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot requestShuttle : snapshot.getChildren()){
                        LocalDate currentDate = LocalDate.now();

                        RequestShuttle reqData = requestShuttle.getValue(RequestShuttle.class);
                        LocalDate reqDate = LocalDate.parse(reqData.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

                        if (currentDate.isEqual(reqDate) || currentDate.isBefore(reqDate)){
                            // Create a LinearLayout to hold the TextViews (same code as before)
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            layoutParams.setMargins(10, 0, 10, 20);

                            LinearLayout linearLayout = new LinearLayout(RequestShuttleHomeActivity.this);
                            linearLayout.setLayoutParams(layoutParams);
                            linearLayout.setOrientation(LinearLayout.VERTICAL);
                            linearLayout.setBackgroundResource(R.drawable.login_rounded_background);
                            linearLayout.setPadding(20, 0, 0, 0);


                            // Create and add TextViews to the LinearLayout
                            String[] texts = {"Departure : " + reqData.getDeparture(), "Date : " + reqData.getDate(),
                                    "Time : " + reqData.getTime(), "Status : " + reqData.getStatus()};
                            for (String text : texts) {
                                TextView textView = new TextView(RequestShuttleHomeActivity.this);
                                textView.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                ));
                                textView.setText(text);
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                textView.setPadding(dpToPx(20), 0, 0, 0); // Set left padding in dp
                                textView.setTextColor(getResources().getColor(R.color.black));
                                textView.setTypeface(ResourcesCompat.getFont(RequestShuttleHomeActivity.this, R.font.inter_regular));

                                linearLayout.addView(textView);
                            }

                            reminderLayout.addView(linearLayout);
                        }

                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
                swipeRefreshLayout.setRefreshing(false);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
