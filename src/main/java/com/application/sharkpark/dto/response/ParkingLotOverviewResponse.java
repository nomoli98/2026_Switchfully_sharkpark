package com.application.sharkpark.dto.response;

public record ParkingLotOverviewResponse(
        int parkingLotId,
        String name,
        int capacity,
        String contactEmail,
        String contactTelephone
) {
}
