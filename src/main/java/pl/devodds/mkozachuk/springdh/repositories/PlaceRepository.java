package pl.devodds.mkozachuk.springdh.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.devodds.mkozachuk.springdh.models.Place;

public interface PlaceRepository extends CrudRepository<Place, Long> {
    Place getPlaceByCity(String city);
}
