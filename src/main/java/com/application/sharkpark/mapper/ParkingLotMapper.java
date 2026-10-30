package com.application.sharkpark.mapper;

import com.application.sharkpark.domain.City;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.response.CityResponse;
import com.application.sharkpark.domain.ParkingLot;
import com.application.sharkpark.dto.request.ParkingLotRequest;
import com.application.sharkpark.dto.response.ParkingLotResponse;
import com.application.sharkpark.repository.CityRepository;
import com.application.sharkpark.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ParkingLotMapper {

    public ParkingLot mapToParkingLot(
            ParkingLotRequest parkingLotRequest,
            User contactPerson,
            City city) {


        return new ParkingLot(
                parkingLotRequest.getName(),
                parkingLotRequest.getCategory(),
                parkingLotRequest.getMaxCapacity(),
                parkingLotRequest.getAddressStreet(),
                parkingLotRequest.getAddressNumber(),
                parkingLotRequest.getPricePerHour(),
                contactPerson,
                city
        );
    }

    public ParkingLotResponse mapToParkingLotResponse(ParkingLot parkingLot) {
        return new ParkingLotResponse(
                parkingLot.getId(),
                parkingLot.getName(),
                parkingLot.getCategory(),
                parkingLot.getMaxCapacity(),
                parkingLot.getAddressStreet(),
                parkingLot.getAddressNumber(),
                parkingLot.getPricePerHour(),

                UserMapper.toUserResponse(parkingLot.getContactPerson()),

                new CityResponse(
                        parkingLot.getCity().getName(),
                        parkingLot.getCity().getPostalCode()
                )
        );
    }


}
