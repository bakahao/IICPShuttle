package com.example.iicpshuttle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminContactUsActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_contact_us);

        ImageView backIconImageView = findViewById(R.id.backIconImageView);

        databaseReference = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Contact Us Information"); // Update the reference to "Contact Us Information"
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences;

        EditText emailEditText;
        EditText phoneEditText;

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        emailEditText = findViewById(R.id.emailEditText_contact);
        phoneEditText = findViewById(R.id.phoneEditText_contact);

        // save previous text
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        emailEditText.setText(sharedPreferences.getString("email", ""));
        phoneEditText.setText(sharedPreferences.getString("phone", ""));

        backIconImageView.setOnClickListener(v -> {
            Intent backIntent = new Intent(AdminContactUsActivity.this, AdminHomePageActivity.class);
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userUid = currentUser.getUid();
                String updatedEmail = emailEditText.getText().toString().trim();
                String updatedPhone = phoneEditText.getText().toString().trim();
                updateUserInformation(userUid, updatedEmail, updatedPhone);
            }
            saveTextToFirebase(emailEditText, phoneEditText);
            startActivity(backIntent);
        });
    }

    private void saveTextToFirebase(EditText emailEditText, EditText phoneEditText) {
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        // Set the data under "Contact Us Information"
        databaseReference.child("email").setValue(email);
        databaseReference.child("phone").setValue(phone);
    }

    private void updateUserInformation(String userUid, String updatedEmail, String updatedPhone) {
        // If you have user information to update, you can do it here
    }
}
