package com.application.sharkpark.dto.request;

import jakarta.validation.constraints.NotNull;

public record StopParkingAllocationRequest(
        @NotNull(message = "parkingAllocationId cannot be null")
        Integer parkingAllocationId,
        @NotNull(message = "memberId cannot be null")
        Integer memberId) {
}
