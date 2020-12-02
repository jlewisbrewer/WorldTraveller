package com.worldtraveller.exception;

public class CountryNotFoundException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CountryNotFoundException(Long id) {
        super("Could not find country " + id);
    }
}