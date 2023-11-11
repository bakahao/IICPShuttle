package com.example.iicpshuttle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btnLogout;
    private Button btnRequestShuttle;
    public static User user;
    public static String userUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseHelper firebaseHelper = new FirebaseHelper();
        user = (User) LoginActivity.getUserData();
        userUID = LoginActivity.getUserUID();

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
