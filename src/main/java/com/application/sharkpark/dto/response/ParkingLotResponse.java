package com.application.sharkpark.dto.response;

import com.application.sharkpark.enums.ParkingLotCategory;

public class ParkingLotResponse {

    private int parkingLotId;
    private String parkingLotName;
    private ParkingLotCategory parkingLotCategory;
    private int maxCapacity;
    private String addressStreet;
    private int addressNumber;
    private double pricePerHour;

    private UserResponse contactPerson;
    private CityResponse city;

    public ParkingLotResponse(int parkingLotId, String parkingLotName, ParkingLotCategory parkingLotCategory,
                              int maxCapacity, String addressStreet,
                              int addressNumber, double pricePerHour,
                              UserResponse userResponse, CityResponse cityResponse) {
        this.parkingLotId = parkingLotId;
        this.parkingLotName = parkingLotName;
        this.parkingLotCategory = parkingLotCategory;
        this.maxCapacity = maxCapacity;
        this.addressStreet = addressStreet;
        this.addressNumber = addressNumber;
        this.pricePerHour = pricePerHour;
        this.contactPerson = userResponse;
        this.city = cityResponse;


    }
}
