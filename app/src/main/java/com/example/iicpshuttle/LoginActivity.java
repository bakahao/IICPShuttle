package com.example.iicpshuttle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

    private EditText idEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idEditText = findViewById(R.id.idEditText);
        passwordEditText = findViewById(R.id.idEditText2);
        Button loginButton = findViewById(R.id.button2);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Button registerButton = findViewById(R.id.button1);

        registerButton.setOnClickListener(v -> {
            // when click on Register button ，go to RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            String enteredID = idEditText.getText().toString().trim();
            String enteredPassword = passwordEditText.getText().toString().trim();

            if (enteredID.isEmpty() || enteredPassword.isEmpty()) {
                //Display error message
                Toast.makeText(LoginActivity.this, "Please enter Student ID and Password", Toast.LENGTH_SHORT).show();
            } else {
                if (enteredID.equals("admin") && enteredPassword.equals("admin")) {
                    Intent intent = new Intent(LoginActivity.this, AdminHomePageActivity.class);
                    startActivity(intent);
                }else if(enteredID.equals("driver") && enteredPassword.equals("driver")){
                    Intent intent = new Intent(LoginActivity.this, DriverHomePageActivity.class);
                    startActivity(intent);
                }else {
                    // Check the input exist in database or not
                    boolean isValid = databaseHelper.checkCredentials(enteredID, enteredPassword);

                    if (isValid) {
                        // If exist，turn to activity_home.xml page
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        // If no, display invalid message
                        Toast.makeText(LoginActivity.this, "Invalid Student ID or Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}