
package com.application.sharkpark.service;

import com.application.sharkpark.domain.Division;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.CreateDivisionRequest;
import com.application.sharkpark.dto.response.DivisionResponse;
import com.application.sharkpark.exceptions.DivisionNameAlreadyExistException;
import com.application.sharkpark.exceptions.DivisionNotFoundException;
import com.application.sharkpark.exceptions.UserNotFoundException;
import com.application.sharkpark.repository.DivisionRepository;
import com.application.sharkpark.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DivisionServiceTest {

    private DivisionRepository divisionRepository;
    private UserRepository userRepository;
    private DivisionService divisionService;

    @BeforeEach
    void setUp() {
        divisionRepository = mock(DivisionRepository.class);
        userRepository = mock(UserRepository.class);

        divisionService = new DivisionService(
                divisionRepository,
                userRepository
        );
    }

    @Test
    void createDivisionWhenNameIsAvailableAndDirectorExists() {
        CreateDivisionRequest request = new CreateDivisionRequest(
                "ParkShark Belgium",
                "Belgium Parking Company",
                1
        );

        User director = new User();
        director.setUserId(1);
        director.setFirstname("Yv");
        director.setLastname("Es");

        when(divisionRepository.existsByName(request.name()))
                .thenReturn(false);

        when(userRepository.findById(1))
                .thenReturn(Optional.of(director));

        when(divisionRepository.save(any(Division.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DivisionResponse response =
                divisionService.createDivision(request);

        assertNotNull(response);
        assertEquals("ParkShark Belgium", response.name());
        assertEquals(
                "Belgium Parking Company",
                response.originalName()
        );
        assertEquals("Yv Es", response.directorName());

        verify(divisionRepository).existsByName(request.name());
        verify(userRepository).findById(1);
        verify(divisionRepository).save(any(Division.class));
    }

    @Test
    void throwExceptionWhenDivisionNameAlreadyExists() {
        CreateDivisionRequest request = new CreateDivisionRequest(
                "ParkShark Belgium",
                "Belgium Parking Company",
                1
        );

        when(divisionRepository.existsByName(request.name()))
                .thenReturn(true);

        assertThrows(
                DivisionNameAlreadyExistException.class,
                () -> divisionService.createDivision(request)
        );

        verify(divisionRepository).existsByName(request.name());
        verifyNoInteractions(userRepository);
        verify(divisionRepository, never()).save(any(Division.class));
    }

    @Test
    void throwExceptionWhenDirectorDoesNotExist() {
        CreateDivisionRequest request = new CreateDivisionRequest(
                "ParkShark Belgium",
                "Belgium Parking Company",
                99
        );

        when(divisionRepository.existsByName(request.name()))
                .thenReturn(false);

        when(userRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> divisionService.createDivision(request)
        );

        verify(userRepository).findById(99);
        verify(divisionRepository, never()).save(any(Division.class));
    }

    @Test
    void returnDivisionWhenIdExists() {
        User director = new User();
        director.setFirstname("Yv");
        director.setLastname("Es");

        Division division = new Division(
                "ParkShark Belgium",
                "Switch Fully",
                director
        );

        when(divisionRepository.findById(1))
                .thenReturn(Optional.of(division));

        DivisionResponse response = divisionService.getDivisionById(1);

        assertNotNull(response);
        assertEquals("ParkShark Belgium", response.name());
        assertEquals("Switch Fully", response.originalName());
        assertEquals("Yv Es", response.directorName());

        verify(divisionRepository).findById(1);
    }

    @Test
    void throwExceptionWhenDivisionDoesNotExist() {
        when(divisionRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                DivisionNotFoundException.class,
                () -> divisionService.getDivisionById(99)
        );

        verify(divisionRepository).findById(99);
    }
}
