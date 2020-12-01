package com.worldtraveller.model;


import javax.persistence.*;

@Entity
@Table(name = "Coordinate")
public class Coordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int x;
    private int y;
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

}
