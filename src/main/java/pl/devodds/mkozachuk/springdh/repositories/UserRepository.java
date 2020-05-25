package pl.devodds.mkozachuk.springdh.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.devodds.mkozachuk.springdh.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

}
