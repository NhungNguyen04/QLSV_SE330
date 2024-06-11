package com.example.qlsv.ui.TKB;

public class TKBClass {
    private String lecturer;
    private String courseName;
    private String room;
    private int begin;
    private int end;

    public TKBClass(String lecturer, String courseName, String room, int begin, int end) {
        this.lecturer = lecturer;
        this.courseName = courseName;
        this.room = room;
        this.begin = begin;
        this.end = end;
    }

    public String getLecturer() {
        return lecturer;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getRoom() {
        return room;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
