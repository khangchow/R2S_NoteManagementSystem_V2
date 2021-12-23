package com.r2s.notemanagementsystem.model;

public class Priority {
    private int id;
    private String name;
    private String createdDate;
    private String userEmail;

    /**
     * Constructor with no paramters
     */
    public Priority() {

    }

    /**
     * Constructor with 3 parameters
     * @param name String
     * @param createdDate String
     * @param userEmail String
     */
    public Priority(String name, String createdDate, String userEmail) {
        this.name = name;
        this.createdDate = createdDate;
        this.userEmail = userEmail;
    }

    /**
     * Constructor with 4 parameters
     * @param id int
     * @param name String
     * @param createdDate String
     * @param userEmail String
     */
    public Priority(int id, String name, String createdDate, String userEmail) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.userEmail = userEmail;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
