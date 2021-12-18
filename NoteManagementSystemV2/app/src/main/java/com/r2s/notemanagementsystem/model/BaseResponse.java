package com.r2s.notemanagementsystem.model;

public class BaseResponse<T> {
    private int status;
    private Info info;
    private T data;
    private int error;

    public BaseResponse(int status, Info info, T data, int error) {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }
}
