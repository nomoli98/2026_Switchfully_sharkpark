package com.application.sharkpark.parkingLot.repository;

import com.application.sharkpark.parkingLot.domain.ParkingLot;
import com.application.sharkpark.parkingLot.dto.request.ParkingLotRequest;
import com.application.sharkpark.parkingLot.dto.response.ParkingLotResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ParkingLotRepository extends JpaRepository<ParkingLot,Integer> {

    ParkingLot save(ParkingLot parkingLot);



}
