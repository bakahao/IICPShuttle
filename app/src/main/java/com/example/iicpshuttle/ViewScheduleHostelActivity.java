package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class ViewScheduleHostelActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule_hostel);

        // Initialize the backViewSchedule button and set an onClickListener
        ImageView  backViewSchedule = findViewById(R.id.backViewSchedule);
        backViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewScheduleHostelActivity.this, ManageScheduleHostelActivity.class);
                startActivity(intent);
            }
        });
    }
}
