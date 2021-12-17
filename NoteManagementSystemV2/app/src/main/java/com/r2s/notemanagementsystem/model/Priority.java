package com.r2s.notemanagementsystem.model;

public class Priority {
    private int id;
    private String name;
    private String createdDate;
    private int userId;

    public Priority(int id, String name, String createdDate, int userId) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
