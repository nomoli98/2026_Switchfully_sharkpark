
package com.application.sharkpark.service;
import java.util.List;
import java.util.Optional;
import com.application.sharkpark.domain.City;
import com.application.sharkpark.domain.Division;
import com.application.sharkpark.domain.ParkingLot;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.ParkingLotRequest;
import com.application.sharkpark.dto.response.ParkingLotOverviewResponse;
import com.application.sharkpark.dto.response.ParkingLotResponse;
import com.application.sharkpark.enums.ParkingLotCategory;
import com.application.sharkpark.exceptions.DivisionNotFoundException;
import com.application.sharkpark.mapper.ParkingLotMapper;
import com.application.sharkpark.repository.CityRepository;
import com.application.sharkpark.repository.DivisionRepository;
import com.application.sharkpark.repository.ParkingLotRepository;
import com.application.sharkpark.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParkingLotServiceTest {

    private ParkingLotRepository parkingLotRepository;
    private UserRepository userRepository;
    private CityRepository cityRepository;
    private DivisionRepository divisionRepository;
    private ParkingLotMapper parkingLotMapper;

    private ParkingLotService service;

    @BeforeEach
    void setUp() {
        parkingLotRepository = mock(ParkingLotRepository.class);
        userRepository = mock(UserRepository.class);
        cityRepository = mock(CityRepository.class);
        divisionRepository = mock(DivisionRepository.class);
        parkingLotMapper = mock(ParkingLotMapper.class);

        service = new ParkingLotService(
                parkingLotRepository,
                parkingLotMapper,
                userRepository,
                cityRepository,
                divisionRepository
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
                2,
                3
        );

        User user = new User();
        City city = new City();
        Division division = mock(Division.class);

        ParkingLot parkingLot = new ParkingLot();
        ParkingLot savedParkingLot = new ParkingLot();

        ParkingLotResponse expectedResponse = mock(ParkingLotResponse.class);

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(cityRepository.findById(2))
                .thenReturn(Optional.of(city));

        when(divisionRepository.findById(3))
                .thenReturn(Optional.of(division));

        when(parkingLotMapper.mapToParkingLot(
                request, user, city, division))
                .thenReturn(parkingLot);

        when(parkingLotRepository.save(parkingLot))
                .thenReturn(savedParkingLot);

        when(parkingLotMapper.mapToParkingLotResponse(savedParkingLot))
                .thenReturn(expectedResponse);

        ParkingLotResponse result = service.createParkingLot(request);

        assertSame(expectedResponse, result);

        verify(divisionRepository).findById(3);
        verify(parkingLotRepository).save(parkingLot);
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
                2,
                3
        );

        when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.createParkingLot(request)
        );

        assertEquals("User not found", exception.getMessage());

        verifyNoInteractions(divisionRepository);
    }

    @Test
    void createParkingLot_divisionDoesNotExist() {

        ParkingLotRequest request = new ParkingLotRequest(
                "Shark Parking",
                ParkingLotCategory.UNDERGROUND_BUILDING,
                100,
                "Shark Street",
                25,
                2.50,
                1,
                2,
                3
        );

        User user = new User();
        City city = new City();

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(cityRepository.findById(2))
                .thenReturn(Optional.of(city));

        when(divisionRepository.findById(3))
                .thenReturn(Optional.empty());


        assertThrows(
                DivisionNotFoundException.class,
                () -> service.createParkingLot(request)
        );

        verify(parkingLotRepository, never()).save(any());
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


}
