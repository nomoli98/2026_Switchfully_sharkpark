package com.application.sharkpark.dto.response;

public record CityResponse (String name,
                            Integer postalCode) {
    public String getName() {
        return name;
    }

    public Integer getPostalCode() {
        return postalCode;
    }
}
