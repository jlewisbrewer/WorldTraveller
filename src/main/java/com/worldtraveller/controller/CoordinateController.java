package com.worldtraveller.controller;

import com.worldtraveller.assembler.CoordinateModelAssembler;
import com.worldtraveller.assembler.CountryModelAssembler;
import com.worldtraveller.exception.CountryNotFoundException;
import com.worldtraveller.model.Coordinate;
import com.worldtraveller.model.Country;
import com.worldtraveller.repository.CoordinateRepository;
import com.worldtraveller.repository.CountryRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.css.Counter;

@RestController
public class CoordinateController {
    private final CoordinateRepository coordinateRepository;
    private final CountryRepository countryRepository;
    private final CoordinateModelAssembler coordinateModelAssembler;
    private final CountryModelAssembler countryModelAssembler;
    private int maxDistance = 100;
    private static Logger logger = LogManager.getLogger();

    public CoordinateController(CoordinateRepository coordinateRepository, CountryRepository countryRepository, CoordinateModelAssembler coordinateModelAssembler, CountryModelAssembler countryModelAssembler) {
        this.coordinateRepository = coordinateRepository;
        this.countryRepository = countryRepository;
        this.coordinateModelAssembler = coordinateModelAssembler;
        this.countryModelAssembler = countryModelAssembler;
    
    }

    @GetMapping("/coordinates")
    public CollectionModel<EntityModel<Coordinate>> getCoordinates() {
        List<EntityModel<Coordinate>> coordinates = StreamSupport.stream(coordinateRepository.findAll().spliterator(), false).map(coordinateModelAssembler::toModel)
        .collect(Collectors.toList());

        return CollectionModel.of(coordinates, linkTo(methodOn(CoordinateController.class).getCoordinates()).withSelfRel());
        
    }

    @GetMapping("/coordinates/{id}")
    public EntityModel<Coordinate> getCoordinate(@PathVariable Long id) {
        Coordinate coordinate = coordinateRepository.findById(id).orElseThrow(() -> new CountryNotFoundException(id));

        return coordinateModelAssembler.toModel(coordinate);
    }


    @GetMapping("/search")
    public Collection<Country> getNearestCountries(@RequestParam(required= true) String x, @RequestParam(required = true) String y) {
        logger.info("Params: " + x +  " and " + y);
        Coordinate coordinate = new Coordinate();
        coordinate.setX(Integer.parseInt(x));
        coordinate.setY(Integer.parseInt(y));

        Iterable<Country> countries = countryRepository.findAll();
        Iterator<Country> countriesIterator = countries.iterator();

        List<Country> validCountries = new ArrayList<Country>();
        // List<EntityModel<Country>> result = new ArrayList<EntityModel<Country>>();


        while(countriesIterator.hasNext()) {
            Country country = countriesIterator.next();
            List<Coordinate> countryCoordinates = country.getCoordinates();
            Iterator<Coordinate> countryCoordinatesIterator = countryCoordinates.iterator();
            logger.info("Country id: " + country.getId());

            while(countryCoordinatesIterator.hasNext()) {
                Coordinate countryCoordinate = countryCoordinatesIterator.next();
                logger.info("Country coordinate: " + countryCoordinate.getId());
                if (coordinate.getDistance(countryCoordinate) <= this.maxDistance) {
                    logger.info("Within the distance");
                    Country countryToAdd = countryRepository.findById(countryCoordinate.getCountry().getId()).orElseThrow(() ->new CountryNotFoundException(countryCoordinate.getCountry().getId()));
                    logger.info("Country to add id: " + countryToAdd.getId());

                    validCountries.add(countryToAdd);

                    break;
                }
            }
        }

        logger.info("Valid countries length: " + validCountries.size());


        return validCountries;
        
    }

}
