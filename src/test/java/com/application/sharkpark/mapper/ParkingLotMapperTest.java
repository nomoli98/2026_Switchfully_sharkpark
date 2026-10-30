package com.application.sharkpark.mapper;

import com.application.sharkpark.domain.City;
import com.application.sharkpark.domain.ParkingLot;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.dto.request.ParkingLotRequest;
import com.application.sharkpark.dto.response.ParkingLotResponse;
import com.application.sharkpark.enums.ParkingLotCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkingLotMapperTest {

    @Test
    void mapToParkingLot_happyPath() {
        ParkingLotMapper mapper = new ParkingLotMapper();

        User user = new User();
        City city = new City();

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

        ParkingLot result =
                mapper.mapToParkingLot(request, user, city);

        assertEquals("Shark Parking", result.getName());
        assertEquals(100, result.getMaxCapacity());
        assertSame(user, result.getContactPerson());
        assertSame(city, result.getCity());
    }

    @Test
    void mapToParkingLotResponse() {
        ParkingLotResponse result = getResult();

        assertEquals(1, result.getParkingLotId());
        assertEquals("Shark Parking", result.getParkingLotName());
        assertEquals(
                ParkingLotCategory.UNDERGROUND_BUILDING,
                result.getParkingLotCategory()
        );
        assertEquals(100, result.getMaxCapacity());
        assertEquals("Shark Street", result.getAddressStreet());
        assertEquals(25, result.getAddressNumber());
        assertEquals(2.50, result.getPricePerHour());
    }

    private static ParkingLotResponse getResult() {
        User user = new User();
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setPhoneNumber("123");
        user.setMobileNumber("456");

        City city = new City();
        city.setName("Waregem");
        city.setPostalCode(8790);

        ParkingLot parkingLot = new ParkingLot(
                "Shark Parking",
                ParkingLotCategory.UNDERGROUND_BUILDING,
                100,
                "Shark Street",
                25,
                2.50,
                user,
                city
        );
        parkingLot.setId(1);

        ParkingLotMapper mapper = new ParkingLotMapper();

        ParkingLotResponse result =
                mapper.mapToParkingLotResponse(parkingLot);
        return result;
    }
}