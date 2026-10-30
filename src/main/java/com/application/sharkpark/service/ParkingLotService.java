package com.application.sharkpark.service;

import com.application.sharkpark.domain.City;
import com.application.sharkpark.domain.ParkingLot;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.ParkingLotRequest;
import com.application.sharkpark.dto.response.ParkingLotOverviewResponse;
import com.application.sharkpark.dto.response.ParkingLotResponse;
import com.application.sharkpark.mapper.ParkingLotMapper;
import com.application.sharkpark.repository.CityRepository;
import com.application.sharkpark.repository.ParkingLotRepository;
import com.application.sharkpark.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingLotService {

    private final Logger logger = LoggerFactory.getLogger(ParkingLotService.class);

    private final ParkingLotRepository parkingLotRepository;

    private final ParkingLotMapper parkingLotMapper;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    public ParkingLotService(ParkingLotRepository parkingLotRepository, ParkingLotMapper parkingLotMapper, UserRepository userRepository, CityRepository cityRepository) {
        this.parkingLotRepository = parkingLotRepository;
        this.parkingLotMapper = parkingLotMapper;
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
    }

    public ParkingLotResponse createParkingLot(ParkingLotRequest parkingLotRequest) {

        User contactPerson = userRepository
                .findById(parkingLotRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        City city = cityRepository
                .findById(parkingLotRequest.getCityId())
                .orElseThrow(() -> new IllegalArgumentException("City not found"));

        ParkingLot parkingLot = parkingLotMapper.mapToParkingLot(parkingLotRequest, contactPerson, city);

        logger.info("Creating parking lot: {}", parkingLot.getName());

        ParkingLot savedParkingLot = parkingLotRepository.save(parkingLot);

        logger.info("Parking lot created with id: {}", savedParkingLot.getId());

        return parkingLotMapper.mapToParkingLotResponse(savedParkingLot);
    }

    public List<ParkingLotOverviewResponse> getAllParkingLots() {

       List<ParkingLot> parkingLotsList = parkingLotRepository.findAll();

       return parkingLotsList.stream()
               .map(parkingLotMapper::mapToParkingLotOverviewResponse)
               .collect(Collectors.toList());
    }
}
