package com.application.sharkpark.service;

import com.application.sharkpark.domain.Division;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.CreateDivisionRequest;
import com.application.sharkpark.dto.response.DivisionResponse;
import com.application.sharkpark.exceptions.DivisionNameAlreadyExistException;
import com.application.sharkpark.exceptions.DivisionNotFoundException;
import com.application.sharkpark.exceptions.UserNotFoundException;
import com.application.sharkpark.mapper.DivisionMapper;
import com.application.sharkpark.repository.DivisionRepository;
import com.application.sharkpark.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DivisionService {
    private static final Logger logger = LoggerFactory.getLogger(DivisionService.class);
    private final DivisionRepository divisionRepository;
    private final UserRepository userRepository;

    public DivisionService(DivisionRepository divisionRepository, UserRepository userRepository) {
        this.divisionRepository = divisionRepository;
        this.userRepository = userRepository;
    }

    public DivisionResponse createDivision(CreateDivisionRequest createDivisionRequest) {
        logger.info("Received POST Division request with name = {} and directorID = {}",
                    createDivisionRequest.name(), createDivisionRequest.directorId());

        Division savedDivision = saveDivision(createDivisionRequest);

        logger.info("Saved Division with ID = {}", savedDivision.getDivisionId());

        return DivisionMapper.toResponse(savedDivision);

    }

    public List<DivisionResponse> getAllDivisions() {
        logger.info("Received GET all Division request");
        List<Division> divisions = divisionRepository.findAll();
        logger.info("Fetched {} divisions", divisions.size());

        return DivisionMapper.toResponse(divisions);
    }

    public DivisionResponse createSubdivision(CreateDivisionRequest createDivisionRequest,
                                              int divisionId) {
        logger.info(
                "Received POST Subdivision request with name = {}, directorID = {} and divisionID = {}",
                createDivisionRequest.name(),
                createDivisionRequest.directorId(),
                divisionId);

        Division division = divisionRepository.findByDivisionId(divisionId)
                                              .orElseThrow(() -> new DivisionNotFoundException(
                                                      divisionId));

        Division savedSubdivision = saveDivision(createDivisionRequest);

        division.addSubdivision(savedSubdivision);

        Division updatedDivision = divisionRepository.save(division);

        logger.info("Saved Subdivision with ID = {}", savedSubdivision.getDivisionId());

        return DivisionMapper.toResponse(updatedDivision);
    }


    private Division saveDivision(CreateDivisionRequest createDivisionRequest) {
        validateDivisionRequest(createDivisionRequest);

        User director = userRepository.findById(createDivisionRequest.directorId())
                                      .orElseThrow(() -> new UserNotFoundException(
                                              createDivisionRequest.directorId()));

        Division division = DivisionMapper.toEntity(createDivisionRequest, director);

        return divisionRepository.save(division);
    }

    private void validateDivisionRequest(CreateDivisionRequest createDivisionRequest) {
        if (divisionRepository.existsByName(createDivisionRequest.name())) {
            throw new DivisionNameAlreadyExistException(createDivisionRequest.name());
        }
    }

    public DivisionResponse getDivisionById(Integer divisionId) {
        Division division = divisionRepository.findById(divisionId)
                                              .orElseThrow(() -> new DivisionNotFoundException(
                                                      divisionId));

        return DivisionMapper.toResponse(division);
    }


}
