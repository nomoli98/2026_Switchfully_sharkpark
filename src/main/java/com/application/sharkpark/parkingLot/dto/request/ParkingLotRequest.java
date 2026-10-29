package com.application.sharkpark.parkingLot.dto.request;

import com.application.sharkpark.parkingLot.enums.ParkingLotCategory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ParkingLotRequest {

    @NotBlank
    private String name;

    @Valid
    private ParkingLotCategory category;

    @NotBlank
    @Positive
    private int maxCapacity;

    @NotBlank
    private String addressStreet;

    @NotBlank
    @Positive
    private int addressNumber;

    @NotBlank
    @Positive
    private double pricePerHour;


    public ParkingLotRequest(String name, ParkingLotCategory category, int maxCapacity, String addressStreet, Integer addressNumber, double pricePerHour) {
        this.name = name;
        this.category = category;
        this.maxCapacity = maxCapacity;
        this.addressStreet = addressStreet;
        this.addressNumber = addressNumber;
        this.pricePerHour = pricePerHour;
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
}
