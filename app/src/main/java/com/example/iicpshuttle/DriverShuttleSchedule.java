package com.example.iicpshuttle;

public class DriverShuttleSchedule extends UserReminderShuttle {
        private String shuttleSeat;

        public DriverShuttleSchedule(){

        }

        public DriverShuttleSchedule(String shuttleDate, String shuttleTime, String shuttleDeparture, String seat){
            super(shuttleDate, shuttleTime,shuttleDeparture);
            this.shuttleSeat= seat;

        }

        public String getShuttleSeat(){return this.shuttleSeat;}

    }



