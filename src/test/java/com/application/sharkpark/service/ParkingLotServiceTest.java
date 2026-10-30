package com.application.sharkpark.service;
import java.util.List;
import java.util.Optional;
import com.application.sharkpark.domain.City;
import com.application.sharkpark.domain.ParkingLot;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.ParkingLotRequest;
import com.application.sharkpark.dto.response.ParkingLotOverviewResponse;
import com.application.sharkpark.dto.response.ParkingLotResponse;
import com.application.sharkpark.enums.ParkingLotCategory;
import com.application.sharkpark.mapper.ParkingLotMapper;
import com.application.sharkpark.repository.CityRepository;
import com.application.sharkpark.repository.ParkingLotRepository;
import com.application.sharkpark.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


class ParkingLotServiceTest {

    private ParkingLotRepository parkingLotRepository;
    private UserRepository userRepository;
    private CityRepository cityRepository;
    private ParkingLotMapper parkingLotMapper;

    private ParkingLotService service;

    @BeforeEach
    void setUp() {
        parkingLotRepository = mock(ParkingLotRepository.class);
        userRepository = mock(UserRepository.class);
        cityRepository = mock(CityRepository.class);
        parkingLotMapper = mock(ParkingLotMapper.class);

        service = new ParkingLotService(
                parkingLotRepository,
                parkingLotMapper,
                userRepository,
                cityRepository
        );
    }

    @Test
    void createParkingLot() {

        ParkingLotRequest request = new ParkingLotRequest(
                "Shark Parking",
                ParkingLotCategory.UNDERGROUND_BUILDING,
                100,
                "Shark Street",
                25,
                2.50,
                1,
                2
        );

        User user = new User();
        City city = new City();

        ParkingLot parkingLot = new ParkingLot();
        ParkingLot savedParkingLot = new ParkingLot();

        ParkingLotResponse expectedResponse = mock(ParkingLotResponse.class);

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(cityRepository.findById(2))
                .thenReturn(Optional.of(city));

        when(parkingLotMapper.mapToParkingLot(request, user, city))
                .thenReturn(parkingLot);

        when(parkingLotRepository.save(parkingLot))
                .thenReturn(savedParkingLot);

        when(parkingLotMapper.mapToParkingLotResponse(savedParkingLot))
                .thenReturn(expectedResponse);

        ParkingLotResponse result = service.createParkingLot(request);

        assertSame(expectedResponse, result);
    }

    @Test
    void createParkingLot_userDoesNotExist() {
        ParkingLotRequest request = new ParkingLotRequest(
                "Shark Parking",
                ParkingLotCategory.UNDERGROUND_BUILDING,
                100,
                "Shark Street",
                25,
                2.50,
                1,
                2
        );

        when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.createParkingLot(request)
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getAllParkingLots() {
        ParkingLot parkingLot1 = new ParkingLot();
        ParkingLot parkingLot2 = new ParkingLot();

        ParkingLotOverviewResponse response1 =
                new ParkingLotOverviewResponse(1, "Parking 1", 100, "one@test.be", "0470111111");

        ParkingLotOverviewResponse response2 =
                new ParkingLotOverviewResponse(2, "Parking 2", 200, "two@test.be", "0470222222");

        when(parkingLotRepository.findAll())
                .thenReturn(List.of(parkingLot1, parkingLot2));

        when(parkingLotMapper.mapToParkingLotOverviewResponse(parkingLot1))
                .thenReturn(response1);

        when(parkingLotMapper.mapToParkingLotOverviewResponse(parkingLot2))
                .thenReturn(response2);

        List<ParkingLotOverviewResponse> result =
                service.getAllParkingLots();

        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));
    }

    @Test
    void getAllParkingLots_noParkingLots() {
        when(parkingLotRepository.findAll())
                .thenReturn(List.of());

        List<ParkingLotOverviewResponse> result =
                service.getAllParkingLots();

        assertTrue(result.isEmpty());
    }

    @Test
    void getSingleParkingLotWithSpecificId() {

        ParkingLot parkingLot = new ParkingLot();
        ParkingLotResponse expectedResponse = mock(ParkingLotResponse.class);

        when(parkingLotRepository.findById(1))
                .thenReturn(Optional.of(parkingLot));

        when(parkingLotMapper.mapToParkingLotResponse(parkingLot))
                .thenReturn(expectedResponse);

        ParkingLotResponse result = service.getParkingLot(1);

        assertSame(expectedResponse, result);
    }

    @Test
    void getParkingLot_parkingLotDoesNotExist() {
        when(parkingLotRepository.findById(1))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.getParkingLot(1)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }





}