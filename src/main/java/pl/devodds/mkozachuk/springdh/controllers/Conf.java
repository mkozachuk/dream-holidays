package pl.devodds.mkozachuk.springdh.controllers;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "conf")
@Data
public class Conf {
    private String weatherKey = "z";
    private String cx = "";
    private String googleKey = "";
}
