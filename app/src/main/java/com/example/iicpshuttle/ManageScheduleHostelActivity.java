package com.example.iicpshuttle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class ManageScheduleHostelActivity extends AppCompatActivity {
    private LinearLayout buttonContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_schedule_hostel);

        buttonContainer = findViewById(R.id.buttonScheduleContainer);

        // Retrieve the selected date from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("selectedDate")) {
            String selectedDate = intent.getStringExtra("selectedDate");
            addNewDateButton(selectedDate);
        }


        ImageView addScheduleImageView = findViewById(R.id.addScheduleImage);

        addScheduleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageScheduleHostelActivity.this, AddScheduleHostelActivity.class);
                startActivity(intent);
            }
        });

        ImageView backToHome = findViewById(R.id.backToHome);

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageScheduleHostelActivity.this, AdminHomePageActivity.class);
                startActivity(intent);
            }
        });


        buttonContainer = findViewById(R.id.buttonScheduleContainer);
    }

    private Button createStyledButton(String text) {
        Button button = new Button(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        button.setText(text);
        button.setBackgroundResource(R.drawable.rounded_home_button_background); // Set the background
        button.setTextSize(25); // Set the text size
        button.setMinHeight(55); // Set the minimum height
        button.setElevation(4); // Set the elevation
        return button;
    }

    public void addNewDateButton(String selectedDate) {
        Button dateButton = createStyledButton(selectedDate);
        buttonContainer.addView(dateButton);
    }
}
