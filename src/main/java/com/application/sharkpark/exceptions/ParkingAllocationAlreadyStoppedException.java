package com.application.sharkpark.exceptions;

public class ParkingAllocationAlreadyStoppedException extends RuntimeException {
    public ParkingAllocationAlreadyStoppedException(int parkingAllocationId) {
        super("ParkingAllocation with ID = " + parkingAllocationId + " already stopped");
    }
}
