package com.application.sharkpark.dto.response.member;

public class UserResponse {
    private final String name;
    private final String phoneNumber;
    private final String mobileNumber;


    public UserResponse(String name, String phoneNumber, String mobileNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.mobileNumber = mobileNumber;
    }
}
