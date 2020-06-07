package pl.devodds.mkozachuk.springdh.models;

import lombok.Data;
import net.aksingh.owmjapis.model.param.Cloud;
import net.aksingh.owmjapis.model.param.Main;
import net.aksingh.owmjapis.model.param.Temp;

import java.util.Date;

@Data
public class OneDayWeather {
    Date date;
    Temp tempData;
    double humidity;
    Cloud cloudData;
    double temp;
    double maxTemp;
    double minTemp;


    @Override
    public String toString() {
        return "Weather for : "
                + date + "\n" +
                "tempData=" + tempData + "\n" +
                "humidity=" + humidity + "\n" +
                "cloudData=" + cloudData + "\n" +
                "temp=" + temp + "\n" +
                "maxTemp=" + maxTemp + "\n" +
                "minTemp=" + minTemp + "\n";
    }
}
