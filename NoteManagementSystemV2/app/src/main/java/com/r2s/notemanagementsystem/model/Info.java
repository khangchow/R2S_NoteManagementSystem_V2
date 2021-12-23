package com.r2s.notemanagementsystem.model;

public class Info {
    private String FirstName;
    private String LastName;

    /**
     * Constructor
     * @param firstName
     * @param lastName
     */
    public Info(String firstName, String lastName) {
        FirstName = firstName;
        LastName = lastName;
    }

    /**
     * get first name
     * @return first name
     */
    public String getFirstName() {
        return FirstName;
    }

    /**
     * set first name
     * @param firstName
     */
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    /**
     * get last name
     * @return last name user
     */
    public String getLastName() {
        return LastName;
    }

    /**
     * set last name
     * @param lastName
     */
    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
