package com.application.sharkpark.parkingLot.dto.response;

import com.application.sharkpark.parkingLot.enums.ParkingLotCategory;

public class ParkingLotResponse {

    private int parkingLotId;
    private String parkingLotName;
    private ParkingLotCategory parkingLotCategory;
    private int maxCapacity;
    private String addressStreet;
    private int addressNumber;
    private double pricePerHour;

    public ParkingLotResponse(int parkingLotId, String parkingLotName, ParkingLotCategory parkingLotCategory, int maxCapacity, String addressStreet, int addressNumber, double pricePerHour) {
        this.parkingLotId = parkingLotId;
        this.parkingLotName = parkingLotName;
        this.parkingLotCategory = parkingLotCategory;
        this.maxCapacity = maxCapacity;
        this.addressStreet = addressStreet;
        this.addressNumber = addressNumber;
        this.pricePerHour = pricePerHour;
    }
}
