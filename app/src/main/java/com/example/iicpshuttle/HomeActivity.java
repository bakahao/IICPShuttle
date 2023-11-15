package com.example.iicpshuttle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class HomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private FirebaseAuth mAuth;
    private Button btnLogout;
    private Button btnRequestShuttle;
    public static User user;
    public static String userUID;
    private LinearLayout reminderLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
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
                                    StudentReminderShuttle srs = shuttleSnapshot.getValue(StudentReminderShuttle.class);
                                    System.out.println("  Shuttle UID: " + srs.getShuttleDeparture());
                                    System.out.println("    Date: " + srs.getShuttleDate());
                                    // Create a LinearLayout to hold the TextViews (same code as before)
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

                                    reminderLayout.addView(linearLayout);
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

    public void getUserData(){
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);

        String name = sharedPreferences.getString("Username", "");
        String studentID = sharedPreferences.getString("StudentID", "");
        String email = sharedPreferences.getString("Email","");
        String phone = sharedPreferences.getString("Phone", "");

        user = new User(name, "Student", email, phone, studentID);
    }
}
