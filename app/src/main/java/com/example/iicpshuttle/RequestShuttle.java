package com.example.iicpshuttle;

public class RequestShuttle {
    private String departure;
    private String date;
    private String time;

    public RequestShuttle(String departure, String date, String time){
        this.departure = departure;
        this.date = date;
        this.time = time;
    }

    public String getDeparture(){return departure;}
    public String getDate(){return date;}
    public String getTime(){return time;}
}
