
package com.application.sharkpark.controller;

import com.application.sharkpark.dto.request.CreateDivisionRequest;
import com.application.sharkpark.dto.response.DivisionResponse;
import com.application.sharkpark.enums.Feature;
import com.application.sharkpark.service.DivisionService;
import com.application.sharkpark.service.SecurityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DivisionControllerTest {

    private DivisionService divisionService;
    private SecurityService securityService;
    private DivisionController divisionController;

    @BeforeEach
    void setUp() {
        divisionService = mock(DivisionService.class);
        securityService = mock(SecurityService.class);

        divisionController = new DivisionController(
                divisionService,
                securityService
        );
    }

    @Test
    void shouldCreateDivisionAndReturn201() {
        // Arrange
        CreateDivisionRequest request = new CreateDivisionRequest(
                "Switch",
                "Fully",
                1
        );

        DivisionResponse expectedResponse = new DivisionResponse(
                1,
                "Switch",
                "Fully",
                "Yv Es"
        );

        String authorization = "Bearer test-token";

        when(divisionService.createDivision(request))
                .thenReturn(expectedResponse);


        ResponseEntity<DivisionResponse> response =
                divisionController.createDivision(request, authorization);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Switch", response.getBody().name());
        assertEquals("Fully", response.getBody().originalName());
        assertEquals("Yv Es", response.getBody().directorName());

        verify(securityService).validateAuthorization(
                authorization,
                Feature.CREATE_DIVISION
        );
        verify(divisionService).createDivision(request);
    }

    @Test
    void shouldNotCreateDivisionWhenAuthorizationFails() {
        CreateDivisionRequest request = new CreateDivisionRequest(
                "Switch",
                "Fully",
                1
        );

        String authorization = "Bearer invalid-token";

        doThrow(new RuntimeException("Unauthorized"))
                .when(securityService)
                .validateAuthorization(authorization, Feature.CREATE_DIVISION);

        assertThrows(
                RuntimeException.class,
                () -> divisionController.createDivision(request, authorization)
        );

        verify(securityService).validateAuthorization(
                authorization,
                Feature.CREATE_DIVISION
        );

        verifyNoInteractions(divisionService);
    }

    @Test
    void shouldValidateAuthorizationBeforeCreatingDivision() {
        // Arrange
        CreateDivisionRequest request = new CreateDivisionRequest(
                "ParkShark Belgium",
                "Belgium Parking Company",
                1
        );

        String authorization = "Bearer test-token";

        when(divisionService.createDivision(request))
                .thenReturn(new DivisionResponse(
                        1,
                        "ParkShark Belgium",
                        "Belgium Parking Company",
                        "John Doe"
                ));

        // Act
        divisionController.createDivision(request, authorization);

        // Assert
        var inOrder = inOrder(securityService, divisionService);
        inOrder.verify(securityService).validateAuthorization(
                authorization,
                Feature.CREATE_DIVISION
        );
        inOrder.verify(divisionService).createDivision(request);
    }
}
