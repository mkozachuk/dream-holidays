package pl.devodds.mkozachuk.springdh.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.devodds.mkozachuk.springdh.models.City;

public interface CityRepository extends CrudRepository<City, Long> {
}
