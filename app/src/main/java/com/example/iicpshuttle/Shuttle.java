package com.example.iicpshuttle;

public class Shuttle {
    private String driver;
    private String departure;
    private String seat;
    private String carPLate;
    private Boolean studentList;
    private String date;
    private String time;

    public Shuttle(){

    }


    public Shuttle(String driver, String departure, String seat, String carPLate,Boolean studentList, String date, String time ) {
        this.date = date;
        this.time = time;
        this.seat = seat;
        this.carPLate= carPLate;
        this.departure =departure;
        this.driver =driver;
        this.studentList =studentList;

    }

    public String getShuttleDate() {return date;}
    public String getShuttleTime() {return time;}
    public String getShuttleSeat() {return seat;}
    public String getShuttleCarPlate() {return carPLate;}
    public Boolean getShuttleStudentList() {return studentList;}
    public String getShuttleDeparture() {return departure;}
    public String getShuttleDriver() {return driver;}

}
