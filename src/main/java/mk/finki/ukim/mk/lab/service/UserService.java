package mk.finki.ukim.mk.lab.service;

import mk.finki.ukim.mk.lab.model.User;
import mk.finki.ukim.mk.lab.model.enumerations.Role;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDate;

public interface UserService extends UserDetailsService {
    User register(String username, String password, String repeatPassword, String name, String surname,  LocalDate date, Role role);
}
