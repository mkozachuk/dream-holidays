package pl.devodds.mkozachuk.springdh.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.devodds.mkozachuk.springdh.models.Country;

public interface CountryRepository extends CrudRepository<Country, Long> {
    Country getByCountryCode(String code);
}
