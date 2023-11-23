package ar.edu.unnoba.poo2023.service;

import ar.edu.unnoba.poo2023.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    public void create (User user);
    //public void create(User user);
    public List<User> getUsers();
    public User getUserByTaxID(int id);
    public User getUserByUsername(String username);
}

