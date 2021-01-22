package com.worldtraveller.assembler;

import com.worldtraveller.controller.CountryController;
import com.worldtraveller.model.Country;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CountryModelAssembler implements RepresentationModelAssembler<Country, EntityModel<Country>> {
    @Override
    public EntityModel<Country> toModel(Country country) {
        return EntityModel.of(country, linkTo(methodOn(CountryController.class).getCountry(country.getName())).withSelfRel(), linkTo(methodOn(CountryController.class).getCountries()).withRel("country"));
    }
}
