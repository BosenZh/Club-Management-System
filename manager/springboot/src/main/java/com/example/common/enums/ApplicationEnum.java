package com.example.common.enums;

public enum ApplicationEnum {
    APPLIED("Application Received"),
    COMPLETED("Application Reviewed"),
    IN_PROGRESS("Application In Progress"),
    APPROVED("Approved"),
    DECLINED("Declined")
    ;

    public String status;

    ApplicationEnum(String status){
        this.status = status;
    }
}
