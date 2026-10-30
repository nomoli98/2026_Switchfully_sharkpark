package com.application.sharkpark.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "cities")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_id", nullable = false)
    private Integer cityId;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "postal_code", nullable = false)
    private Integer postalCode;

    public City() {
    }

    public Integer getId() {
        return cityId;
    }

    public void setId(Integer id) {
        this.cityId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

}