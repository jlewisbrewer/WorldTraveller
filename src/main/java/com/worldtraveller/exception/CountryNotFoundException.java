package com.worldtraveller.exception;

public class CountryNotFoundException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CountryNotFoundException(String name) {
        super("Could not find country " + name);
    }
}