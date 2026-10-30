
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DivisionServiceTest {

    private DivisionRepository divisionRepository;
    private UserRepository userRepository;
    private DivisionService divisionService;
    private User director;
    private CreateDivisionRequest validCreateDivisionRequest;
    private CreateDivisionRequest invalidCreateDivisionRequest;

    @BeforeEach
    void setUp() {
        divisionRepository = mock(DivisionRepository.class);
        userRepository = mock(UserRepository.class);

        divisionService = new DivisionService(
                divisionRepository,
                userRepository
        );
        addDirector();
        addCreateDivisionRequests();
    }

    @Test
    void createDivisionWhenNameIsAvailableAndDirectorExists() {

        when(divisionRepository.existsByName(validCreateDivisionRequest.name()))
                .thenReturn(false);

        when(userRepository.findById(1))
                .thenReturn(Optional.of(director));

        when(divisionRepository.save(any(Division.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DivisionResponse response =
                divisionService.createDivision(validCreateDivisionRequest);

        assertNotNull(response);
        assertEquals("Valid ParkShark Belgium", response.name());
        assertEquals(
                "Belgium Parking Company",
                response.originalName()
        );
        assertEquals("Yv Es", response.directorName());

        verify(divisionRepository).existsByName(validCreateDivisionRequest.name());
        verify(userRepository).findById(1);
        verify(divisionRepository).save(any(Division.class));
    }

    @Test
    void throwExceptionWhenDivisionNameAlreadyExists() {

        when(divisionRepository.existsByName(validCreateDivisionRequest.name()))
                .thenReturn(true);

        assertThrows(
                DivisionNameAlreadyExistException.class,
                () -> divisionService.createDivision(validCreateDivisionRequest)
        );

        verify(divisionRepository).existsByName(validCreateDivisionRequest.name());
        verifyNoInteractions(userRepository);
        verify(divisionRepository, never()).save(any(Division.class));
    }

    @Test
    void throwExceptionWhenDirectorDoesNotExist() {

        when(divisionRepository.existsByName(invalidCreateDivisionRequest.name()))
                .thenReturn(false);

        when(userRepository.findById(99))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> divisionService.createDivision(invalidCreateDivisionRequest)
        );

        verify(userRepository).findById(99);
        verify(divisionRepository, never()).save(any(Division.class));
    }

    @Nested
    class CreateSubdivision {

        @Test
        void givenNonExistingDivision_thenThrowDivisionNotFoundException() {
            assertThrows(DivisionNotFoundException.class,
                    () -> divisionService.createSubdivision(validCreateDivisionRequest, 2));
        }


            @Test
            void givenValidDirectorAndDivision_whenCreateSubdivision_thenReturnsDivisionResponse() {
                Division parentDivision = new Division("ParkShark Belgium","Belgium Parking Company", director);

                when(divisionRepository.existsByName(any())).thenReturn(false);
                when(userRepository.findById(any())).thenReturn(Optional.of(director));

                when(divisionRepository.findByDivisionId(1))
                        .thenReturn(Optional.of(parentDivision));

                when(divisionRepository.save(any(Division.class)))
                        .thenAnswer(invocation -> invocation.getArgument(0));

                DivisionResponse response =
                        divisionService.createSubdivision(validCreateDivisionRequest, 1);

                assertNotNull(response);
                assertEquals("ParkShark Belgium", response.name());
                assertEquals("Belgium Parking Company", response.originalName());
                assertEquals("Yv Es", response.directorName());
                assertEquals("Valid ParkShark Belgium", response.subdivisions().get(0).name());

                verify(divisionRepository).findByDivisionId(1);
                verify(divisionRepository, times(2)).save(any(Division.class));
        }
    }




    private void addDirector() {
        director = new User();
        director.setUserId(1);
        director.setFirstname("Yv");
        director.setLastname("Es");
    }

    private void addCreateDivisionRequests() {
        validCreateDivisionRequest = new CreateDivisionRequest(
                "Valid ParkShark Belgium",
                "Belgium Parking Company",
                1
        );
        invalidCreateDivisionRequest = new CreateDivisionRequest(
                "Invalid ParkShark Belgium",
                "Belgium Parking Company",
                99
        );
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
