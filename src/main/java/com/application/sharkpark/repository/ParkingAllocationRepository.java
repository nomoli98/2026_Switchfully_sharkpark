package com.application.sharkpark.repository;

import com.application.sharkpark.domain.ParkingAllocation;
import com.application.sharkpark.domain.User;
import com.application.sharkpark.enums.ParkingAllocationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingAllocationRepository extends JpaRepository<ParkingAllocation, Integer>,
                                                     JpaSpecificationExecutor<ParkingAllocation> {

    List<ParkingAllocation> findAllByLicensePlateAndStatus(String licensePlate,
                                                           ParkingAllocationStatus status);

}
