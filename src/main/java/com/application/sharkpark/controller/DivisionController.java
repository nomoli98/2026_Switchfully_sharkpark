package com.application.sharkpark.controller;

import com.application.sharkpark.dto.request.CreateDivisionRequest;
import com.application.sharkpark.dto.response.DivisionResponse;
import com.application.sharkpark.enums.Feature;
import com.application.sharkpark.service.DivisionService;
import com.application.sharkpark.service.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("divisions")
public class DivisionController {
    private final DivisionService divisionService;
    private final SecurityService securityService;

    public DivisionController(DivisionService divisionService, SecurityService securityService) {
        this.divisionService = divisionService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DivisionResponse> createDivision(@RequestBody CreateDivisionRequest request) {
        DivisionResponse response = divisionService.createDivision(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<DivisionResponse> getAllDivisions(@RequestHeader(value = "Authorization", required = false)
                                                  String authorization) {
        securityService.validateAuthorization(authorization, Feature.GET_DIVISION);
        return divisionService.getAllDivisions();
    }


}
