package com.application.sharkpark.dto.response;

import java.time.LocalDateTime;

public record ParkingAllocationResponse(int parkingAllocationId, int parkingLotId, int memberId, String licensePlate, LocalDateTime startTime) {
}
