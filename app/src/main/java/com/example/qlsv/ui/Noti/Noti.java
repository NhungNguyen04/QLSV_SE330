package com.example.qlsv.ui.Noti;

public class Noti {
    private String title;
    private String date;
    private String time;
    private String room;

    public Noti(String title, String date, String time, String room) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.room = room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
