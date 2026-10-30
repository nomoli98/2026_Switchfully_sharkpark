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
    private static final String APPLICATION_JSON = "application/json";

    private final DivisionService divisionService;
    private final SecurityService securityService;

    public DivisionController(DivisionService divisionService, SecurityService securityService) {
        this.divisionService = divisionService;
        this.securityService = securityService;
    }

    @PostMapping
    public ResponseEntity<DivisionResponse> createDivision(@RequestBody CreateDivisionRequest request,
                                                           @RequestHeader (value ="Authorization", required = false) String authorization) {
        securityService.validateAuthorization(authorization, Feature.CREATE_DIVISION);

        DivisionResponse response = divisionService.createDivision(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(produces = APPLICATION_JSON)
    @ResponseStatus(HttpStatus.OK)
    public List<DivisionResponse> getAllDivisions(@RequestHeader(value = "Authorization", required = false)
                                                  String authorization) {
        securityService.validateAuthorization(authorization, Feature.GET_DIVISION);
        return divisionService.getAllDivisions();
    }

    @PostMapping(path = "{id}/subdivisions", produces = APPLICATION_JSON, consumes = APPLICATION_JSON)
    @ResponseStatus(HttpStatus.CREATED)
    public DivisionResponse createSubdivision(@RequestHeader(value = "Authorization", required = false) String authorization,
                                              @RequestBody CreateDivisionRequest createDivisionRequest,
                                              @PathVariable("id") int divisionId) {
        securityService.validateAuthorization(authorization, Feature.CREATE_DIVISION);
        return divisionService.createSubdivision(createDivisionRequest, divisionId);
    }
    @GetMapping("/{divisionId}")
    public ResponseEntity<DivisionResponse> getDivision(@PathVariable Integer divisionId,
                                                        @RequestHeader(value = "Authorization", required = false) String authorization) {
        securityService.validateAuthorization(authorization, Feature.GET_DIVISION);

        DivisionResponse response = divisionService.getDivisionById(divisionId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
