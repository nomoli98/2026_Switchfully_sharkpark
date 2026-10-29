package com.application.sharkpark.parkingLot.domain;

import com.application.sharkpark.parkingLot.enums.ParkingLotCategory;
import jakarta.persistence.*;

@Entity
@Table(name = "parking_lots", schema = "sharkpark")
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parking_lot_id", nullable = false)
    private Integer parkingLotId;

    @Column(name = "name")
    private String name;

    @Column(name = "category", nullable = false, length = 25)
    private ParkingLotCategory category;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "address_street", length = 150)
    private String addressStreet;

    @Column(name = "address_number")
    private Integer addressNumber;

    @Column(name = "price_per_hour")
    private double pricePerHour;

    // add person and city
//    @ManyToOne
//    @JoinColumn(name = "fk_contact_person_id")
//    private User contactPerson;

    public ParkingLot(String name, ParkingLotCategory category, Integer maxCapacity, String addressStreet, Integer addressNumber, double pricePerHour) {
        this.name = name;
        this.category = category;
        this.maxCapacity = maxCapacity;
        this.addressStreet = addressStreet;
        this.addressNumber = addressNumber;
        this.pricePerHour = pricePerHour;
    }

    public ParkingLot() {

    }

    public Integer getId() {
        return parkingLotId;
    }

    public void setId(Integer id) {
        this.parkingLotId = id;
    }

    public ParkingLotCategory getCategory() {
        return category;
    }

    public void setCategory(ParkingLotCategory category) {
        this.category = category;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public Integer getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(Integer addressNumber) {
        this.addressNumber = addressNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
}