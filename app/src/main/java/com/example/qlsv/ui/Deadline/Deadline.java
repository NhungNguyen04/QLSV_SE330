package com.example.qlsv.ui.Deadline;

public class Deadline {
    int id;
    private String className;
    private String title;
    private String date;
    private String status;
    private String complete;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Deadline(String className, String title, String date, String status, String complete, int id) {
        this.className = className;
        this.title = title;
        this.date = date;
        this.status = status;
        this.complete = complete;
        this.id = id;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getClassName() {
        return className;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getComplete() {
        return complete;
    }
}
