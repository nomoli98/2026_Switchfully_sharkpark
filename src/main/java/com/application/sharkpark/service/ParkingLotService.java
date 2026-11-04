package com.application.sharkpark.service;

import com.application.sharkpark.domain.City;
import com.application.sharkpark.domain.Division;
import com.application.sharkpark.domain.ParkingLot;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.ParkingLotRequest;
import com.application.sharkpark.dto.response.ParkingLotResponse;
import com.application.sharkpark.exceptions.DivisionNotFoundException;
import com.application.sharkpark.mapper.ParkingLotMapper;
import com.application.sharkpark.repository.CityRepository;
import com.application.sharkpark.repository.DivisionRepository;
import com.application.sharkpark.repository.ParkingLotRepository;
import com.application.sharkpark.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ParkingLotService {

    private final Logger logger = LoggerFactory.getLogger(ParkingLotService.class);

    private final ParkingLotRepository parkingLotRepository;
    private final DivisionRepository divisionRepository;

    private final ParkingLotMapper parkingLotMapper;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    public ParkingLotService(ParkingLotRepository parkingLotRepository,
                             ParkingLotMapper parkingLotMapper,
                             UserRepository userRepository,
                             CityRepository cityRepository,
                             DivisionRepository divisionRepository) {
        this.parkingLotRepository = parkingLotRepository;
        this.parkingLotMapper = parkingLotMapper;
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.divisionRepository = divisionRepository;
    }

    public ParkingLotResponse createParkingLot(ParkingLotRequest parkingLotRequest) {

        User contactPerson = userRepository
                .findById(parkingLotRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        City city = cityRepository
                .findById(parkingLotRequest.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("City not found"));

        Division division = divisionRepository
                .findById(parkingLotRequest.getDivisionId())
                .orElseThrow(()-> new DivisionNotFoundException(parkingLotRequest.getDivisionId()));

        ParkingLot parkingLot = parkingLotMapper.mapToParkingLot(parkingLotRequest, contactPerson, city,division);

        logger.info("Creating parking lot: {}", parkingLot.getName());

        ParkingLot savedParkingLot = parkingLotRepository.save(parkingLot);

        logger.info("Parking lot created with id: {}", savedParkingLot.getId());

        return parkingLotMapper.mapToParkingLotResponse(savedParkingLot);
    }
}
