package com.application.sharkpark.repository;

import com.application.sharkpark.domain.City;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface CityRepository extends ListCrudRepository<City, Integer> {

    Optional<City> findByPostalCode(Integer postalCode);
}