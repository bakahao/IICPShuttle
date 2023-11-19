package com.example.iicpshuttle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;

public class HomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private FirebaseAuth mAuth;
    private Button btnLogout;
    private Button btnRequestShuttle;
    public static User user;
    public static String userUID;
    private LinearLayout reminderLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PopupWindow popupWindow;
    private static final String SHUTTLE_REMINDER = "Shuttle_Reminder";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        swipeRefreshLayout = findViewById(R.id.homeSwipeRefreshLayout);
        reminderLayout = findViewById(R.id.reminderLinearLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        user = (User) LoginActivity.getUserData();
        userUID = LoginActivity.getUserUID();
        getReminder();

        //For testing -- cf
        mAuth = FirebaseAuth.getInstance();
        btnLogout = findViewById(R.id.button6);

        btnLogout.setOnClickListener(view -> {
            clearSharedPreferences();
            mAuth.signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        });


        btnRequestShuttle = findViewById(R.id.button4);
        btnRequestShuttle.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this, RequestShuttleHomeActivity.class));
        });

        AppCompatButton button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, StudentContactUsActivity.class);
                startActivity(intent);
            }
        });


    }

    public void onBookHostelShuttleClicked(View view){
        Intent intent = new Intent(this, BookShuttleActivity.class);
        intent.putExtra("Departure", "HostelShuttle");
        startActivity(intent);
    }

    public void onBookCampusShuttleCLicked(View view){
        Intent intent = new Intent(this, BookShuttleActivity.class);
        intent.putExtra("Departure", "CampusShuttle");


    @Override
    public void onRefresh() {

        getReminder();
    }

    public void getReminder(){
        reminderLayout.removeAllViews();
        DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("UserSchedule");
        db.child(HomeActivity.userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    DatabaseReference shuttleListRef = snapshot.child("ShuttleList").getRef();
                    Query query = shuttleListRef.orderByChild("shuttleTimeStamp");
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for (DataSnapshot shuttleSnapshot:snapshot.getChildren()){
                                    if (getBooleanFromSharedPreferences(shuttleSnapshot.getKey(), true)){
                                        StudentReminderShuttle srs = shuttleSnapshot.getValue(StudentReminderShuttle.class);
                                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                        );
                                        layoutParams.setMargins(10, 0, 10, 20);

                                        LinearLayout linearLayout = new LinearLayout(HomeActivity.this);
                                        linearLayout.setLayoutParams(layoutParams);
                                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                                        linearLayout.setBackgroundResource(R.drawable.login_rounded_background);
                                        linearLayout.setPadding(20, 0, 0, 0);


                                        // Create and add TextViews to the LinearLayout
                                        String[] texts = {"Departure : " + srs.getShuttleDeparture(), "Date : " + srs.getShuttleDate(),
                                                "Time : " + srs.getShuttleTime(), "Attendance : " + srs.isAttendance()};
                                        for (String text : texts) {
                                            TextView textView = new TextView(HomeActivity.this);
                                            textView.setLayoutParams(new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                            ));
                                            textView.setText(text);
                                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                            textView.setPadding(dpToPx(20), 0, 0, 0); // Set left padding in dp
                                            textView.setTextColor(getResources().getColor(R.color.black));
                                            textView.setTypeface(ResourcesCompat.getFont(HomeActivity.this, R.font.inter_regular));

                                            linearLayout.addView(textView);
                                        }

                                        linearLayout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String date = srs.getShuttleDate();
                                                String departure = srs.getShuttleDeparture();
                                                String time = srs.getShuttleTime();
                                                boolean isAttendance = srs.isAttendance();
                                                String shuttleID = shuttleSnapshot.getKey();
                                                showReminder(date, time, departure, isAttendance, shuttleID);
                                            }
                                        });

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


    public void onBookHostelShuttleClicked(View view){
        Intent intent = new Intent(this, BookShuttleActivity.class);
        intent.putExtra("Departure", "HostelShuttle");

        startActivity(intent);
    }

    public void onBookCampusShuttleCLicked(View view){
        Intent intent = new Intent(this, BookShuttleActivity.class);
        intent.putExtra("Departure", "CampusShuttle");
        startActivity(intent);
    }
    public void onScanMeClicked(View view){
        Intent intent = new Intent(this, ScanMeActivity.class);
        startActivity(intent);
    }

    private void showReminder(String date, String time, String departure, boolean isAttendance, String shuttleUID){
        ReminderPopupView reminderPopupView = new ReminderPopupView(this, null);

        // Set the confirmation message
        reminderPopupView.setReminderMessage(date, time, departure, isAttendance);

        // Create a PopupWindow
        popupWindow = new PopupWindow(
                reminderPopupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        DatabaseReference db = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        // Set listeners for "Yes" and "No" buttons
        reminderPopupView.setOnReminderListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle "Cancel Booking" button click
                        String path;
                        if (departure.startsWith("IICP")){
                            path = "CampusShuttle/" + date + "/" + time + "/" + shuttleUID;
                        } else {
                            path = "HostelShuttle/" + date + "/" + time + "/" + shuttleUID;
                        }
                        DatabaseReference shuttleStudentRef = db.child("ShuttleSchedule").child(path);
                        shuttleStudentRef.child("shuttleStudentList").child(userUID).removeValue().addOnCompleteListener(task -> {
                           if (task.isSuccessful()){
                               shuttleStudentRef.child("shuttleSeat").addListenerForSingleValueEvent(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                       if (snapshot.exists()) {
                                           int seat = Integer.parseInt(snapshot.getValue(String.class)) + 1;
                                           shuttleStudentRef.child("shuttleSeat").setValue(String.valueOf(seat));
                                       }
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {

                                   }
                               });
                           }
                        });
                        db.child("UserSchedule").child(userUID).child("ShuttleList").child(shuttleUID).removeValue();
                        Toast.makeText(HomeActivity.this, "The shuttle canceled", Toast.LENGTH_SHORT).show();
                        getReminder();
                        popupWindow.dismiss();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle "Remove" button click
                        saveToSharedPreferences(shuttleUID, false);
                        getReminder();
                        popupWindow.dismiss();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Handle "Back" button click
                        popupWindow.dismiss();
                    }
                }
        );

        // Set background drawable to allow dismissal on outside touch
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));

        // Set focusability to allow interaction with the PopupWindow
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(findViewById(R.id.studentHomeLayout), Gravity.CENTER, 0, 0);
    }

    // Method to save a boolean value to SharedPreferences
    private void saveToSharedPreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHUTTLE_REMINDER,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    // Method to retrieve a boolean value from SharedPreferences
    private boolean getBooleanFromSharedPreferences(String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHUTTLE_REMINDER,MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    private void clearSharedPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHUTTLE_REMINDER,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
