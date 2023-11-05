package com.example.iicpshuttle;

public class Schedule {
    private String date;
    private String time;
    private String seat;




    public Schedule(String date, String time, String seat ) {
        this.date = date;
        this.time = time;
        this.seat = seat;


    }

    public String getScheduleDate() {return date;}
    public String getScheduleTime() {return time;}
    public String getScheduleSeat() {return seat;}




}
