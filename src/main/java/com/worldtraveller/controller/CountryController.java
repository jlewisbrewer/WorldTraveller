package com.worldtraveller.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.worldtraveller.assembler.CountryModelAssembler;
import com.worldtraveller.exception.CountryNotFoundException;
import com.worldtraveller.model.Country;
import com.worldtraveller.repository.CountryRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {
    private final CountryRepository countryRepository;
    private final CountryModelAssembler countryModelAssembler;

    public CountryController(CountryRepository countryRepository, CountryModelAssembler countryModelAssembler) {
        this.countryRepository = countryRepository;
        this.countryModelAssembler = countryModelAssembler;
    }

    @GetMapping("/countries")
    public CollectionModel<EntityModel<Country>> getCountries() {

        List<EntityModel<Country>> countries = StreamSupport.stream(countryRepository.findAll().spliterator(), false).map(countryModelAssembler::toModel)
        .collect(Collectors.toList());

        return CollectionModel.of(countries, linkTo(methodOn(CountryController.class).getCountries()).withSelfRel());
    }

    @GetMapping("/countries/{id}")
    public EntityModel<Country> getCountry(@PathVariable Long id) {
        Country country = countryRepository.findById(id).orElseThrow(() -> new CountryNotFoundException(id));

        return countryModelAssembler.toModel(country); 
    }
}
