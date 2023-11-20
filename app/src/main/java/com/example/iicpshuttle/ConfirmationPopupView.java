package com.example.iicpshuttle;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class ConfirmationPopupView extends LinearLayout {


    public ConfirmationPopupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.popup_confirmation, this, true);
        // Customize the view and handle interactions
    }

    public void setConfirmationMessage(String date, String time, String departure, String studentID) {
        TextView tvDate = findViewById(R.id.dateTextView);
        TextView tvTime = findViewById(R.id.timeTextView);
        TextView tvDeparture = findViewById(R.id.departureTextView);
        TextView tvStuID = findViewById(R.id.studentIDTextView);

        tvDate.setText(tvDate.getText() + date);
        tvTime.setText(tvTime.getText() + time);
        tvDeparture.setText(tvDeparture.getText() + departure);
        tvStuID.setText(tvStuID.getText() + studentID);
    }

    public void setOnConfirmationListener(View.OnClickListener onYesClickListener, View.OnClickListener onNoClickListener) {
        AppCompatButton btnYes = findViewById(R.id.btnConfirm);
        AppCompatButton btnNo = findViewById(R.id.btnBack);

        btnYes.setOnClickListener(onYesClickListener);
        btnNo.setOnClickListener(onNoClickListener);
    }
}


