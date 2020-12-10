package com.worldtraveller;

import com.worldtraveller.model.Country;
import com.worldtraveller.repository.CountryRepository;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CountryTests {
    
    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void saveCountry() {
        Country country = new Country("Russia", 99);
        countryRepository.save(country);
        countryRepository.findById(new Long(1)).map(newCountry ->{
            Assert.assertEquals("Russia", newCountry.getName());
            return true;
        });
    }

    
}
