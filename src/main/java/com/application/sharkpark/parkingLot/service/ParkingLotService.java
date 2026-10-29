package com.application.sharkpark.parkingLot.service;

import com.application.sharkpark.parkingLot.controller.ParkingLotController;
import com.application.sharkpark.parkingLot.domain.ParkingLot;
import com.application.sharkpark.parkingLot.dto.request.ParkingLotRequest;
import com.application.sharkpark.parkingLot.dto.response.ParkingLotResponse;
import com.application.sharkpark.parkingLot.mapper.ParkingLotMapper;
import com.application.sharkpark.parkingLot.repository.ParkingLotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingLotService {

    private final Logger logger = LoggerFactory.getLogger(ParkingLotService.class);

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private ParkingLotMapper parkingLotMapper;

    public ParkingLotResponse createParkingLot(ParkingLotRequest parkingLotRequest) {
        ParkingLot parkingLot = parkingLotMapper.mapToParkingLot(parkingLotRequest);
        logger.info("Creating parking lot: {}", parkingLot.getName());
        ParkingLot savedParkingLot = parkingLotRepository.save(parkingLot);
        logger.info("Parking lot created with id: {}", savedParkingLot.getId());
        return parkingLotMapper.mapToParkingLotResponse(savedParkingLot);
    }
}
