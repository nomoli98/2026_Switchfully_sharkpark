package com.application.sharkpark.repository;

import com.application.sharkpark.domain.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotRepository extends JpaRepository<ParkingLot,Integer> {

    ParkingLot save(ParkingLot parkingLot);



}
