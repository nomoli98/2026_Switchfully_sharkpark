package com.application.sharkpark.controller;

import com.application.sharkpark.dto.request.CreateParkingAllocationRequest;
import com.application.sharkpark.dto.request.ParkingAllocationResponse;
import com.application.sharkpark.enums.Feature;
import com.application.sharkpark.service.ParkingAllocationService;
import com.application.sharkpark.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("parking-allocations")
public class ParkingAllocationController {
    private final ParkingAllocationService parkingAllocationService;
    private final SecurityService securityService;

    public ParkingAllocationController(ParkingAllocationService parkingAllocationService, SecurityService securityService) {
        this.parkingAllocationService = parkingAllocationService;
        this.securityService = securityService;
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ParkingAllocationResponse createParkingAllocation(@RequestHeader(value = "Authorization", required = false)
                                                                 String authorization,
                                                             @Valid @RequestBody CreateParkingAllocationRequest createParkingAllocationRequest) {
        securityService.validateAuthorization(authorization, Feature.CREATE_PARKING_ALLOCATION);
        return parkingAllocationService.createParkingAllocation(createParkingAllocationRequest);
    }
}
