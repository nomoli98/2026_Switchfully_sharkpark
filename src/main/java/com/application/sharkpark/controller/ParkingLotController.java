package com.application.sharkpark.controller;

import com.application.sharkpark.dto.response.ParkingLotOverviewResponse;
import com.application.sharkpark.enums.Feature;
import com.application.sharkpark.dto.request.ParkingLotRequest;
import com.application.sharkpark.dto.response.ParkingLotResponse;
import com.application.sharkpark.service.ParkingLotService;
import com.application.sharkpark.service.SecurityService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json")
    public List<ParkingLotOverviewResponse> getAllParkingLots(@RequestHeader(value = "Authorization", required = false) String authorization) {
        securityService.validateAuthorization(authorization, Feature.GET_ALL_PARKING_LOTS);

        logger.info("Entered: getAllParkingLots");

        return parkingLotService.getAllParkingLots();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{parkingLotId}", produces = "application/json")
    public ParkingLotResponse getParkingLot(@PathVariable("parkingLotId") int parkingLotId,
                                            @RequestHeader(value = "Authorization",  required = false) String authorization) {
        securityService.validateAuthorization(authorization, Feature.GET_PARKING_LOT);

        logger.info("Entered: getParkingLot");

        return parkingLotService.getParkingLot(parkingLotId);

    }

}
