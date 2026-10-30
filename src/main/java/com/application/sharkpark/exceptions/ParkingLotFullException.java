package com.application.sharkpark.exceptions;

public class ParkingLotFullException extends RuntimeException {
    public ParkingLotFullException() {
        super("The parking lot is full");
    }
}
