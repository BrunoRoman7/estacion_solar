package ar.edu.unnoba.poo2023.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ar.edu.unnoba.poo2023.model.DatosSensor;

public interface DatosSensorRepository extends JpaRepository<DatosSensor, Long> {
}
