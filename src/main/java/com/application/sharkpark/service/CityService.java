package com.application.sharkpark.service;

import com.application.sharkpark.domain.City;
import com.application.sharkpark.repository.CityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CityService {
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City save(City newCity) {
        return cityRepository.save(newCity);
    }

    public List<City> findAll() {
        return cityRepository.findAll();
    }

    public City findById(Integer cityId) {
        return cityRepository.findById(cityId)
                             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found"));
    }

    public City findByPostalCode(Integer postalCode) {
        return cityRepository.findByPostalCode(postalCode)
                             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found"));
    }

    public boolean existsById(Integer cityId) {
        return cityRepository.existsById(cityId);
    }
}