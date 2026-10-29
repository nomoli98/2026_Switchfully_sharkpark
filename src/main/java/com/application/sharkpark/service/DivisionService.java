package com.application.sharkpark.service;

import com.application.sharkpark.domain.Division;
import com.application.sharkpark.dto.request.CreateDivisionRequest;
import com.application.sharkpark.dto.response.CreateDivisionResponse;
import com.application.sharkpark.repository.DivisionRepository;
import org.springframework.stereotype.Service;

@Service
public class DivisionService {
    private final DivisionRepository divisionRepository;
    public DivisionService(DivisionRepository divisionRepository) {
        this.divisionRepository = divisionRepository;
    }

    public CreateDivisionResponse createDivision(CreateDivisionRequest createDivisionRequest) {
        if (divisionRepository.existsByName(createDivisionRequest.name())) {
            throw new IllegalArgumentException("Division with name " + createDivisionRequest.name() + " already exists");
        }

        Division division = new Division(
                createDivisionRequest.name(),
                createDivisionRequest.originalName(),
                createDivisionRequest.directorId()
        );

        Division savedDivision =divisionRepository.save(division);

        return new CreateDivisionResponse(
                savedDivision.getDivisionId(),
                savedDivision.getName(),
                savedDivision.getOriginalName(),
                savedDivision.getDirectorId()
        );
    }


}
