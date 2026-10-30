package com.application.sharkpark.repository;

import com.application.sharkpark.domain.ParkingAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingAllocationRepository extends JpaRepository<ParkingAllocation, Integer> {
}
