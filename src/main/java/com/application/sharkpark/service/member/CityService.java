package com.application.sharkpark.service.member;

import com.application.sharkpark.domain.member.City;
import com.application.sharkpark.repository.member.CityRepository;
import org.springframework.stereotype.Service;

@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City save(City newCity) {
        return cityRepository.saveCity(newCity);
    }

    public City findById(Integer cityId) {
        return cityRepository.findByCityId(cityId)
                             .orElseThrow(() -> new RuntimeException("City not found"));
    }

    public City findByName(String cityName) {
        return cityRepository.findByName(cityName)
                             .orElseThrow(() -> new RuntimeException("City not found"));
    }

    public boolean existsById(Integer cityId) {
        return cityRepository.existsByCityId(cityId);
    }

}
