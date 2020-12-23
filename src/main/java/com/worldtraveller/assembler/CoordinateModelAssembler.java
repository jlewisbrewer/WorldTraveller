package com.worldtraveller.assembler;

import com.worldtraveller.controller.CoordinateController;
import com.worldtraveller.model.Coordinate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CoordinateModelAssembler implements RepresentationModelAssembler<Coordinate, EntityModel<Coordinate>> {
    @Override
    public EntityModel<Coordinate> toModel(Coordinate coordinate) {
        return EntityModel.of(coordinate, linkTo(methodOn(CoordinateController.class).getCoordinate(coordinate.getId())).withSelfRel(), linkTo(methodOn(CoordinateController.class).getCoordinates()).withRel("coordinate"));
    }
}