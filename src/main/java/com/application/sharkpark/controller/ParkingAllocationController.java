package com.application.sharkpark.controller;

import com.application.sharkpark.domain.ParkingAllocation;
import com.application.sharkpark.dto.request.CreateParkingAllocationRequest;
import com.application.sharkpark.dto.response.ParkingAllocationResponse;
import com.application.sharkpark.enums.Feature;
import com.application.sharkpark.enums.ParkingAllocationStatus;
import com.application.sharkpark.service.ParkingAllocationService;
import com.application.sharkpark.service.SecurityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/allocations")
    public List<ParkingAllocationResponse> getAllocations(
            @RequestParam(required = false) ParkingAllocationStatus status,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(required = false) @Positive Integer limit) {
        return parkingAllocationService.getAllocations(status, direction, limit);
    }

    @GetMapping("/members/{memberUserId}/allocations")
    public List<ParkingAllocationResponse> getAllocationsByMember(
            @PathVariable Integer memberUserId,
            @RequestParam(required = false) ParkingAllocationStatus status,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(required = false) @Positive Integer limit) {
        return parkingAllocationService.getAllocationsByMember(memberUserId, status, direction, limit);
    }

    @GetMapping("/parking-lots/{parkingLotId}/allocations")
    public List<ParkingAllocationResponse> getAllocationsByParkingLot(
            @PathVariable Integer parkingLotId,
            @RequestParam(required = false) ParkingAllocationStatus status,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(required = false) @Positive Integer limit) {
        return parkingAllocationService.getAllocationsByParkingLot(parkingLotId, status, direction, limit);
    }
}
