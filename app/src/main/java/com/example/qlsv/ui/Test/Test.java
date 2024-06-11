package com.example.qlsv.ui.Test;

public class Test {
    private String date;
    private String time;
    private String course;
    private String roomName;

    public Test(String date, String time, String course, String roomName) {
        this.date = date;
        this.time = time;
        this.course = course;
        this.roomName = roomName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getCourse() {
        return course;
    }

    public String getRoomName() {
        return roomName;
    }
}
