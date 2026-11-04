package com.application.sharkpark.controller;

import com.application.sharkpark.dto.request.CreateParkingAllocationRequest;
import com.application.sharkpark.dto.request.StopParkingAllocationRequest;
import com.application.sharkpark.dto.response.ParkingAllocationResponse;
import com.application.sharkpark.enums.Feature;
import com.application.sharkpark.enums.ParkingAllocationStatus;
import com.application.sharkpark.service.ParkingAllocationService;
import com.application.sharkpark.service.SecurityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("parking-allocations")
public class ParkingAllocationController {
    private final ParkingAllocationService parkingAllocationService;
    private final Logger logger = LoggerFactory.getLogger(ParkingAllocationController.class);
    private final SecurityService securityService;

    public ParkingAllocationController(ParkingAllocationService parkingAllocationService,
                                       SecurityService securityService) {
        this.parkingAllocationService = parkingAllocationService;
        this.securityService = securityService;
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ParkingAllocationResponse createParkingAllocation(
            @RequestHeader(value = "Authorization", required = false)
            String authorization,
            @Valid @RequestBody CreateParkingAllocationRequest request) {
        securityService.validateAuthorization(authorization, Feature.CREATE_PARKING_ALLOCATION);
        return parkingAllocationService.createParkingAllocation(request);
    }

    @PostMapping(path = "stop", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public void stopParkingAllocation(@RequestHeader(value = "Authorization", required = false)
                                          String authorization,
                                      @Valid @RequestBody StopParkingAllocationRequest request) {
        securityService.validateAuthorization(authorization, Feature.STOP_PARKING_ALLOCATION);
        parkingAllocationService.stopParkingAllocation(request);
    }

    @GetMapping(path = "/allocations", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ParkingAllocationResponse> getAllocations(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestParam(required = false) ParkingAllocationStatus status,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(required = false) @Positive Integer limit) {
        securityService.validateAuthorization(authorization, Feature.GET_PARKING_ALLOCATION);
        logger.info("Entered: getAllParkingAllocations");
        return parkingAllocationService.getAllocations(status, direction, limit);
    }

    @GetMapping(path = "/members/{memberUserId}/allocations", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ParkingAllocationResponse> getAllocationsByMember(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Integer memberUserId,
            @RequestParam(required = false) ParkingAllocationStatus status,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(required = false) @Positive Integer limit) {
        securityService.validateAuthorization(authorization, Feature.GET_PARKING_ALLOCATION);
        logger.info("Entered: getAllParkingAllocations by members");
        return parkingAllocationService.getAllocationsByMember(memberUserId,
                                                               status,
                                                               direction,
                                                               limit);
    }

    @GetMapping(path = "/parking-lots/{parkingLotId}/allocations", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ParkingAllocationResponse> getAllocationsByParkingLot(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @PathVariable Integer parkingLotId,
            @RequestParam(required = false) ParkingAllocationStatus status,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(required = false) @Positive Integer limit) {
        securityService.validateAuthorization(authorization, Feature.GET_PARKING_ALLOCATION);
        logger.info("Entered: getAllParkingAllocations by parking lots");
        return parkingAllocationService.getAllocationsByParkingLot(parkingLotId,
                                                                   status,
                                                                   direction,
                                                                   limit);
    }
}
