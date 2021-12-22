package com.r2s.notemanagementsystem.constant;

public class Constants {
    public static final String KEY_USER_DATA = "user";
    public static final String emailPattern = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
    public static final String BASE_URL = "https://api.dmq.biz/";
    public static final int LOGIN_SUCCESS = 1;
    public static final int LOGIN_ERR = -1;
    public static final int LOGIN_USER_NOT_FOUND_ERR = 1;
    public static final int LOGIN_PWD_INCORRECT_ERR = 2;

    public static final int REGISTER_SUCCESS = 1;
    public static final int REGISTER_ERR = -1;
    public static final int REGISTER_EMAIL_EXISTS_ERR = 2;
}
