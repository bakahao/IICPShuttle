package com.example.iicpshuttle;

public class StudentReminderShuttle extends UserReminderShuttle {
    private boolean isAttendance;
    private long shuttleTimeStamp;

    public StudentReminderShuttle(){

    }

    public StudentReminderShuttle(String date, String time, String departure, boolean isAttendance, long shuttleTimeStamp){
        super(date, time, departure);
        this.isAttendance = isAttendance;
        this.shuttleTimeStamp = shuttleTimeStamp;
    }

    public boolean isAttendance() {
        return this.isAttendance;
    }
    public long getShuttleTimeStamp(){return this.shuttleTimeStamp;}
}
