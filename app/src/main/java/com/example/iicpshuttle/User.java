package com.example.iicpshuttle;

public class User {
    private int id;
    private String name;
    private String studentId;
    private String password;
    private String phone;

    public User(int id, String name, String studentId, String password, String phone) {
        this.id = id;
        this.name = name;
        this.studentId = studentId;
        this.password = password;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {return name;}

    public String getStudentId() {
        return studentId;
    }

    public String getPassword() {return password;}

    public String getPhone() {return phone;}

}
