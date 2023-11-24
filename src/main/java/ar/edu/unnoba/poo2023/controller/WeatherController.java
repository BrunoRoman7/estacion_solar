package ar.edu.unnoba.poo2023.controller;

import ar.edu.unnoba.poo2023.model.Irradiacion;
import ar.edu.unnoba.poo2023.service.WeatherServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/")
public class WeatherController {

    @Autowired
    private WeatherServiceImplementation weatherServiceImplementation;
    @Autowired
    private ResourceLoader resourceLoader;


    @GetMapping("/Grafico")
    public String Grafico(){

        return "users/grafico";
    }
    @GetMapping("/Radiacion")
    @ResponseBody
    public List<Irradiacion> datosClima() {
        List<Irradiacion> datos=weatherServiceImplementation.getRadiacion();

        System.out.println(datos);

        return datos;
    }

    @GetMapping("/sol.gif")
    @ResponseBody
    public ResponseEntity<Resource> getLoadingGif() throws IOException {


        // Lee el archivo en forma de Resource
        Resource resource = resourceLoader.getResource("classpath:/templates/static/images/sol.gif");

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_GIF)
                .body(resource);
    }
    @GetMapping("/radiacionporfechas")
    @ResponseBody
    public List<Irradiacion> datosClima(
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta
    ) {
        Timestamp fechaDesde;
        Timestamp fechaHasta;

        if (desde == null && hasta == null) {
            LocalDateTime ahora = LocalDateTime.now();
            LocalDateTime julio = ahora.minusMonths(5);
            // Retroceder un mes desde el momento actual
            LocalDateTime unMesAtras = julio.minusMonths(1);

            // Establecer desde hace un mes y hasta ahora
            fechaDesde = Timestamp.valueOf(unMesAtras);
            fechaHasta = Timestamp.valueOf(julio);
        } else {
try {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    // you can change format of date
    Date date = formatter.parse(desde);
    fechaDesde = new Timestamp(date.getTime());
    // you can change format of date
    date = formatter.parse(hasta);
    fechaHasta = new Timestamp(date.getTime());

}catch (ParseException e) {
            System.out.println("Exception :" + e);
            return null;
        }
        }

        return weatherServiceImplementation.getRadiacionPorFechas(fechaDesde, fechaHasta);
    }

    @GetMapping("/irradiacionMaxima")
    @ResponseBody
    public Double irradianciaMax(Timestamp mes){
        List<Irradiacion> list = weatherServiceImplementation.getRadiacionMensual(mes);
        Double cant= (double) 0;
        for(Irradiacion i: list){
            if (i.getRadiacion() > cant){
                cant = i.getRadiacion();
            }
        }
        return cant;
    }

    @GetMapping("/irradiacionPromedio")
    @ResponseBody
    public Double irradianciaPromedio(Timestamp mes){
        List<Irradiacion> list = weatherServiceImplementation.getRadiacionMensual(mes);
        int cant = list.size();
        Double suma= (double) 0;
        for(Irradiacion i: list){
            suma += i.getRadiacion();
        }
        return suma/cant;
    }

    @GetMapping("/cargabd")
    public String cargarbd() {
        //csvdataReader.procesocsv(); para activar la carga de
        // la base de datos hay que descompentar esto,tener cuidado
        return "redirect:/users";
    }
}
