package com.application.sharkpark.parkingLot.controller;

import com.application.sharkpark.enums.Feature;
import com.application.sharkpark.parkingLot.dto.request.ParkingLotRequest;
import com.application.sharkpark.parkingLot.dto.response.ParkingLotResponse;
import com.application.sharkpark.parkingLot.service.ParkingLotService;
import com.application.sharkpark.service.SecurityService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkinglot")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;
    private final Logger logger = LoggerFactory.getLogger(ParkingLotController.class);
    private final SecurityService securityService;


    public ParkingLotController(ParkingLotService parkingLotService, SecurityService securityService) {
        this.parkingLotService = parkingLotService;
        this.securityService = securityService;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ParkingLotResponse createParkingLot(@Valid @RequestBody ParkingLotRequest parkingLotRequest,
                                               @RequestHeader(value = "Authorization", required = false) String authorization) {
        securityService.validateAuthorization(authorization, Feature.CREATE_PARKING_LOT);
        logger.info("Entered: createParkingLot");
        return parkingLotService.createParkingLot(parkingLotRequest);
    }

}
