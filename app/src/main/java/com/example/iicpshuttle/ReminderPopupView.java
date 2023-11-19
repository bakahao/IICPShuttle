package com.example.iicpshuttle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

public class ReminderPopupView extends LinearLayout {
    public ReminderPopupView(Context context, AttributeSet attrs){
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.popup_reminder, this, true);
    }

    public void setReminderMessage(String date, String time, String departure, boolean isAttendance ){
        TextView tvDate = findViewById(R.id.dateTextView);
        TextView tvTime = findViewById(R.id.timeTextView);
        TextView tvDeparture = findViewById(R.id.departureTextView);
        TextView tvAttendance = findViewById(R.id.attendanceTextView);

        tvDate.setText(tvDate.getText() + date);
        tvTime.setText(tvTime.getText() + time);
        tvDeparture.setText(tvDeparture.getText() + departure);
        tvAttendance.setText(tvAttendance.getText() + String.valueOf(isAttendance));
    }

    public void setOnReminderListener(View.OnClickListener onCancelBookingClickListener, View.OnClickListener onRemoveClickListener, View.OnClickListener onCancelClickListener) {
        AppCompatButton btnCancelBooking = findViewById(R.id.btnCancelBooking);
        AppCompatButton btnRemove = findViewById(R.id.btnRemove);
        AppCompatButton btnBack = findViewById(R.id.btnBack);

        btnCancelBooking.setOnClickListener(onCancelBookingClickListener);
        btnRemove.setOnClickListener(onRemoveClickListener);
        btnBack.setOnClickListener(onCancelClickListener);
    }
}
