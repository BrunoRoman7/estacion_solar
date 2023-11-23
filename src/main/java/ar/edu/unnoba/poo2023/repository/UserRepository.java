package ar.edu.unnoba.poo2023.repository;

import ar.edu.unnoba.poo2023.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findOneByUsername(String username);

    @Query("SELECT u FROM User u ")
    List<User> findByEmailContaining();

    @Query("SELECT u FROM User u WHERE u.id =:id")
    User findByIdContaining(int id);
}
