package com.example.iicpshuttle;

public class ScheduleAdmin {

    private String date;
    private String departure;
    private String status;
    private String time;

    public ScheduleAdmin() {

    }

    public ScheduleAdmin(String date, String departure, String status, String time) {
        this.date = date;
        this.departure = departure;
        this.status = status;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getDeparture() {
        return departure;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
