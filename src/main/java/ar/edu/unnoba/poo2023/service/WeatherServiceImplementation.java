package ar.edu.unnoba.poo2023.service;

import ar.edu.unnoba.poo2023.model.Irradiacion;
import ar.edu.unnoba.poo2023.repository.DatosSensorRepository;
import ar.edu.unnoba.poo2023.repository.IrradiacionRepository;
import ar.edu.unnoba.poo2023.repository.VientoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class WeatherServiceImplementation implements WeatherService {

    private final IrradiacionRepository irradiacionRepository;
    private final DatosSensorRepository datosSensorRepository;
    private final VientoRepository vientoRepository;
    private  PeriodicHttpRequest periodicHttpRequest;


    @Autowired
    public WeatherServiceImplementation(IrradiacionRepository irradiacionRepository, DatosSensorRepository datosSensorRepository,VientoRepository vientoRepository,PeriodicHttpRequest periodicHttpRequest) {
        this.irradiacionRepository = irradiacionRepository;
        this.datosSensorRepository = datosSensorRepository;
        this.vientoRepository=vientoRepository;
        this.periodicHttpRequest=periodicHttpRequest;


    }
    @PostConstruct
    public void init() {
        this.periodicHttpRequest.consulta(this.irradiacionRepository, this.datosSensorRepository, this.vientoRepository);
    }

    public List<Irradiacion> getRadiacion() {
        //return this.irradiacionRepository.obtenerIrradiacionesConSensoresPorTiempo(new Timestamp(new Date(2023, 5, 30, 0, 0, 0).getTime()), new Timestamp(new Date(2023, 7, 30, 0, 0, 0).getTime()) );
        List<Irradiacion> data = this.irradiacionRepository.findAll();


        return data;
    }

    public List<Irradiacion> getRadiacionPorFechas(Timestamp desde, Timestamp hasta) {
        return this.irradiacionRepository.obtenerIrradiacionPorFechas(desde, hasta);
    }

    public List<Irradiacion> getRadiacionPorFecha(int dia, int mes, int año) {
        return this.irradiacionRepository.obtenerIrradiacionPorFecha(dia, año, mes);
    }
    public List<Irradiacion> getRadiacionPorMes(int mes, int año) {
        return this.irradiacionRepository.obtenerIrradiacionPorMesYAnio(año,mes);
    }

    @Override
    public List<Irradiacion> getRadiacionMensual(Timestamp mes) {
        return this.irradiacionRepository.obtenerIrradiacionPorMes(mes);
    }




}

