package pl.devodds.mkozachuk.springdh.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.devodds.mkozachuk.springdh.models.Holiday;
import pl.devodds.mkozachuk.springdh.models.User;

import java.util.List;

public interface HolidayRepository extends CrudRepository<Holiday, Long> {
    List<Holiday> getHolidaysByUser(User user);
}
