package ar.edu.unnoba.poo2023.service;

import ar.edu.unnoba.poo2023.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public User create(User user);
}
