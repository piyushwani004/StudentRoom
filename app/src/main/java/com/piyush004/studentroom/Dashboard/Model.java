package com.piyush004.studentroom.Dashboard;

public class Model {

    private String Name;
    private String Password;

    public Model(String name, String pass) {
        Name = name;
        Password = pass;
    }

    public Model() {
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "Model{" +
                "Name='" + Name + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }
}
