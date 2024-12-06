package com.org.tunestream.models;

public class UserModel {

    private String dob;
    private String email;
    private String name;
    private String password;
    private String gender;

    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(UserDataModel.class)
    }

    public UserModel(String dob, String email, String name, String password, String gender) {
        this.dob = dob;
        this.email = email;
        this.name = name;
        this.password = password;
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
