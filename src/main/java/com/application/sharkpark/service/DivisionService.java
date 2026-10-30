package com.application.sharkpark.service;

import com.application.sharkpark.domain.Division;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.CreateDivisionRequest;
import com.application.sharkpark.dto.response.DivisionResponse;
import com.application.sharkpark.exceptions.DivisionNameAlreadyExistException;
import com.application.sharkpark.exceptions.UserNotFoundException;
import com.application.sharkpark.mapper.DivisionMapper;
import com.application.sharkpark.repository.DivisionRepository;
import com.application.sharkpark.repository.member.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.application.sharkpark.repository.UserRepository;
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
        if (divisionRepository.existsByName(createDivisionRequest.name())) {
            throw new DivisionNameAlreadyExistException(createDivisionRequest.name());
        }
        User director = userRepository.findById(createDivisionRequest.directorId())
                .orElseThrow(()-> new UserNotFoundException(createDivisionRequest.directorId()));

        Division division = DivisionMapper.toEntity(createDivisionRequest, director );

        Division savedDivision =divisionRepository.save(division);

        return DivisionMapper.toResponse(savedDivision);

    }

    public List<DivisionResponse> getAllDivisions() {
        logger.info("Received GET all Division request");
        List<Division> divisions = divisionRepository.findAll();
        logger.info("Fetched {} divisions", divisions.size());

        return DivisionMapper.toResponse(divisions);
    }


}
