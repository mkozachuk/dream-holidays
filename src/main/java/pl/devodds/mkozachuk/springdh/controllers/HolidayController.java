package pl.devodds.mkozachuk.springdh.controllers;

import com.google.api.services.customsearch.v1.model.Result;
import lombok.extern.slf4j.Slf4j;
import net.aksingh.owmjapis.api.APIException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import pl.devodds.mkozachuk.springdh.models.Holiday;
import pl.devodds.mkozachuk.springdh.models.Transport;
import pl.devodds.mkozachuk.springdh.models.User;
import pl.devodds.mkozachuk.springdh.models.Weather;
import pl.devodds.mkozachuk.springdh.repositories.HolidayRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/holidays")
@SessionAttributes("holiday")
public class HolidayController {
    private final HolidayRepository holidayRepository;
    private final TransportController transportController;
    private final WeatherController weatherController;
    private final HolidayImageController holidayImageController;

    public HolidayController(HolidayRepository holidayRepository, TransportController transportController, WeatherController weatherController, HolidayImageController holidayImageController){
        this.holidayRepository = holidayRepository;
        this.transportController = transportController;
        this.weatherController = weatherController;
        this.holidayImageController = holidayImageController;
    }

    @GetMapping("/dreamed")
    public String dreamedForm(@AuthenticationPrincipal User user,
                              @ModelAttribute Holiday holiday) {
        return "dreamedForm";
    }

    @PostMapping("/dreamed")
    public String processDreamedHoliday(@Valid Holiday holiday, Errors errors, SessionStatus sessionStatus, @AuthenticationPrincipal User user, Model model) {
        if (errors.hasErrors()) {
            return "dreamedForm";
        }
        model.addAttribute(holiday);

        log.info("Holiday submitted: " + holiday);
        holiday.setUser(user);
//        holiday.setTransport(transportController.getByType(new Transport(holiday.getTransportId()).getType()));
        holidayRepository.save(holiday);
        sessionStatus.setComplete();

        return "redirect:/holidays/my-holidays";
    }

    @GetMapping("/planed")
    public String planedForm(@AuthenticationPrincipal User user,
                              @ModelAttribute Holiday holiday) {
        return "planedForm";
    }

    @PostMapping("/planed")
    public String processPlanedHoliday(@Valid Holiday holiday, Errors errors, SessionStatus sessionStatus, @AuthenticationPrincipal User user, Model model) {
        if (errors.hasErrors()) {
            return "planedForm";
        }
        model.addAttribute(holiday);

        log.info("Holiday submitted: " + holiday);
        holiday.setUser(user);
        holiday.setTransport(transportController.getByType(new Transport(holiday.getTransport().getType()).getType()));
        holidayRepository.save(holiday);
        sessionStatus.setComplete();

        return "redirect:/holidays/my-holidays";
    }

    @GetMapping("/my-holidays")
    public String holidaysForm(@AuthenticationPrincipal User user,
                              Model model) {

        user.setUsersHolidays(getUsersHolidays(user));



        for(Holiday holiday : user.getUsersHolidays()){
            try {
                weatherController.weather(weatherController.forecast("Warsaw"));
                holiday.setWeather(new Weather(weatherController.getWeatherFromCity(holiday.getPlace().getCity()), weatherController.forecast(holiday.getPlace().getCity())));
            }catch (APIException e){
                log.error("APIE");
            }
            holiday.setImgs(new ArrayList<>());

            try{
                List<Result> res = holidayImageController.search(holiday.getPlace().getCity() + " beautiful photo");
                for(Result result : res){
//                    System.out.println(result.getImage().getContextLink());
                    System.out.println(result.getPagemap().get("cse_image"));
                    holiday.getImgs().add(result.getPagemap().get("cse_image").toString().replace("[{src=","").replace("}]", ""));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        model.addAttribute(user);


        return "holidays";
    }

    public List<Holiday> getUsersHolidays(User user){
        return holidayRepository.getHolidaysByUser(user);
    }

}
