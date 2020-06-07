package pl.devodds.mkozachuk.springdh.controllers;

import com.google.api.services.customsearch.v1.model.Result;
import lombok.extern.slf4j.Slf4j;
import net.aksingh.owmjapis.api.APIException;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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

    public HolidayController(HolidayRepository holidayRepository, TransportController transportController, WeatherController weatherController, HolidayImageController holidayImageController) {
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
        holiday.setCreatedAt(new Date());
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(new Date());
        try {
            Date date = format.parse(dateString);
            holiday.setCreatedAt(date);
        } catch (ParseException e) {
            log.error("Parse exception {}", e);
        }
        holidayRepository.save(holiday);
        sessionStatus.setComplete();


        return "redirect:/holidays/my-holidays";
    }

    @GetMapping("/my-holidays")
    public String holidaysForm(@AuthenticationPrincipal User user,
                               Model model) {

        user.setUsersHolidays(getUsersHolidays(user));

        user.setCurrentDate(new Date());

        for (Holiday holiday : user.getUsersHolidays()) {
            if (!holiday.getPlace().isDreamed()) {
                holiday.setTimePercent(calculateTimeProgress(holiday, user));
                holidayRepository.save(holiday);
            }else {
                holiday.setCurrentCapital(calculateCurrentCapital(holiday, user));
                holiday.setCapitalPercent(calculateCapitalProgress(holiday,user));
                holiday.setEnoughCapital(calculateWhenEnoughCapital(holiday, user));
                holidayRepository.save(holiday);
            }

            try {
                holiday.setWeather(new Weather(weatherController.getWeatherFromCity(holiday.getPlace().getCity()), weatherController.dailyForecastList(weatherController.forecast(holiday.getPlace().getCity()))));
            } catch (APIException e) {
                log.error("APIE");
            }
            holiday.setImgs(new ArrayList<>());
            holiday.setInterestingPlacesUrls(new ArrayList<>());

            try {
                List<Result> res = holidayImageController.search(holiday.getPlace().getCity() + " beautiful photo");
                for (Result result : res) {
//                    System.out.println(result.getImage().getContextLink());
                    System.out.println(result.getPagemap().get("cse_image"));
                    holiday.getImgs().add(result.getPagemap().get("cse_image").toString().replace("[{src=", "").replace("}]", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                List<Result> trip = holidayImageController.search("site:tripadvisor.com \"" + holiday.getPlace().getCity() + " attractions\"");
                for (Result res : trip) {
                    holiday.getInterestingPlacesUrls().add(res.getLink());
                    System.out.println("TRIP: " + res.getLink());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        model.addAttribute(user);


        return "holidays";
    }

    public List<Holiday> getUsersHolidays(User user) {
        return holidayRepository.getHolidaysByUser(user);
    }

    public int calculateTimeProgress(Holiday holiday, User user) {
        double result;
        double allTime = (holiday.getStartDate().getTime() - holiday.getCreatedAt().getTime()) / (24.0 * 60.0 * 60.0 * 1000);
        double timeLeft = (holiday.getStartDate().getTime() - user.getCurrentDate().getTime()) / (24.0 * 60.0 * 60.0 * 1000);

        result = 100.0 - ((timeLeft * 100.0) / allTime);

        log.error("RESULT {}", result);
        System.out.println((int) result);
        log.error("RESULT TO INT {}", (int) result);

        return (int) result;
    }

    public BigDecimal calculateCurrentCapital(Holiday holiday, User user) {
        BigDecimal currentCapital = new BigDecimal(0);
        BigDecimal daySave = holiday.getMonthlySave().divide(new BigDecimal(30), 2, RoundingMode.HALF_UP);
        double daysFromCreate = (user.getCurrentDate().getTime() - holiday.getCreatedAt().getTime()) / (24.0 * 60.0 * 60.0 * 1000);
        currentCapital = holiday.getStartCapital().add(daySave.multiply(new BigDecimal(daysFromCreate)));


        return currentCapital;
    }

    public int calculateCapitalProgress(Holiday holiday, User user) {

        int ressult = (calculateCurrentCapital(holiday, user).multiply(new BigDecimal(100)).divide(holiday.getPrice(), 2, RoundingMode.HALF_UP)).intValue();

        return ressult;
    }

    public String calculateWhenEnoughCapital(Holiday holiday, User user){
        BigDecimal capitalLeft = holiday.getPrice().subtract(calculateCurrentCapital(holiday, user));
        int daysForEnough = (capitalLeft.divide(holiday.getMonthlySave().divide(new BigDecimal(30), 2, RoundingMode.HALF_UP), 2, RoundingMode.HALF_UP)).intValue();

        LocalDateTime ldt = LocalDateTime.ofInstant(user.getCurrentDate().toInstant(), ZoneId.systemDefault());

        LocalDateTime finishDate = LocalDateTime.from(ldt).plusDays(daysForEnough);
        String pattern = "yyyy-MM-dd";
        return finishDate.format(DateTimeFormatter.ofPattern(pattern));
    }

}
