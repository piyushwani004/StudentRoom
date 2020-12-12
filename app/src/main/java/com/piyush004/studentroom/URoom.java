package com.piyush004.studentroom;

public class URoom {

    public static String UserRoom = " ";

    public static String RoomSubject = " ";

    public static String UserEmail = " ";

    public static String SubjectTopic = " ";

    public static String UserName = " ";

    public static String RoomAdmin = " ";

    public String emailSplit(String str) {
        String resultStr = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) > 64 && str.charAt(i) <= 122) {
                resultStr = resultStr + str.charAt(i);
            }
        }
        return resultStr;
    }

}
