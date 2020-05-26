package pl.devodds.mkozachuk.springdh.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import pl.devodds.mkozachuk.springdh.models.Transport;
import pl.devodds.mkozachuk.springdh.models.TransportType;
import pl.devodds.mkozachuk.springdh.repositories.TransportRepository;

@Slf4j
@Controller
public class TransportController {
    private final TransportRepository transportRepository;

    public TransportController(TransportRepository transportRepository){
        this.transportRepository = transportRepository;
    }

    public Transport getByType(TransportType type){
        return transportRepository.getByType(type);
    }
}
