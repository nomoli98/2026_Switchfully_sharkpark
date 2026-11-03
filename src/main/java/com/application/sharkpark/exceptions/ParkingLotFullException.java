package com.application.sharkpark.exceptions;

public class ParkingLotFullException extends RuntimeException {
    public ParkingLotFullException(int parkingLotId) {
        super("The parking lot with ID = " + parkingLotId + " is full");
    }
}
