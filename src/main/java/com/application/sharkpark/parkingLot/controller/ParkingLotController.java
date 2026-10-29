package com.application.sharkpark.parkingLot.controller;

import com.application.sharkpark.parkingLot.dto.request.ParkingLotRequest;
import com.application.sharkpark.parkingLot.dto.response.ParkingLotResponse;
import com.application.sharkpark.parkingLot.service.ParkingLotService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ParkingLotController {

    private final ParkingLotService parkingLotService;
    private final Logger logger = LoggerFactory.getLogger(ParkingLotController.class);


    public ParkingLotController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    //needs auth-checks
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ParkingLotResponse createParkingLot(@Valid @RequestBody ParkingLotRequest parkingLotRequest) {

        logger.info("Entered: createParkingLot");
        return parkingLotService.createParkingLot(parkingLotRequest);
    }

}
