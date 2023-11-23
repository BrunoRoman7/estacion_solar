package ar.edu.unnoba.poo2023.service;

import ar.edu.unnoba.poo2023.model.Irradiacion;

import java.sql.Timestamp;
import java.util.List;

public interface WeatherService {
    public List<Irradiacion> getRadiacion();
    public List<Irradiacion> getRadiacionPorFechas(Timestamp desde, Timestamp hasta);





}
