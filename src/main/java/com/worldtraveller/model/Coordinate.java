package com.worldtraveller.model;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "Coordinate")
public class Coordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int x;
    private int y;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    public Coordinate() {
    }

    public Coordinate(int x, int y, Country country) {
        this.x = x;
        this.y = y;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

	public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Country getCountry() {
        return this.country;
    }

    public int getDistance(Coordinate coordinate) {
        int width = Math.abs(this.x - coordinate.x);
        int height = Math.abs(this.y - coordinate.y);

        return (int) Math.ceil(Math.sqrt((width * width) + (height * height)));
    }

}
