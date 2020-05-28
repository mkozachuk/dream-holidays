package pl.devodds.mkozachuk.springdh.models;

import lombok.Data;
import net.aksingh.owmjapis.model.param.WeatherData;

import java.util.List;

@Data
public class Weather {
    public Weather(String currentWeather, List<WeatherData> forecast) {
        this.currentWeather = currentWeather;
        this.forecast = forecast;
    }

    public Weather() {
    }

    String currentWeather;
    List<WeatherData> forecast;
}
