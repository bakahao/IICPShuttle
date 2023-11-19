package com.example.iicpshuttle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
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

        databaseReference = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
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

            // Check if the user is signed in
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userUid = currentUser.getUid();
                // Get updated email and phone data
                String updatedEmail = emailEditText.getText().toString().trim();
                String updatedPhone = phoneEditText.getText().toString().trim();

                // Update user information in the "Users" node using the UID
                updateUserInformation(userUid, updatedEmail, updatedPhone);
            }
            //saveTextToFirebase(emailEditText, phoneEditText);

            saveTextToSharedPreferences(sharedPreferences, emailEditText, phoneEditText);
            startActivity(backIntent);
        });
    }
    private void saveTextToFirebase(EditText emailEditText, EditText phoneEditText) {
        String name = "Admin";
        String role = "Admin";
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        // Create a new child node in the "Users" node with a unique ID (you can use push() to generate one)
        String userId = databaseReference.push().getKey();

        // Create a data object
        Admin admin = new Admin(name, role, email, phone); // Create a User class to store email and phone

        // Set the data in the database
        databaseReference.child(userId).setValue(admin);
    }
    private void updateUserInformation(String userUid, String updatedEmail, String updatedPhone) {
        databaseReference.child(userUid).child("email").setValue(updatedEmail);
        databaseReference.child(userUid).child("phone").setValue(updatedPhone);
    }


    private void saveTextToSharedPreferences(SharedPreferences sharedPreferences, EditText emailEditText,
                                             EditText phoneEditText) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", emailEditText.getText().toString());
        editor.putString("phone", phoneEditText.getText().toString());
        editor.apply();
    }

}
