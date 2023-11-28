package ar.edu.unnoba.poo2023.repository;

import ar.edu.unnoba.poo2023.model.Irradiacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;


public interface IrradiacionRepository extends JpaRepository<Irradiacion,Long>  {


        @Query("SELECT i FROM Irradiacion i " +
                "JOIN i.datosSensor ds " +
                "WHERE ds.dia = :targetDay " +
                "AND ds.año = :targetYear " +
                "AND ds.mes = :targetMonth " +
                "ORDER BY ds.fecha") // Ordenar por fecha
        List<Irradiacion> obtenerIrradiacionPorFecha(
                @Param("targetDay") int targetDay,
                @Param("targetYear") int targetYear,
                @Param("targetMonth") int targetMonth);



        @Query("SELECT i FROM Irradiacion i WHERE i.datosSensor.fecha BETWEEN :desde AND :hasta")
        List<Irradiacion> obtenerIrradiacionPorFechas(Timestamp desde, Timestamp hasta);

        @Query("SELECT i FROM Irradiacion i WHERE MONTH(i.datosSensor.fecha) = :mes")
        List<Irradiacion> obtenerIrradiacionPorMes(Timestamp mes);

}
