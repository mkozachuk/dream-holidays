package pl.devodds.mkozachuk.springdh.controllers;

import org.springframework.stereotype.Controller;
import pl.devodds.mkozachuk.springdh.models.Country;
import pl.devodds.mkozachuk.springdh.repositories.CountryRepository;

import java.util.List;

@Controller
public class CountryController {

    private final CountryRepository countryRepository;

    public CountryController(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getAllCounties(){
        return (List<Country>) countryRepository.findAll();
    }

    public Country getByCode(String code){
        return countryRepository.getByCountryCode(code);
    }
}
