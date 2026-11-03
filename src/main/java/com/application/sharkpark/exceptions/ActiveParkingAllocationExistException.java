package com.application.sharkpark.exceptions;

public class ActiveParkingAllocationExistException extends RuntimeException {
    public ActiveParkingAllocationExistException(String licensePlate, int parkingPlotId) {
        super("An ACTIVE ParkingAllocation already exist with licensePlate = " + licensePlate
                + " and parkingPlotId = " + parkingPlotId);
    }
}
