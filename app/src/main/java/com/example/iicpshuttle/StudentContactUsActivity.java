package com.example.iicpshuttle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentContactUsActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_contact_us);

        ImageView backIconImageView = findViewById(R.id.backIconImageView);

        // Initialize the database reference to "Contact Us Information"
        databaseReference = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Contact Us Information");

        TextView emailTextView = findViewById(R.id.showEmail);
        TextView phoneTextView = findViewById(R.id.showPhone);

        // Read email and phone from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userEmail = dataSnapshot.child("email").getValue(String.class);
                    String userPhone = dataSnapshot.child("phone").getValue(String.class);

                    // Display email and phone
                    emailTextView.setText(userEmail);
                    phoneTextView.setText(userPhone);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });

        emailTextView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://mail.google.com/mail/u/0/?ogbl#inbox?compose=new");
            }
        }));

        phoneTextView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneTextView.getText().toString().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(phone)));
                startActivity(intent);
            }
        }));

        backIconImageView.setOnClickListener(v -> {
            Intent backIntent = new Intent(StudentContactUsActivity.this, HomeActivity.class);
            startActivity(backIntent);
        });
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
