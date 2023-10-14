package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class AdminHomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);

        AppCompatButton button5 = findViewById(R.id.button5);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建一个Intent来启动activity_admin_contact_us
                Intent intent = new Intent(AdminHomePageActivity.this, AdminContactUsActivity.class);

                // 可选：如果您想传递数据到activity_admin_contact_us，可以使用Intent的putExtra方法
                // intent.putExtra("key", value);

                // 启动activity_admin_contact_us
                startActivity(intent);
            }
        });

    }
}