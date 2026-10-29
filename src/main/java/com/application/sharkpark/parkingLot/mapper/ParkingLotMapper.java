package com.application.sharkpark.parkingLot.mapper;

import com.application.sharkpark.domain.member.City;
import com.application.sharkpark.domain.member.User;
import com.application.sharkpark.dto.response.member.CityResponse;
import com.application.sharkpark.dto.response.member.UserResponse;
import com.application.sharkpark.parkingLot.domain.ParkingLot;
import com.application.sharkpark.parkingLot.dto.request.ParkingLotRequest;
import com.application.sharkpark.parkingLot.dto.response.ParkingLotResponse;
import com.application.sharkpark.repository.member.CityRepository;
import com.application.sharkpark.repository.member.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ParkingLotMapper {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    public ParkingLotMapper(UserRepository userRepository, CityRepository cityRepository) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
    }

    public ParkingLot mapToParkingLot(ParkingLotRequest parkingLotRequest){
        Optional<User> contactpersonOptional = userRepository.findById(parkingLotRequest.getUserId());
        if (contactpersonOptional.isEmpty()){
            throw new IllegalArgumentException("User not found");
        }

        Optional<City> cityOptional = cityRepository.findById(parkingLotRequest.getCityId());
        if (cityOptional.isEmpty()){
            throw new IllegalArgumentException("City not found");
        }

        User contactPerson = contactpersonOptional.get();
        City city =  cityOptional.get();

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

    public ParkingLotResponse mapToParkingLotResponse(ParkingLot parkingLot){
        return new ParkingLotResponse(
                parkingLot.getId(),
                parkingLot.getName(),
                parkingLot.getCategory(),
                parkingLot.getMaxCapacity(),
                parkingLot.getAddressStreet(),
                parkingLot.getAddressNumber(),
                parkingLot.getPricePerHour(),

                new UserResponse(
                        parkingLot.getContactPerson().getFirstname()
                        + ""
                        + parkingLot.getContactPerson().getLastname(),
                        parkingLot.getContactPerson().getPhoneNumber(),
                        parkingLot.getContactPerson().getMobileNumber()
                ),

                new CityResponse(
                        parkingLot.getCity().getName(),
                        parkingLot.getCity().getPostalCode()
                )
        );
    }


}
