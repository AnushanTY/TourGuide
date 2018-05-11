package com.example.anushan.tourguide;

/**
 * Created by Anushan on 3/8/2018.
 */

public class User {
    private  String email,username;
    private long role;

    public User() {
    }

    public User(String email, String username, long role) {
        this.email = email;
        this.username = username;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public long getRole() {
        return role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(long role) {
        this.role = role;
    }
}
