package pl.devodds.mkozachuk.springdh.security;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.devodds.mkozachuk.springdh.models.User;

@Data
public class RegistrationForm {

    private String username;
    private String password;
    private String email;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(username, passwordEncoder.encode(password), email);
    }

}
