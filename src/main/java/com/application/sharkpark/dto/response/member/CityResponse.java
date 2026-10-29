package com.application.sharkpark.dto.response.member;

public class CityResponse {

    private final String name;
    private final int postCode;


    public CityResponse(String name, int postCode) {
        this.name = name;
        this.postCode = postCode;
    }
}
