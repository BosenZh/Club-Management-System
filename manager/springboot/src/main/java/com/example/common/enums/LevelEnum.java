package com.example.common.enums;

public enum LevelEnum {
    ClubRepresentative("ClubRepresentative"),
    ClubMember("ClubMember")
    ;
    public String title;

    LevelEnum(String title){
        this.title = title;
    }
}
