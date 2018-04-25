package com.example.anushan.tourguide;

/**
 * Created by Anushan on 3/10/2018.
 */

public class Review {
    private String comment,username;

    public Review() {
    }

    public Review(String comment, String username) {
        this.comment = comment;
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
