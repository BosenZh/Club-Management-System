package com.example.common.enums;

public enum ResultCodeEnum {
    SUCCESS("200", "Success"),

    PARAM_ERROR("400", "Bad Request"),
    TOKEN_INVALID_ERROR("401", "Unauthorized Token"),
    TOKEN_CHECK_ERROR("401", "Unauthorized Token, Please Login"),
    PARAM_LOST_ERROR("4001", "Not Found"),

    SYSTEM_ERROR("500", "System Error"),
    USER_EXIST_ERROR("5001", "Student Already Exist"),
    USER_NOT_LOGIN("5002", "Login Error"),
    USER_ACCOUNT_ERROR("5003", "Wrong Username/Password"),
    USER_NOT_EXIST_ERROR("5004", "User Does Not Exist"),
    PARAM_PASSWORD_ERROR("5005", "Incorrect Password"),
    REP_ALREADY_EXIST("6001", "this student is the club rep of another club"),
    APPLICATION_ALREADY_EXIST("7001","application already exist"),
    ;

    public String code;
    public String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
