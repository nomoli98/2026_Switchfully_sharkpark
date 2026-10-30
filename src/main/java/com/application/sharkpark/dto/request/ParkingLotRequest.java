package com.application.sharkpark.dto.request;

import com.application.sharkpark.enums.ParkingLotCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ParkingLotRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Category is required")
    private ParkingLotCategory category;

    @Positive (message = "maxcap needs to be a positive number")
    private int maxCapacity;

    @NotBlank(message = "address cannot be blank")
    private String addressStreet;

    @Positive(message = "number needs to be a positive number")
    private int addressNumber;

    @Positive
    private double pricePerHour;

    @Positive(message = "id needs to be positive")
    private int userId;

    @Positive(message = "id needs to be positive")
    private int cityId;


    public ParkingLotRequest(String name, ParkingLotCategory category,
                             int maxCapacity, String addressStreet, Integer addressNumber,
                             double pricePerHour,  int userId, int cityId) {
        this.name = name;
        this.category = category;
        this.maxCapacity = maxCapacity;
        this.addressStreet = addressStreet;
        this.addressNumber = addressNumber;
        this.pricePerHour = pricePerHour;
        this.userId = userId;
        this.cityId = cityId;

    }

    public String getName() {
        return name;
    }

    public ParkingLotCategory getCategory() {
        return category;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public int getAddressNumber() {
        return addressNumber;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public int getUserId() {
        return userId;
    }

    public int getCityId() {
        return cityId;
    }
}
