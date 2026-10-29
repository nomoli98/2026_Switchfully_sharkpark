package com.application.sharkpark.repository.member;

import com.application.sharkpark.domain.member.City;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends CrudRepository<City, Integer> {
    City saveCity(City newCity);

    Optional<City> findByCityId(Integer id);

    Optional<City> findByPostalCode(Integer postalCode);

    List<City> findAllCity();

    boolean existsByCityId(Integer cityId);
}
