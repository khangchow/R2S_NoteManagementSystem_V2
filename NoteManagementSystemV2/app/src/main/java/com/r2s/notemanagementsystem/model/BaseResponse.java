package com.r2s.notemanagementsystem.model;

import java.util.List;

public class BaseResponse {
    private int status;
    private Info info;
    private List<List<String>> data;
    private int error;

    public BaseResponse(int status, Info info, List<List<String>> data, int error) {
        this.status = status;
        this.info = info;
        this.data = data;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }
}
