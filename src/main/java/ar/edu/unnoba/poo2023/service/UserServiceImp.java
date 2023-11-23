package ar.edu.unnoba.poo2023.service;

import ar.edu.unnoba.poo2023.model.User;
import ar.edu.unnoba.poo2023.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService{

    private final UserRepository userRepository;
    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*User user1 = new User(1,"admin","admin","admin@email.com","admin");
        create(user1);*/
        User user =getUserByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("Bad credentials");
        }else{
            return user;
        }


    }

    @Override
    public void create(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        this.userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return this.userRepository.findByEmailContaining();
    }



    public User getUserByTaxID(int id) {
        return this.userRepository.findByIdContaining(id);

    }
    public User getUserByUsername(String username) {
        return this.userRepository.findOneByUsername(username);

    }
}
