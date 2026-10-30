package com.application.sharkpark.repository;

import com.application.sharkpark.domain.ParkingLot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot,Integer> {

    ParkingLot save(ParkingLot parkingLot);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ParkingLot p WHERE p.parkingLotId = :id")
    Optional<ParkingLot> findByParkingLotIdForUpdate(@Param("id") Integer id);

}
