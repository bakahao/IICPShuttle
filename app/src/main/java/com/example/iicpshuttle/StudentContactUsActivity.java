package com.example.iicpshuttle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

        emailTextView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://mail.google.com/mail/u/0/?ogbl#inbox?compose=new");
            }
        }));

        phoneTextView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone;
                phone = phoneTextView.getText().toString().trim();

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