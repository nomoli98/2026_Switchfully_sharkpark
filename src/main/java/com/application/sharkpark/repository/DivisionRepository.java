package com.application.sharkpark.repository;

import com.application.sharkpark.domain.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Integer> {
    boolean existsByName(String name);
}
