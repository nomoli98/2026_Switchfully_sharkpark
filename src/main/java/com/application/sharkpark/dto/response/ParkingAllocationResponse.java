package com.application.sharkpark.dto.response;

import com.application.sharkpark.enums.ParkingAllocationStatus;

import java.time.LocalDateTime;

public record ParkingAllocationResponse(int parkingAllocationId,
                                        int parkingLotId,
                                        int memberId,
                                        String licensePlate,
                                        LocalDateTime startTime,
                                        ParkingAllocationStatus status,
                                        LocalDateTime stopTime) {
}
