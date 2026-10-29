package com.application.sharkpark.parkingLot.mapper;

import com.application.sharkpark.parkingLot.domain.ParkingLot;
import com.application.sharkpark.parkingLot.dto.request.ParkingLotRequest;
import com.application.sharkpark.parkingLot.dto.response.ParkingLotResponse;
import org.springframework.stereotype.Component;

@Component
public class ParkingLotMapper {

    public ParkingLot mapToParkingLot(ParkingLotRequest parkingLotRequest){
        return new ParkingLot(
                parkingLotRequest.getName(),
                parkingLotRequest.getCategory(),
                parkingLotRequest.getMaxCapacity(),
                parkingLotRequest.getAddressStreet(),
                parkingLotRequest.getAddressNumber(),
                parkingLotRequest.getPricePerHour()
        );
    }

    public ParkingLotResponse mapToParkingLotResponse(ParkingLot parkingLot){
        return new ParkingLotResponse(
                parkingLot.getId(),
                parkingLot.getName(),
                parkingLot.getCategory(),
                parkingLot.getMaxCapacity(),
                parkingLot.getAddressStreet(),
                parkingLot.getAddressNumber(),
                parkingLot.getPricePerHour()
        );
    }


}
