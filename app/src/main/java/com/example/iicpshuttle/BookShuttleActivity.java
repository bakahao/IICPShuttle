package com.example.iicpshuttle;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class BookShuttleActivity extends AppCompatActivity {



    private ConstraintLayout constraintLayout;
    private int numberOfButtons;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookshuttle);

        constraintLayout = findViewById(R.id.hello);
        numberOfButtons = getNumberOfPeopleFromDatabase();

        // Create Button
        createButtons(numberOfButtons);
    }


    private int getNumberOfPeopleFromDatabase() {

        return 10;
    }


    private void createButtons(int count) {
        LinearLayout linear = findViewById(R.id.buttonLayout);
        for (int i = 1; i <= count; i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = 50;
//                Button btn = new Button(this);
//                btn.setId(i);
//                final int id_ = btn.getId();
//                btn.setText("Button " + id_);
//                btn.setBackgroundColor(Color.rgb(70,80,90));
//                linear.addView(btn, params);
//                Button btn1 = (Button)findViewById(id_);
//                btn1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(v.getContext(), "Fuck You", Toast.LENGTH_SHORT).show();
//                    }
//                });

            AppCompatButton apc = new AppCompatButton(this);
            apc.setId(i);
            apc.setBackgroundResource(R.drawable.rounded_home_button_background);
            final int id_ = apc.getId();
            apc.setText("Button " + id_);
            apc.setTextColor(Color.BLACK);
            linear.addView(apc, params);
            AppCompatButton btn = findViewById(id_);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Button " + id_, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}


