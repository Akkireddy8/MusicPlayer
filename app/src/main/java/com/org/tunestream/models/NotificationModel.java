package com.org.tunestream.models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class NotificationModel {

    private String title;
    private String message;
    private Timestamp timestamp;
    private String userEmail;

    public NotificationModel(String title, String message, Timestamp timestamp, String userEmail) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.userEmail = userEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
