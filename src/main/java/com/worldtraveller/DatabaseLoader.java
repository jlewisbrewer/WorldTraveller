package com.worldtraveller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.worldtraveller.model.*;
import com.worldtraveller.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseLoader implements CommandLineRunner{
    private final CountryRepository countryRepository;
    private static final String countryDataFile = "./src/main/java/com/worldtraveller/data/CountryData.txt";

    @Autowired
    public DatabaseLoader(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        try {
            File file = new File(countryDataFile);
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()) {

                String countryLine = reader.nextLine();
                String[] arr = countryLine.split("\t", 3);
                Country country = new Country(arr[0], Integer.parseInt(arr[1].replace(",", "")));
                this.countryRepository.save(country);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}