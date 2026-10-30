package com.application.sharkpark.service;

import com.application.sharkpark.domain.ParkingAllocation;
import com.application.sharkpark.domain.ParkingLot;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.CreateParkingAllocationRequest;
import com.application.sharkpark.dto.request.ParkingAllocationResponse;
import com.application.sharkpark.exceptions.NotFoundException;
import com.application.sharkpark.exceptions.RequestException;
import com.application.sharkpark.exceptions.UserNotFoundException;
import com.application.sharkpark.mapper.ParkingAllocationMapper;
import com.application.sharkpark.repository.ParkingAllocationRepository;
import com.application.sharkpark.repository.ParkingLotRepository;
import com.application.sharkpark.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ParkingAllocationService {
    private static final Logger logger = LoggerFactory.getLogger(ParkingAllocationService.class);

    private final ParkingAllocationRepository parkingAllocationRepository;
    private final UserRepository userRepository;
    private final ParkingLotRepository parkingLotRepository;

    public ParkingAllocationService(ParkingAllocationRepository parkingAllocationRepository, UserRepository userRepository, ParkingLotRepository parkingLotRepository) {
        this.parkingAllocationRepository = parkingAllocationRepository;
        this.userRepository = userRepository;
        this.parkingLotRepository = parkingLotRepository;
    }

    @Transactional
    public ParkingAllocationResponse createParkingAllocation(CreateParkingAllocationRequest request) {
        logger.info("Received POST parking allocation with memberID = {} and parkingLotID = {}",
                request.memberId(), request.memberId());

        User member = getMember(request.memberId());
        validateLicensePlate(member, request.licensePlate());

        ParkingLot parkingLot = getParkingLot(request.parkingLotId());

        ParkingAllocation parkingAllocation = parkingLot.allocateSpot(member, request.licensePlate());

        parkingLotRepository.save(parkingLot);
        ParkingAllocation savedParkingAllocation = parkingAllocationRepository.save(parkingAllocation);

        logger.info("Saved parking allocation with ID = {}", savedParkingAllocation.getParkingAllocationId());

        return ParkingAllocationMapper.toResponse(savedParkingAllocation);

    }

    private User getMember(int memberId) {
        return userRepository.findByUserId(memberId)
                .orElseThrow(() -> new UserNotFoundException(memberId));
    }

    private void validateLicensePlate(User member, String licensePlate) {
        if (!member.isValidLicensePlate((licensePlate))) {
            throw RequestException.invalidField("licensePlate");
        }

    }

    private ParkingLot getParkingLot(int parkingLotId) {
        return parkingLotRepository.findByParkingLotIdForUpdate(parkingLotId)
                .orElseThrow(() -> new NotFoundException("ParkingLot not found with ID = " + parkingLotId));
    }
}
