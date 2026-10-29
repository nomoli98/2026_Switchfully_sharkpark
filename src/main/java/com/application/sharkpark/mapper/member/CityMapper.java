package com.application.sharkpark.mapper.member;

import com.application.sharkpark.domain.member.City;
import com.application.sharkpark.dto.response.member.CityResponse;

public class CityMapper {
    public static CityResponse toResponse(City city){
        return new CityResponse(city.getName(), city.getPostalCode());
    }
}
