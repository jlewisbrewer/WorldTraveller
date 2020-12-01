package com.worldtraveller.repository;


import com.worldtraveller.model.Country;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {
    
} 