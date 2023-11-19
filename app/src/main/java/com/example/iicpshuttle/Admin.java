package com.example.iicpshuttle;

public class Admin {
    private String adminName;
    private String role;
    private String email;
    private String phone;

    public Admin(String name, String role, String email, String phone) {
        role = "Admin";
        name = "Admin";

        this.adminName = name;
        this.role = role;
        this.email = email;
        this.phone = phone;
    }

    public String getAdminName() {return adminName;}

    public String getEmail() {
        return email;
    }

    public String getPhone() {return phone;}

    public String getUserRole() {return role;}

}
