package ar.edu.unnoba.poo2023.repository;

import ar.edu.unnoba.poo2023.model.Irradiacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;


public interface IrradiacionRepository extends JpaRepository<Irradiacion,Long>  {



        @Query("SELECT i FROM Irradiacion i WHERE i.datosSensor.fecha BETWEEN :desde AND :hasta")
        List<Irradiacion> obtenerIrradiacionPorFechas(Timestamp desde, Timestamp hasta);





}
