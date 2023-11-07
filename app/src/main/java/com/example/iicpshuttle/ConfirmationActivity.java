package com.example.iicpshuttle;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // 获取用户点击的按钮的ID
        int buttonId = getIntent().getIntExtra("buttonId", -1);

        // 显示对话框
        showDialog(buttonId);
    }

    private void showConfirmationDialog(final int buttonId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("You clicked button with ID: " + buttonId);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击确认按钮的处理逻辑
                // 可以在这里执行其他操作，如提交预订请求
                Toast.makeText(ConfirmationActivity.this, "Button with ID " + buttonId + " confirmed.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击取消按钮的处理逻辑
                Toast.makeText(ConfirmationActivity.this, "Button with ID " + buttonId + " canceled.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        builder.show();
    }



}


