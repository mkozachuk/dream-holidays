package pl.devodds.mkozachuk.springdh.controllers;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import net.aksingh.owmjapis.model.param.WeatherData;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WeatherController {


    private Conf conf;

    public WeatherController(Conf conf){
        this.conf = conf;
    }


    private String apiKey = "z";


    public String getWeatherFromCity(String city) throws APIException {
        apiKey = conf.getWeatherKey();
         OWM owm = new OWM(apiKey);
        owm.setUnit(OWM.Unit.METRIC);
        CurrentWeather cwd = owm.currentWeatherByCityName(city);
        if (cwd.hasRespCode() && cwd.getRespCode() == 200) {

            // checking if city name is available
            if (cwd.hasCityName()) {
                //printing city name from the retrieved data
                System.out.println("City: " + cwd.getCityName());
            }

            // checking if max. temp. and min. temp. is available
            if (cwd.hasMainData() && cwd.getMainData().hasTempMax() && cwd.getMainData().hasTempMin()) {
                // printing the max./min. temperature
                System.out.println("Temperature: " + cwd.getMainData().getTempMax()
                        + "/" + cwd.getMainData().getTempMin() + "\'C");

            }

        }
        return cwd.getMainData().getTemp() + "\'C";
    }

    public List<WeatherData> forecast(String city) throws APIException{
        apiKey = conf.getWeatherKey();
        OWM owm = new OWM(apiKey);
        owm.setUnit(OWM.Unit.METRIC);
        HourlyWeatherForecast hw = owm.hourlyWeatherForecastByCityName(city);
        return hw.getDataList();
    }

    public void weather(List<WeatherData> list){
        System.out.println(
        "list.get(0).getMainData() " + list.get(0).getMainData() + "\n" +

                "list.get(0).getDateTimeText() " +list.get(0).getDateTimeText() +"\n" +

                "list.get(0).getWeatherList().get(0).getConditionId() " +list.get(0).getWeatherList().get(0).getConditionId() +"\n" +
                "list.get(0).getWeatherList().get(0).getDescription() " +list.get(0).getWeatherList().get(0).getDescription() +"\n" +
                "list.get(0).getWeatherList().get(0).getIconCode() " +list.get(0).getWeatherList().get(0).getIconCode() +"\n"

        );
    }




}

