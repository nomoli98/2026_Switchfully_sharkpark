package com.application.sharkpark.dto.request;

import java.time.LocalDateTime;

public record ParkingAllocationResponse(int parkingAllocationId, String licensePlate, LocalDateTime startTime) {
}
