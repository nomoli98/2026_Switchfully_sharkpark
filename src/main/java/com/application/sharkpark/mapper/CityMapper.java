package com.application.sharkpark.mapper;

import com.application.sharkpark.domain.City;
import com.application.sharkpark.dto.response.CityResponse;

public class CityMapper {
    public static CityResponse toResponse(City city){
        return new CityResponse(city.getName(), city.getPostalCode());
    }
}
