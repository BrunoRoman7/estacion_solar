package ar.edu.unnoba.poo2023.repository;

import ar.edu.unnoba.poo2023.model.Client;
import ar.edu.unnoba.poo2023.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
}
