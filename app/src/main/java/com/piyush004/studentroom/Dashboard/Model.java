package com.piyush004.studentroom.Dashboard;

public class Model {

    private String Name;


    public Model(String name) {
        Name = name;
    }

    public Model() {
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "model{" +
                "Name='" + Name + '\'' +
                '}';
    }
}
