package com.application.sharkpark.domain;

import jakarta.persistence.*;

@Entity
@Table (name = "DIVISIONS")
public class Division {
    @Id
    @Column (name = "DIVISION_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "divisions_division_id_seq")
    @SequenceGenerator(name = "divisions_division_id_seq", sequenceName = "divisions_division_id_seq", allocationSize = 1)
    private int divisionId;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "ORIGINAL_NAME",  nullable = false, length = 50)
    private String originalName;

    @Column(name = "FK_DIRECTOR_ID")
    private Integer directorId;

    public Division(String name, String originalName, Integer directorId) {
        this.name = name;
        this.originalName = originalName;
        this.directorId = directorId;

    }

    public Division() {

    }

    public int getDivisionId() {
        return divisionId;
    }

    public String getName() {
        return name;
    }

    public String getOriginalName() {
        return originalName;
    }
    public Integer getDirectorId() {
        return directorId;
    }


}
