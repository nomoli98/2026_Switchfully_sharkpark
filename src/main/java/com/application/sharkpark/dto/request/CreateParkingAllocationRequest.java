package com.application.sharkpark.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateParkingAllocationRequest(
        @NotNull(message = "memberId cannot be null")
        Integer memberId,
        @NotNull(message = "licensePlate cannot be null")
        String licensePlate,
        @NotNull(message = "parkingLotId cannot be null")
        Integer parkingLotId) {
}
