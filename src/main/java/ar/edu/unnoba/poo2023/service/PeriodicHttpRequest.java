package ar.edu.unnoba.poo2023.service;
//consulta al link

import ar.edu.unnoba.poo2023.model.DatosSensor;
import ar.edu.unnoba.poo2023.model.Irradiacion;
import ar.edu.unnoba.poo2023.model.Viento;
import ar.edu.unnoba.poo2023.repository.DatosSensorRepository;
import ar.edu.unnoba.poo2023.repository.IrradiacionRepository;
import ar.edu.unnoba.poo2023.repository.VientoRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PeriodicHttpRequest {


    public  void consulta(IrradiacionRepository irradiacionRepository, DatosSensorRepository datosSensorRepository, VientoRepository vientoRepository) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

        // Programar la tarea para ejecutarse cada 5 minutos
        executor.scheduleAtFixedRate(() -> {
            try {
                // URL a la que deseas enviar la solicitud
                String url = "http://192.168.79.7/momview.htm";

                // Crear objeto URL
                URL obj = new URL(url);

                // Abrir una conexión HttpURLConnection
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // Establecer el método de solicitud
                con.setRequestMethod("GET");

                // Obtener la respuesta
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();


                System.out.println("Respuesta de la solicitud HTTP:");

                HtmlParser(response.toString(), datosSensorRepository, irradiacionRepository, vientoRepository);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.MINUTES); // Ejecutar depedninto de period


        executor.schedule(() -> {
            executor.shutdown();
            System.out.println("Tarea periódica detenida");
        }, 30, TimeUnit.MINUTES);
    }
    private static void HtmlParser(String html, DatosSensorRepository datosSensorRepository, IrradiacionRepository irradiacionRepository, VientoRepository vientoRepository){

            Document doc = Jsoup.parse(html);

            // Obtener el contenido dentro de la etiqueta <PRE>
            Element preElement = doc.select("pre").first(); // Seleccionar la primera etiqueta <PRE>
            if (preElement != null) {
                String preContent = preElement.text();
                System.out.println("Contenido dentro de <PRE>:");
                System.out.println(preContent);
                String[] valores = preContent.split(",");

                if (valores.length >= 5) { // Asegurarse de tener al menos 5 valores
                    String fecha = valores[0];
                    String hora = valores[1];
                    String radiacionstring = valores[2];
                    String direccionVientostring = valores[3];
                    String velocidadVientostring = valores[4];


                    Timestamp timestamp = parseToTimestamp(fecha, hora);
                    Calendar calendar = Calendar.getInstance();
                    long tiempoEnMilisegundos = timestamp.getTime();
                    calendar.setTimeInMillis(tiempoEnMilisegundos);

                    Double radiacion = Double.parseDouble(radiacionstring);
                    Double direccionViento = Double.parseDouble(direccionVientostring);
                    Double velocidad = Double.parseDouble(velocidadVientostring);

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



                } else {
                    System.out.println("No se pudieron obtener todos los valores necesarios.");
                }
            } else {
                System.out.println("No se encontró la etiqueta <PRE> en el HTML.");
            }

    }
    private static Timestamp parseToTimestamp(String dateStr, String timeStr) {
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


