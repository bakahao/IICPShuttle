package com.example.iicpshuttle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverRegisterActivity extends AppCompatActivity {

    private EditText driverNameEditText, driverEmailEditText, driverPasswordEditText, driverPhoneEditText;
    private Button driverNextButton;
    private TextView DriverNameErrorTextView, DriverEmailErrorTextView, DriverPasswordErrorTextView, DriverPhoneErrorTextView;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        mAuth = FirebaseAuth.getInstance();

        driverNextButton = findViewById(R.id.button1);
        driverNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register(){
        driverNameEditText = findViewById(R.id.driverNameEditText);
        driverEmailEditText = findViewById(R.id.driverEmailEditText);
        driverPasswordEditText = findViewById(R.id.driverPasswordEditText);
        driverPhoneEditText = findViewById(R.id.driverPhoneEditText);


        DriverNameErrorTextView = findViewById(R.id.DriverNameErrorTextView);
        DriverEmailErrorTextView = findViewById(R.id.DriverEmailErrorTextView);
        DriverPasswordErrorTextView = findViewById(R.id.DriverPasswordErrorTextView);
        DriverPhoneErrorTextView = findViewById(R.id.DriverPhoneErrorTextView);

        // 获取用户输入
        String name = driverNameEditText.getText().toString().trim();
        String email = driverEmailEditText.getText().toString().trim();
        String password = driverPasswordEditText.getText().toString().trim();
        String phoneNumber = driverPhoneEditText.getText().toString().trim();
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty()) {
            if (name.isEmpty()) {
                DriverNameErrorTextView.setVisibility(View.VISIBLE);
            } else {
                DriverEmailErrorTextView.setVisibility(View.GONE);
            }

            if (email.isEmpty()) {
                DriverEmailErrorTextView.setVisibility(View.VISIBLE);
            } else {
                DriverEmailErrorTextView.setVisibility(View.GONE);
            }
            if (password.isEmpty()) {
                DriverPasswordErrorTextView.setVisibility(View.VISIBLE);
            } else {
                DriverPasswordErrorTextView.setVisibility(View.GONE);
            }

            if (phoneNumber.isEmpty()) {
                DriverPhoneErrorTextView.setVisibility(View.VISIBLE);

            } else {
                DriverPhoneErrorTextView.setVisibility(View.GONE);
            }
            return;
        }

        String role = "Driver";
        User user = new User(name, role, email, phoneNumber);

        startActivity(new Intent(DriverRegisterActivity.this, DriverRegisterDetails.class));

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser mUser = mAuth.getCurrentUser();
                    DatabaseReference userRef = FirebaseDatabase.getInstance("https://iicpshuttle-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                    userRef.child("Users").child(mUser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(DriverRegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DriverRegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    startActivity(new Intent(DriverRegisterActivity.this, DriverRegisterDetails.class));
                } else {
                    Toast.makeText(DriverRegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void onIconClick(View view) {
        // 创建 Intent 以启动 LoginActivity
        Intent intent = new Intent(this, DriverRegisterDetails.class);
        startActivity(intent);
    }
}