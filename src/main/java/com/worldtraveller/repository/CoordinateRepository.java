package com.worldtraveller.repository;

import com.worldtraveller.model.Coordinate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinateRepository extends CrudRepository<Coordinate, Long> {
    
}
