package com.r2s.notemanagementsystem.model;

import java.util.List;

public class BaseResponse {
    private int status;
    private Info info;
    private List<List<String>> data;
    private int error;

    /**
     * Constructor with 4 parameters
     * @param status int
     * @param info Info
     * @param data List
     * @param error int
     */
    public BaseResponse(int status, Info info, List<List<String>> data, int error) {
        this.status = status;
        this.info = info;
        this.data = data;
        this.error = error;
    }

    /**
     * Get status
     * @return status int
     */
    public int getStatus() {
        return status;
    }

    /**
     * Set status
     * @param status int
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * get info
     * @return info
     */
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

    /**
     * Get list of category's data
     * @return List of category's data
     */
    public List<List<String>> getData() {
        return data;
    }

    /**
     * set list of category's data
     * @param data
     */
    public void setData(List<List<String>> data) {
        this.data = data;
    }
}
