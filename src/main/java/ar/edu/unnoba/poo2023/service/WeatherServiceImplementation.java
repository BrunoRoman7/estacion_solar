package ar.edu.unnoba.poo2023.service;

import ar.edu.unnoba.poo2023.model.Irradiacion;
import ar.edu.unnoba.poo2023.repository.IrradiacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class WeatherServiceImplementation implements WeatherService{

    private final IrradiacionRepository irradiacionRepository;
    @Autowired
    public WeatherServiceImplementation(IrradiacionRepository irradiacionRepository) {
        this.irradiacionRepository = irradiacionRepository;
    }
    public   List<Irradiacion> getRadiacion(){
        //return this.irradiacionRepository.obtenerIrradiacionesConSensoresPorTiempo(new Timestamp(new Date(2023, 5, 30, 0, 0, 0).getTime()), new Timestamp(new Date(2023, 7, 30, 0, 0, 0).getTime()) );
        List<Irradiacion> data=this.irradiacionRepository.findAll();


        return data;
    }
    public List<Irradiacion> getRadiacionPorFechas(Timestamp desde, Timestamp hasta){
        return this.irradiacionRepository.obtenerIrradiacionPorFechas(desde,hasta);
    }

    @Override
    public List<Irradiacion> getRadiacionMensual(Timestamp mes) {
        return this.irradiacionRepository.obtenerIrradiacionPorMes(mes);
    }


}
