package com.r2s.notemanagementsystem.model;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Category {
    private int cateId;

    private String nameCate;
    private String createdDate;
    private int uId;

    /**
     * Constructor
     * @param nameCate
     */
    public Category(int cateId, String nameCate, String createdDate) {
        this.cateId = cateId;
        this.nameCate = nameCate;
        this.createdDate = createdDate;
    }

    /**
     * get user id
     * @return
     */
    public int getUId() {
        return uId;
    }

    /**
     * set user id
     * @param uId
     */
    public void setUId(int uId) {
        this.uId = uId;
    }

    /**
     * get id Category
     * @return
     */
    public int getCateId() {
        return cateId;
    }

    /**
     * set id Category
     * @param cateId
     */
    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    /**
     * get name Category
     * @return
     */
    public String getNameCate() {
        return nameCate;
    }

    /**
     * set name Category
     * @param nameCate
     */
    public void setNameCate(String nameCate) {
        this.nameCate = nameCate;
    }

    /**
     * get date created
     * @return
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * set date created
     * @param createdDate
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
