package ar.edu.unnoba.poo2023.service;

import ar.edu.unnoba.poo2023.model.User;
import ar.edu.unnoba.poo2023.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImp implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(new BCryptPasswordEncoder().encode("12345"));
        return userRepository.findOneByUsername(username);
    }

    @Override
    public User create(User user) {
        return null;
    }
}
