package com.application.sharkpark.domain;

import jakarta.persistence.*;

import java.time.Clock;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_allocations")
public class ParkingAllocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer parkingAllocationId;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private User member;

    @ManyToOne
    @JoinColumn(name = "fk_parking_lot_id")
    private ParkingLot parkingLot;

    protected ParkingAllocation() {
    }

    public ParkingAllocation(ParkingLot parkingLot, User member, String licensePlate) {
        this.parkingLot = parkingLot;
        this.member = member;
        this.licensePlate = licensePlate;
        this.startTime = LocalDateTime.now(Clock.systemDefaultZone());
    }

    public Integer getParkingAllocationId() {
        return parkingAllocationId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public User getMember() {
        return member;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }
}
