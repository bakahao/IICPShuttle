package com.example.iicpshuttle;

public class User {
    private String userName;
    private String userRole;
    private String email;
    private String phone;

    public User(String name, String role, String email, String phone) {
        this.userName = name;
        this.userRole = role;
        this.email = email;
        this.phone = phone;
    }

    public String getUserName() {return userName;}

    public String getEmail() {
        return email;
    }

    public String getPhone() {return phone;}

    public String getUserRole() {return userRole;}

}
