package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RequestShuttleHomeActivity extends AppCompatActivity {

    private Button requestShuttle;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_shuttle_home);

        requestShuttle = findViewById(R.id.button1);

        requestShuttle.setOnClickListener(view -> {
            startActivity(new Intent(this, RequestShuttleActivity.class));
        });
    }

}
