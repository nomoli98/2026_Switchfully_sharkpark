package com.application.sharkpark.controller;

import com.application.sharkpark.service.ParkingLotService;
import com.application.sharkpark.service.SecurityService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
        // GIVEN
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

        // WHEN + THEN
        mockMvc.perform(post("/parkinglot")
                        .header("Authorization", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void createParkingLot_nameIsBlank() throws Exception {
        // GIVEN
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

        // WHEN + THEN
        mockMvc.perform(post("/parkinglot")
                        .header("Authorization", "test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

}