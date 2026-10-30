package com.application.sharkpark.controller;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.application.sharkpark.dto.response.ParkingLotOverviewResponse;
import com.application.sharkpark.dto.response.ParkingLotResponse;
import com.application.sharkpark.enums.Feature;
import com.application.sharkpark.exceptions.UnauthorizedException;
import com.application.sharkpark.service.ParkingLotService;
import com.application.sharkpark.service.SecurityService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParkingLotController.class)
class ParkingLotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ParkingLotService parkingLotService;

    @MockitoBean
    private SecurityService securityService;

    @Test
    void createParkingLot() throws Exception {
        String json = """
                {
                    "name": "Shark Parking",
                    "category": "UNDERGROUND_BUILDING",
                    "maxCapacity": 100,
                    "addressStreet": "Shark Street",
                    "addressNumber": 25,
                    "pricePerHour": 2.50,
                    "userId": 1,
                    "cityId": 2
                }
                """;

        mockMvc.perform(post("/parkinglot")
                        .header("Authorization", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void createParkingLot_nameIsBlank() throws Exception {
        String json = """
                {
                    "name": "",
                    "category": "UNDERGROUND_BUILDING",
                    "maxCapacity": 100,
                    "addressStreet": "Shark Street",
                    "addressNumber": 25,
                    "pricePerHour": 2.50,
                    "userId": 1,
                    "cityId": 2
                }
                """;

        mockMvc.perform(post("/parkinglot")
                        .header("Authorization", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllParkingLots() throws Exception {
        ParkingLotOverviewResponse response =
                new ParkingLotOverviewResponse(
                        1,
                        "Shark Parking",
                        100,
                        "manager@sharkpark.be",
                        "0470123456"
                );

        when(parkingLotService.getAllParkingLots())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/parkinglot")
                        .header("Authorization", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].parkingLotId").value(1))
                .andExpect(jsonPath("$[0].name").value("Shark Parking"))
                .andExpect(jsonPath("$[0].capacity").value(100))
                .andExpect(jsonPath("$[0].contactEmail").value("manager@sharkpark.be"))
                .andExpect(jsonPath("$[0].contactTelephone").value("0470123456"));
    }

    @Test
    void getAllParkingLots_notAuthorized() throws Exception {
        doThrow(new UnauthorizedException())
                .when(securityService)
                .validateAuthorization(null, Feature.GET_ALL_PARKING_LOTS);

        mockMvc.perform(get("/parkinglot"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getParkingLot() throws Exception {

        ParkingLotResponse response = mock(ParkingLotResponse.class);

        when(parkingLotService.getParkingLot(1))
                .thenReturn(response);

        mockMvc.perform(get("/parkinglot/1")
                        .header("Authorization", "test"))
                .andExpect(status().isOk());
    }

    @Test
    void getParkingLot_notAuthorized() throws Exception {
        doThrow(new UnauthorizedException())
                .when(securityService)
                .validateAuthorization(null, Feature.GET_PARKING_LOT);

        mockMvc.perform(get("/parkinglot/1"))
                .andExpect(status().isUnauthorized());
    }



}