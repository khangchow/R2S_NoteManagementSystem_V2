package com.r2s.notemanagementsystem.model;

import java.util.List;

public class BaseResponse {
    private int status;
    private Category category;
    private List<List<String>> data;
    private int error;

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

    /**
     * get Category
     * @return category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * set category
     * @param category
     */
    public void setCategory(Category category) {
        this.category = category;
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

    /**
     * get error
     * @return number error
     */
    public int getError() {
        return error;
    }

    /**
     * set error
     * @param error
     */
    public void setError(int error) {
        this.error = error;
    }
}
