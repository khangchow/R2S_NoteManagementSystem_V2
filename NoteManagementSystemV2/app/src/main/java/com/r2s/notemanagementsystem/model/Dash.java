package com.r2s.notemanagementsystem.model;

public class Dash {
    private String status;
    private String note;

    public Dash(String status, String note) {
        this.status = status;
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
