package com.example.iicpshuttle;

public class StudentListDetails {
    private String studentID;
    private String userName;

    public StudentListDetails(String studentID, String userName) {
        this.studentID = studentID;
        this.userName = userName;

    }

    public StudentListDetails() {
    }

    public String getStudentID() {
        return studentID;
    }

    public String getUserName() {
        return userName;
    }



    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    }








