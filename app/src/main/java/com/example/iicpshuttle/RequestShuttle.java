package com.example.iicpshuttle;

public class RequestShuttle {
    private String departure;
    private String date;
    private String time;
    private String status;

    public RequestShuttle() {
        // Default constructor is required by Firebase for deserialization
    }

    public RequestShuttle(String departure, String date, String time, String status){
        this.departure = departure;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getDeparture(){return departure;}
    public String getDate(){return date;}
    public String getTime(){return time;}
    public String getStatus(){return status;}
}
