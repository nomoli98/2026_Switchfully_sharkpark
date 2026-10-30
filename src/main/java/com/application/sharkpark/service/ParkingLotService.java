package com.application.sharkpark.service;

import com.application.sharkpark.domain.ParkingLot;
import com.application.sharkpark.dto.request.ParkingLotRequest;
import com.application.sharkpark.dto.response.ParkingLotResponse;
import com.application.sharkpark.mapper.ParkingLotMapper;
import com.application.sharkpark.repository.ParkingLotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLotService {

    private final Logger logger = LoggerFactory.getLogger(ParkingLotService.class);

    private final ParkingLotRepository parkingLotRepository;

    private final ParkingLotMapper parkingLotMapper;

    public ParkingLotService(ParkingLotRepository parkingLotRepository, ParkingLotMapper parkingLotMapper) {
        this.parkingLotRepository = parkingLotRepository;
        this.parkingLotMapper = parkingLotMapper;
    }

    public ParkingLotResponse createParkingLot(ParkingLotRequest parkingLotRequest) {
        ParkingLot parkingLot = parkingLotMapper.mapToParkingLot(parkingLotRequest);
        logger.info("Creating parking lot: {}", parkingLot.getName());
        ParkingLot savedParkingLot = parkingLotRepository.save(parkingLot);
        logger.info("Parking lot created with id: {}", savedParkingLot.getId());
        return parkingLotMapper.mapToParkingLotResponse(savedParkingLot);
    }

    public List<ParkingLotResponse> getAllParkingLots() {

        //stream to new ParkingLotOverviewResponse
       List<ParkingLotResponse> overview = parkingLotRepository.findAll();

       return overview;
    }
}
