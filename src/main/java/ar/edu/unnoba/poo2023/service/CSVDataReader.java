package ar.edu.unnoba.poo2023.service;

import ar.edu.unnoba.poo2023.model.DatosSensor;
import ar.edu.unnoba.poo2023.model.Irradiacion;
import ar.edu.unnoba.poo2023.model.Viento;
import ar.edu.unnoba.poo2023.repository.DatosSensorRepository;
import ar.edu.unnoba.poo2023.repository.IrradiacionRepository;
import ar.edu.unnoba.poo2023.repository.VientoRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.Reader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CompletableFuture;


@Service
public class CSVDataReader {

    private IrradiacionRepository irradiacionRepository;
    private DatosSensorRepository datosSensorRepository;
    private VientoRepository vientoRepository;

    @Autowired
    public CSVDataReader(IrradiacionRepository irradiacionRepository, DatosSensorRepository datosSensorRepository, VientoRepository vientoRepository) {
        this.irradiacionRepository = irradiacionRepository;
        this.datosSensorRepository = datosSensorRepository;
        this.vientoRepository = vientoRepository;
    }

    public void procesocsv() {
        String csvFile = "/home/facu/Escritorio/repos/estacion_solar/src/main/java/ar/edu/unnoba/poo2023/csv/MAYO2023.csv";

        try (Reader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord record : csvParser) {
                CompletableFuture.runAsync(() -> {
                    Timestamp timestamp = parseToTimestamp(record.get("Date"), record.get("Time"));
                    Calendar calendar = Calendar.getInstance();
                    long tiempoEnMilisegundos = timestamp.getTime();
                    calendar.setTimeInMillis(tiempoEnMilisegundos);

                    Double radiacion = Double.parseDouble(record.get("Radiación solar OK W/m2"));
                    Double direccionViento = Double.parseDouble(record.get("Dirección viento °"));
                    Double velocidad = Double.parseDouble(record.get("Velocidad M/s"));

                    int año = calendar.get(Calendar.YEAR);
                    int mes = calendar.get(Calendar.MONTH) + 1; // Meses comienzan desde 0
                    int dia = calendar.get(Calendar.DAY_OF_MONTH);

                    System.out.println("Fecha: " + timestamp + ", año: " + año + ", mes: " + mes + ", dia: " + dia);

                    // Crear las entidades y guardarlas en los repositorios correspondientes
                    DatosSensor datosSensor = new DatosSensor(timestamp, año, mes, dia);
                    datosSensorRepository.save(datosSensor);

                    Irradiacion irradiacion = new Irradiacion(datosSensor, radiacion);
                    irradiacionRepository.save(irradiacion);

                    Viento viento = new Viento(datosSensor, direccionViento, velocidad);
                    vientoRepository.save(viento);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Timestamp parseToTimestamp(String dateStr, String timeStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            Date parsedDate = dateFormat.parse(dateStr + " " + timeStr);
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Manejo de errores, devuelve null en caso de fallo
        }
    }
}
