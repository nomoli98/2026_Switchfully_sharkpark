package com.application.sharkpark.repository.member;

import com.application.sharkpark.domain.member.City;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface CityRepository extends ListCrudRepository<City, Integer> {

    Optional<City> findByPostalCode(Integer postalCode);
}