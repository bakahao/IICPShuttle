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
                Toast.makeText(LoginActivity.this, "Please enter Student ID and Password", Toast.LENGTH_SHORT).show();
            }else {
                // 在这里进行与数据库的比对逻辑
                boolean isValid = databaseHelper.checkCredentials(enteredID, enteredPassword);

                if (isValid) {
                    // 用户验证成功，跳转到activity_home.xml页面
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Student ID or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}