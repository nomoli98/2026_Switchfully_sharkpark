package com.application.sharkpark.mapper;

import com.application.sharkpark.domain.City;
import com.application.sharkpark.dto.response.CityResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CityMapperTest {

    @Test
    void toResponse() {
        City city = new City();
        city.setName("Brussel");
        city.setPostalCode(1000);

        CityResponse result = CityMapper.toResponse(city);

        assertEquals("Brussel", result.getName());
        assertEquals(1000, result.getPostalCode());
    }
}