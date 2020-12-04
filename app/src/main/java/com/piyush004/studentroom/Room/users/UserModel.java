package com.piyush004.studentroom.Room.users;

public class UserModel {

    private String Email;


    public UserModel() {
    }

    public UserModel(String email) {
        Email = email;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "Email='" + Email + '\'' +
                '}';
    }
}
