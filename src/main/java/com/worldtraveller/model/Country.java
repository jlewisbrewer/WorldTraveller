package com.worldtraveller.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "Country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int landArea;
    // @JsonManagedReference
    // @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<Coordinate> coordinates = new ArrayList<>();

    public Country() {
    }

    public Country(String name, int landArea) {
        this.setName(name);
        this.setLandArea(landArea);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLandArea() {
        return landArea;
    }

    public void setLandArea(int landArea) {
        this.landArea = landArea;
    }

    // public List<Coordinate> getCoordinates() {
    //     return this.coordinates;
    // }

    // public void setCoordinates(List<Coordinate> coordinates) {
    //     this.coordinates = coordinates;
    // }

}
