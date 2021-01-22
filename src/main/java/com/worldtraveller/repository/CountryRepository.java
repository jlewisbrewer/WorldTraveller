package com.worldtraveller.repository;

import java.util.List;

import com.worldtraveller.model.Country;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
    Country findByName(String name);
} 