package com.piyush004.studentroom.Room.storage;

public class StorageModel {

    private String Subject;

    public StorageModel() {
    }

    public StorageModel(String email) {
        Subject = email;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String email) {
        Subject = email;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "Email='" + Subject + '\'' +
                '}';
    }
}
