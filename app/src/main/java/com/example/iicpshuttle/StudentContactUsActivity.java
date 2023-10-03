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
// 从SharedPreferences中读取数据
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", ""); // 读取邮箱数据
        String userPhone = sharedPreferences.getString("phone", ""); // 读取电话数据

// 在另一个页面中，将数据显示在相应的视图中
        TextView emailTextView = findViewById(R.id.showEmail); // 假设你有一个TextView来显示邮箱
        TextView phoneTextView = findViewById(R.id.showPhone); // 假设你有一个TextView来显示电话

        emailTextView.setText(userEmail); // 将邮箱数据设置到TextView中
        phoneTextView.setText(userPhone); // 将电话数据设置到TextView中


        backIconImageView.setOnClickListener(v -> {
            Intent backIntent = new Intent(StudentContactUsActivity.this, HomeActivity.class);
            startActivity(backIntent);
        });
    }
}