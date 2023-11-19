package com.example.iicpshuttle;

public class UserReminderShuttle {
    private String shuttleDate;
    private String shuttleTime;
    private String shuttleDeparture;

    public UserReminderShuttle(){

    }

    public UserReminderShuttle(String shuttleDate, String shuttleTime, String shuttleDeparture){
        this.shuttleDate = shuttleDate;
        this.shuttleTime = shuttleTime;
        this.shuttleDeparture = shuttleDeparture;
    }

    public String getShuttleDate(){return this.shuttleDate;}
    public String getShuttleTime(){return this.shuttleTime;}
    public String getShuttleDeparture(){return this.shuttleDeparture;}
}

