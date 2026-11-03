package com.application.sharkpark.mapper;

import com.application.sharkpark.domain.ParkingAllocation;
import com.application.sharkpark.dto.response.ParkingAllocationResponse;

public final class ParkingAllocationMapper {
    private ParkingAllocationMapper() {
    }

    public static ParkingAllocationResponse toResponse(ParkingAllocation allocation) {
        return new ParkingAllocationResponse(
                allocation.getParkingAllocationId(),
                allocation.getParkingLot().getId(),
                allocation.getMember().getUserId(),
                allocation.getLicensePlate(),
                allocation.getStartTime());
    }
}
