package com.worldtraveller.controller;

import com.worldtraveller.assembler.CoordinateModelAssembler;
import com.worldtraveller.assembler.CountryModelAssembler;
import com.worldtraveller.exception.CountryNotFoundException;
import com.worldtraveller.model.Coordinate;
import com.worldtraveller.repository.CoordinateRepository;
import com.worldtraveller.repository.CountryRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RestController;

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
    public CollectionModel<EntityModel<Coordinate>> getNearestCoordinates(@RequestParam(required= true) String x, @RequestParam(required = true) String y) {
        logger.info("Params: " + x +  " and " + y);
        Coordinate coordinateToFind = new Coordinate();
        coordinateToFind.setX(Integer.parseInt(x));
        coordinateToFind.setY(Integer.parseInt(y));

        List<EntityModel<Coordinate>> validCoordinates = new ArrayList<EntityModel<Coordinate>>();
        List<EntityModel<Coordinate>> coordinates = StreamSupport.stream(coordinateRepository.findAll().spliterator(), false).map(coordinateModelAssembler::toModel).collect(Collectors.toList());

        for (EntityModel<Coordinate> entityModel : coordinates) {
            Coordinate countryCoordinate = entityModel.getContent();
            if (coordinateToFind.getDistance(countryCoordinate) <= this.maxDistance) {
                validCoordinates.add(entityModel);
            }
        }

        logger.info("Valid coordinates length: " + validCoordinates.size());

        return CollectionModel.of(validCoordinates, linkTo(methodOn(CoordinateController.class).getCoordinates()).withSelfRel());

        // List<EntityModel<Country>> validCountries = new ArrayList<EntityModel<Country>>();
        // List<EntityModel<Country>> countries = StreamSupport.stream(countryRepository.findAll().spliterator(), false).map(countryModelAssembler::toModel)
        // .collect(Collectors.toList());


        // for (EntityModel<Country> entityModel : countries) {
        //     List<Coordinate> countryCoordinates = entityModel.getContent().getCoordinates();
        //     Iterator<Coordinate> countryCoordinatesIterator = countryCoordinates.iterator();

        //     while(countryCoordinatesIterator.hasNext()) {
        //         Coordinate countryCoordinate = countryCoordinatesIterator.next();
        //         if (coordinate.getDistance(countryCoordinate) <= this.maxDistance) {
        //             validCountries.add(entityModel);
        //             break;
        //         }
        //     }
        // }

        // logger.info("Valid countries length: " + validCountries.size());

        // return CollectionModel.of(validCountries, linkTo(methodOn(CountryController.class).getCountries()).withSelfRel());
        
    }

}
