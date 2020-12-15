package com.piyush004.studentroom.Room.Chat;

public class ChatModel {

    public String Message;
    public String Date;
    public String UserName;
    public String Time;


    public ChatModel(String message, String date, String USerName, String time) {
        Message = message;
        Date = date;
        UserName = USerName;
        Time = time;
    }

    public ChatModel() {
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    @Override
    public String toString() {
        return "ChatModel{" +
                "Message='" + Message + '\'' +
                ", Date='" + Date + '\'' +
                ", USerName='" + UserName + '\'' +
                ", Time='" + Time + '\'' +
                '}';
    }
}
