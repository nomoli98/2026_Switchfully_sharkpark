package com.application.sharkpark.service;

import com.application.sharkpark.domain.ParkingAllocation;
import com.application.sharkpark.domain.ParkingLot;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.CreateParkingAllocationRequest;
import com.application.sharkpark.dto.request.StopParkingAllocationRequest;
import com.application.sharkpark.dto.response.ParkingAllocationResponse;
import com.application.sharkpark.enums.ParkingAllocationStatus;
import com.application.sharkpark.exceptions.*;
import com.application.sharkpark.mapper.ParkingAllocationMapper;
import com.application.sharkpark.repository.ParkingAllocationRepository;
import com.application.sharkpark.repository.ParkingLotRepository;
import com.application.sharkpark.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;


@Service
@Transactional
public class ParkingAllocationService {
    private static final Logger logger = LoggerFactory.getLogger(ParkingAllocationService.class);

    private final ParkingAllocationRepository parkingAllocationRepository;
    private final UserRepository userRepository;
    private final ParkingLotRepository parkingLotRepository;

    public ParkingAllocationService(ParkingAllocationRepository parkingAllocationRepository,
                                    UserRepository userRepository,
                                    ParkingLotRepository parkingLotRepository) {
        this.parkingAllocationRepository = parkingAllocationRepository;
        this.userRepository = userRepository;
        this.parkingLotRepository = parkingLotRepository;
    }

    public ParkingAllocationResponse createParkingAllocation(CreateParkingAllocationRequest request) {
        logger.info("Received CREATE parking allocation request with memberID = {} and parkingLotID = {}",
                request.memberId(), request.parkingLotId());

        User member = getMemberById(request.memberId());
        validateLicensePlate(member, request.licensePlate());

        ParkingLot parkingLot = getParkingLotById(request.parkingLotId());

        validateParkingAllocationRequest(request.licensePlate());

        ParkingAllocation parkingAllocation = parkingLot.allocateSpot(member,
                                                                      request.licensePlate());

        parkingLotRepository.save(parkingLot);
        ParkingAllocation savedParkingAllocation = parkingAllocationRepository.save(
                parkingAllocation);

        logger.info("Saved parking allocation with ID = {}",
                    savedParkingAllocation.getParkingAllocationId());

        return ParkingAllocationMapper.toResponse(savedParkingAllocation);

    }

    public List<ParkingAllocationResponse> getAllocations(ParkingAllocationStatus status,
                                                          Sort.Direction direction,
                                                          Integer limit) {
        return findAllocations(null, null, status, direction, limit);
    }

    public List<ParkingAllocationResponse> getAllocationsByMember(Integer memberUserId,
                                                          ParkingAllocationStatus status,
                                                          Sort.Direction direction,
                                                          Integer limit) {
        return findAllocations(memberUserId, null, status, direction, limit);
    }

    public List<ParkingAllocationResponse> getAllocationsByParkingLot(Integer parkingLotId,
                                                                      ParkingAllocationStatus status,
                                                                      Sort.Direction direction,
                                                                      Integer limit) {
        return findAllocations(null, parkingLotId, status, direction, limit);
    }


    private List<ParkingAllocationResponse> findAllocations(Integer memberUserId,
                                                            Integer parkingLotId,
                                                            ParkingAllocationStatus status,
                                                            Sort.Direction direction,
                                                            Integer limit) {
        Specification<ParkingAllocation> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Sort sort = Sort.by(direction, "startTime");

        if (limit == null) {
            return ParkingAllocationMapper.toResponseList(parkingAllocationRepository.findAll(spec,
                                                                                              sort));
        }

        return ParkingAllocationMapper.toResponseList(parkingAllocationRepository.findAll(spec,
                                                                                          PageRequest.of(
                                                                                                  0,
                                                                                                  limit,
                                                                                                  sort))
                                                                                 .getContent());
    }


    public void stopParkingAllocation(StopParkingAllocationRequest request) {
        logger.info("Received STOP parking allocation request with memberID = {} and parkingAllocationID = {}",
                request.memberId(), request.parkingAllocationId());

        User member = getMemberById(request.memberId());

        ParkingAllocation parkingAllocation = getParkingAllocationById(request.parkingAllocationId());

        validateParkingAllocationOwner(parkingAllocation.getMember().getUserId(), member.getUserId());

        parkingAllocation.stopParkingAllocation();
        parkingAllocationRepository.save(parkingAllocation);
    }

    private User getMemberById(int memberId) {
        return userRepository.findById(memberId)
                             .orElseThrow(() -> new UserNotFoundException(memberId));
    }

    private ParkingAllocation getParkingAllocationById(int parkingAllocationId) {
        return parkingAllocationRepository.findById(parkingAllocationId)
                .orElseThrow(() -> new NotFoundException("ParkingAllocation not found with ID = " + parkingAllocationId));
    }

    private void validateParkingAllocationOwner(int parkingAllocationId, int memberId) {
        if (parkingAllocationId != memberId) {
            throw new InvalidParkingAllocationOwnerException(parkingAllocationId, memberId);
        }
    }

    private void validateLicensePlate(User member, String licensePlate) {
        if (!member.isValidLicensePlate((licensePlate))) {
            throw RequestException.invalidField("licensePlate");
        }
    }

    private ParkingLot getParkingLotById(int parkingLotId) {
        return parkingLotRepository.findByParkingLotIdForUpdate(parkingLotId)
                                   .orElseThrow(() -> new NotFoundException(
                                           "ParkingLot not found with ID = " + parkingLotId));
    }

    private void validateParkingAllocationRequest(String licensePlate) {
        List<ParkingAllocation> parkingAllocations = parkingAllocationRepository
                .findAllByLicensePlateAndStatus(licensePlate, ParkingAllocationStatus.ACTIVE);

        if (!parkingAllocations.isEmpty()) {
            throw new ActiveParkingAllocationExistException(licensePlate,
                                                            parkingAllocations.get(0)
                                                                              .getParkingAllocationId());
        }
    }

}