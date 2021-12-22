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
    }

    private Category category;

    /**
     * Constructor
     * @param status
     * @param category
     * @param data
     * @param error
     */
    public BaseResponse(int status, Category category, List<List<String>> data, int error) {
        this.status = status;
        this.category = category;
        this.data = data;
        this.error = error;
    }

    /**
     * get status
     * @return status
     */
    public int getStatus() {
        return status;
    }

    /**
     * set status
     * @param status
     */
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
}
