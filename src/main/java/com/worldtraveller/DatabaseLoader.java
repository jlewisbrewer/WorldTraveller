package com.worldtraveller;

import java.util.Arrays;
import java.util.List;

import com.worldtraveller.model.*;
import com.worldtraveller.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner{
    private final CountryRepository countryRepository;

    @Autowired
    public DatabaseLoader(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        Country country = new Country("Russia", 999192);
        
		Coordinate coordinateOne = new Coordinate(0, 0, country);
        Coordinate coordinateTwo = new Coordinate(0, 1, country); 
    
		List<Coordinate> coordinates = Arrays.asList(coordinateOne, coordinateTwo);

        country.setCoordinates(coordinates);

        this.countryRepository.save(country);
    }

}