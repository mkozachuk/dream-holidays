package pl.devodds.mkozachuk.springdh.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.devodds.mkozachuk.springdh.models.Transport;
import pl.devodds.mkozachuk.springdh.models.TransportType;

public interface TransportRepository extends CrudRepository<Transport, Long> {
    Transport getByType(TransportType type);
}
