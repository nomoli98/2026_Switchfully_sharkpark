package com.application.sharkpark.service;

import com.application.sharkpark.domain.City;
import com.application.sharkpark.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CityServiceTest {

    private CityRepository cityRepository;
    private CityService cityService;

    @BeforeEach
    void setUp() {
        cityRepository = mock(CityRepository.class);
        cityService = new CityService(cityRepository);
    }

    @Test
    void save() {
        City city = new City();

        when(cityRepository.save(city))
                .thenReturn(city);

        City result = cityService.save(city);

        assertSame(city, result);
    }

    @Test
    void findAll() {
        City city1 = new City();
        City city2 = new City();

        when(cityRepository.findAll())
                .thenReturn(List.of(city1, city2));

        List<City> result = cityService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void findById() {
        City city = new City();

        when(cityRepository.findById(1))
                .thenReturn(Optional.of(city));

        City result = cityService.findById(1);

        assertSame(city, result);
    }

    @Test
    void findById_cityDoesNotExist() {
        when(cityRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> cityService.findById(1)
        );
    }

    @Test
    void findByPostalCode() {
        City city = new City();

        when(cityRepository.findByPostalCode(8790))
                .thenReturn(Optional.of(city));

        City result = cityService.findByPostalCode(8790);

        assertSame(city, result);
    }

    @Test
    void findByPostalCode_cityDoesNotExist() {
        when(cityRepository.findByPostalCode(8790))
                .thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> cityService.findByPostalCode(8790)
        );
    }

    @Test
    void existsById() {
        when(cityRepository.existsById(1))
                .thenReturn(true);

        boolean result = cityService.existsById(1);

        assertTrue(result);
    }
}