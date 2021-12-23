package com.r2s.notemanagementsystem.model;

public class Note{
    private int nid;

    private String name;
    private String category;
    private String priority;
    private String status;
    private String planDate;
    private String createdDate;
    private int userId;

    public Note(int nid, String name, String category, String priority, String status, String planDate, String createdDate, int userId) {
        this.nid = nid;
        this.name = name;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.planDate = planDate;
        this.createdDate = createdDate;
        this.userId = userId;
    }

    public Note(String name, String category, String priority, String status, String planDate, String createdDate) {
        this.name = name;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.planDate = planDate;
        this.createdDate = createdDate;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}