package com.application.sharkpark.exceptions;

public class InvalidParkingAllocationOwnerException extends RuntimeException {
    public InvalidParkingAllocationOwnerException(int parkingAllocationId, int memberId) {
        super("Member with ID = " + memberId + " does not own ParkingAllocation with ID = " + parkingAllocationId);
    }
}
