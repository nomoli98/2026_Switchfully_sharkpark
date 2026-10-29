package com.application.sharkpark.repository;

import com.application.sharkpark.domain.member.City;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<City, Long> {
    City saveCity(City newCity);

    City findByCityId(Integer id);

    City findByName(String name);

    City findAllCity();

    boolean existsByCityId(Integer cityId);
}
