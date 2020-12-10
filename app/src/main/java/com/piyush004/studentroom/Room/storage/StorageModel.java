package com.piyush004.studentroom.Room.storage;

public class StorageModel {

    private String Subject;
    private String URIName;

    public StorageModel() {
    }

    public StorageModel(String subject, String URIName) {
        Subject = subject;
        this.URIName = URIName;
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

    public String getURIName() {
        return URIName;
    }

    public void setURIName(String URIName) {
        this.URIName = URIName;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "Email='" + Subject + '\'' +
                '}';
    }
}
