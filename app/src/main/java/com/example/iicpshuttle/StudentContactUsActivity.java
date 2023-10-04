package com.example.iicpshuttle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class StudentContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_contact_us);

        ImageView backIconImageView = findViewById(R.id.backIconImageView);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", ""); // 读取邮箱数据
        String userPhone = sharedPreferences.getString("phone", ""); // 读取电话数据

        TextView emailTextView = findViewById(R.id.showEmail);
        TextView phoneTextView = findViewById(R.id.showPhone);

        emailTextView.setText(userEmail); // replace the email to userEmail(textView)
        phoneTextView.setText(userPhone); // replace the phone to userEmail(textView)


        backIconImageView.setOnClickListener(v -> {
            Intent backIntent = new Intent(StudentContactUsActivity.this, HomeActivity.class);
            startActivity(backIntent);
        });
    }
}