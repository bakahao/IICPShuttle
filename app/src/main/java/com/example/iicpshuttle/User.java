package com.example.iicpshuttle;

public class User {
    private String userName;
    private String userRole;
    private String email;
    private String phone;
    private String studentID;

    public User(String name, String role, String email, String phone, String studentID) {
        this.userName = name;
        this.userRole = role;
        this.email = email;
        this.phone = phone;
        this.studentID = studentID;
    }

    public User() {

    }

    public User(User u){
        this.userName = u.userName;
        this.userRole = u.userRole;
        this.email = u.email;
        this.phone = u.phone;
        this.studentID = u.studentID;
    }

    public String getUserName() {return userName;}
    public String getEmail() {
        return email;
    }
    public String getPhone() {return phone;}
    public String getUserRole() {return userRole;}
    public String getStudentID(){return studentID;}

}
