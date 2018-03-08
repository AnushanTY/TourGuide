package com.example.anushan.tourguide;

/**
 * Created by Anushan on 3/8/2018.
 */

public class User {
    private  String email,username;

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
