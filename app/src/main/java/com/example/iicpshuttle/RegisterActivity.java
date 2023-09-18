package com.example.iicpshuttle;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, studentIdEditText, passwordEditText, phoneEditText;
    private Button registerButton;
    private TextView nameErrorTextView, studentIdErrorTextView, passwordErrorTextView, phoneErrorTextView;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameEditText = findViewById(R.id.nameEditText);
        studentIdEditText = findViewById(R.id.studentIdEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        registerButton = findViewById(R.id.button1);

        databaseHelper = new DatabaseHelper(this);

        nameErrorTextView = findViewById(R.id.nameErrorTextView);
        studentIdErrorTextView = findViewById(R.id.studentIdErrorTextView);
        passwordErrorTextView = findViewById(R.id.passwordErrorTextView);
        phoneErrorTextView = findViewById(R.id.phoneErrorTextView);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入
                String name = nameEditText.getText().toString().trim();
                String studentId = studentIdEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String phoneNumber = phoneEditText.getText().toString().trim();
                if (name.isEmpty() || studentId.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
                    if (name.isEmpty()) {
                        nameErrorTextView.setVisibility(View.VISIBLE);
                    } else {
                        nameErrorTextView.setVisibility(View.GONE);
                    }

                    if (studentId.isEmpty()) {
                        studentIdErrorTextView.setVisibility(View.VISIBLE);
                    } else {
                        studentIdErrorTextView.setVisibility(View.GONE);
                    }

                    if (password.isEmpty()) {
                        passwordErrorTextView.setVisibility(View.VISIBLE);
                    } else {
                        passwordErrorTextView.setVisibility(View.GONE);
                    }

                    if (phoneNumber.isEmpty()) {
                        phoneErrorTextView.setVisibility(View.VISIBLE);

                    } else {
                        phoneErrorTextView.setVisibility(View.GONE);
                    }
                    return;
                }
                SQLiteDatabase db = databaseHelper.getReadableDatabase();
                Cursor cursor = db.query(
                        DatabaseHelper.TABLE_NAME,
                        new String[]{DatabaseHelper.COLUMN_PHONE_NUMBER, DatabaseHelper.COLUMN_STUDENT_ID},
                        DatabaseHelper.COLUMN_PHONE_NUMBER + "=? OR " + DatabaseHelper.COLUMN_STUDENT_ID + "=?",
                        new String[]{phoneNumber, studentId},
                        null,
                        null,
                        null
                );
                if (cursor.getCount() > 0) {
                    // 电话号码或学生 ID 已存在，显示相应的错误消息并停止注册
                    Toast.makeText(RegisterActivity.this, "Phone Number or Student ID already exists", Toast.LENGTH_SHORT).show();
                }else {
                    // 将用户输入插入到数据库
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_NAME, name);
                    values.put(DatabaseHelper.COLUMN_STUDENT_ID, studentId);
                    values.put(DatabaseHelper.COLUMN_PASSWORD, password);
                    values.put(DatabaseHelper.COLUMN_PHONE_NUMBER, phoneNumber);

                    long newRowId = db.insert(DatabaseHelper.TABLE_NAME, null, values);

                    if (newRowId != -1) {
                        Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                    }
                }
                cursor.close();
                db.close();
            }
        });
    }
    public void onIconClick(View view) {
        // 创建 Intent 以启动 LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
