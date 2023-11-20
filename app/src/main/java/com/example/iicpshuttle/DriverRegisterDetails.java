package com.example.iicpshuttle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverRegisterDetails extends AppCompatActivity {

    private EditText driverNumberPlateEditText, driverSeatEditText;
    private Button driverSubmitButton;
    private TextView DriverNumberPlateErrorTextView, DriverseatErrorTextView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register_details);
        mAuth = FirebaseAuth.getInstance();

        driverSubmitButton = findViewById(R.id.SubmitDriverDetails);
        driverSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDriverDetails();
            }
        });
    }

    private void registerDriverDetails() {
        driverNumberPlateEditText = findViewById(R.id.driverNumberPlateEditText);
        driverSeatEditText = findViewById(R.id.driverSeatEditText);
        DriverNumberPlateErrorTextView = findViewById(R.id.DriverNumberPlateErrorTextView);
        DriverseatErrorTextView = findViewById(R.id.DriverseatErrorTextView);

        String numberPlate = driverNumberPlateEditText.getText().toString().trim();
        String maximumSeat = driverSeatEditText.getText().toString().trim();

        if (numberPlate.isEmpty() || maximumSeat.isEmpty()) {
            if (numberPlate.isEmpty()) {
                DriverNumberPlateErrorTextView.setVisibility(View.VISIBLE);
            } else {
                DriverNumberPlateErrorTextView.setVisibility(View.GONE);
            }

            if (maximumSeat.isEmpty()) {
                DriverseatErrorTextView.setVisibility(View.VISIBLE);
            } else {
                DriverseatErrorTextView.setVisibility(View.GONE);
            }
            return;
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            DatabaseReference userRef = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

            DatabaseReference userNodeRef = userRef.child("Driver").child(userId);

            DatabaseReference shuttleRef = userNodeRef.child("Shuttle:");

            shuttleRef.child("NumberPlate").setValue(numberPlate);
            shuttleRef.child("MaximumSeat").setValue(maximumSeat);


            Toast.makeText(DriverRegisterDetails.this, "Driver registered successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, AdminHomePageActivity.class);
            startActivity(intent);
        }
    }


    private void onIconClick(View view) {
        // 创建 Intent 以启动 LoginActivity
        Intent intent = new Intent(this, AdminHomePageActivity.class);
        startActivity(intent);
    }

}
