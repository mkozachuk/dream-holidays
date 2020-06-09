package pl.devodds.mkozachuk.springdh.controllers;

import org.springframework.stereotype.Controller;
import pl.devodds.mkozachuk.springdh.models.City;
import pl.devodds.mkozachuk.springdh.repositories.CityRepository;

import java.util.List;

@Controller
public class CityController {
    private final CityRepository cityRepository;

    public CityController(CityRepository cityRepository){
        this.cityRepository = cityRepository;
    }

    public List<City> getAll(){
        return (List<City>) cityRepository.findAll();
    }
}
