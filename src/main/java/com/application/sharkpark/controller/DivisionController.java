package com.application.sharkpark.controller;

import com.application.sharkpark.dto.request.CreateDivisionRequest;
import com.application.sharkpark.dto.response.DivisionResponse;
import com.application.sharkpark.service.DivisionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("divisions")
public class DivisionController {
    private final DivisionService divisionService;
    public DivisionController(DivisionService divisionService) {
        this.divisionService = divisionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DivisionResponse> createDivision(@RequestBody CreateDivisionRequest request) {
        DivisionResponse response = divisionService.createDivision(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
