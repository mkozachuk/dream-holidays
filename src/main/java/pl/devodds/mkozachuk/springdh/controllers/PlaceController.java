package pl.devodds.mkozachuk.springdh.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.devodds.mkozachuk.springdh.models.*;
import pl.devodds.mkozachuk.springdh.repositories.CityRepository;
import pl.devodds.mkozachuk.springdh.repositories.HolidayRepository;
import pl.devodds.mkozachuk.springdh.repositories.PlaceRepository;
import pl.devodds.mkozachuk.springdh.repositories.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/place")
@Controller
@Slf4j
@SessionAttributes("holiday")
public class PlaceController {

    private CountryController countryController;
    private CityController cityController;
    private PlaceRepository placeRepository;
    private UserRepository userRepository;

    public PlaceController(CountryController countryController, CityController cityController, PlaceRepository placeRepository, UserRepository userRepository) {
        this.countryController = countryController;
        this.cityController = cityController;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    @ModelAttribute(name = "holiday")
    public Holiday holiday() {
        return new Holiday();
    }

    @ModelAttribute(name = "design")
    public Place design() {
        return new Place();
    }

    @GetMapping("/new-place")
    public String showDesignForm(Model model, Principal principal) {
        model.addAttribute("design", design());
        List<Country> allCounties = new ArrayList<>(countryController.getAllCounties());
        model.addAttribute("countyList", allCounties);
        List<City> allCities = new ArrayList<>(cityController.getAll());
        model.addAttribute("cityList", allCities);

        String username = principal.getName();
        User user = userRepository.findByUsername(username);
        model.addAttribute("user", user);

        return "design";
    }

    @PostMapping("/new-place")
    public String processDesign(@ModelAttribute("design") Place design, Errors errors, @ModelAttribute Holiday holiday, @AuthenticationPrincipal User user) {

        design.setUser(user);
        Place saved = placeRepository.save(design);
        log.info("Place has been saved: " + design);
        holiday.addDesign(saved);


        if (design.isDreamed()) {
            return "redirect:/holidays/dreamed";
        } else {
            return "redirect:/holidays/planed";
        }
    }


}
