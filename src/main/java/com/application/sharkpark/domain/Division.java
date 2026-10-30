package com.application.sharkpark.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "DIVISIONS")
public class Division {
    @Id
    @Column (name = "DIVISION_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "divisions_division_id_seq")
    @SequenceGenerator(name = "divisions_division_id_seq", sequenceName = "divisions_division_id_seq", allocationSize = 1)
    private int divisionId;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "ORIGINAL_NAME",  nullable = false, length = 100)
    private String originalName;

    @ManyToOne
    @JoinColumn(name = "FK_DIRECTOR_ID", referencedColumnName = "user_id")
    private User director;

    @OneToMany
    @JoinColumn(name = "FK_SUBDIVISION_ID")
    private List<Division> subdivisions;

    public Division(String name, String originalName, User director) {
        this.name = name;
        this.originalName = originalName;
        this.director = director;
        this.subdivisions = new ArrayList<>();
    }

    protected Division() {

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

    public User getDirector() {
        return director;
    }

    public List<Division> getSubdivisions() {
        return subdivisions;
    }

    public void addSubdivision(Division subdivision) {
        subdivisions.add(subdivision);
    }
}
