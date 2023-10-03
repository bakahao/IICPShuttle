package com.example.iicpshuttle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_contact_us);
        ImageView backIconImageView = findViewById(R.id.backIconImageView);

        SharedPreferences sharedPreferences;

        EditText emailEditText;
        EditText phoneEditText;

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        emailEditText = findViewById(R.id.emailEditText_contact);
        phoneEditText = findViewById(R.id.phoneEditText_contact);

        // save previous text
        emailEditText.setText(sharedPreferences.getString("email", ""));
        phoneEditText.setText(sharedPreferences.getString("phone", ""));

        backIconImageView.setOnClickListener(v -> {
            Intent backIntent = new Intent(AdminContactUsActivity.this, AdminHomePageActivity.class);
            saveTextToSharedPreferences(sharedPreferences, emailEditText, phoneEditText);
            startActivity(backIntent);
        });
    }

    private void saveTextToSharedPreferences(SharedPreferences sharedPreferences, EditText emailEditText,
                                             EditText phoneEditText) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", emailEditText.getText().toString());
        editor.putString("phone", phoneEditText.getText().toString());
        editor.apply();
    }

}
