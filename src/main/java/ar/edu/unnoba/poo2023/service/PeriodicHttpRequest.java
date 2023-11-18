package ar.edu.unnoba.poo.service;
//consulta al link
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

///procesar contenido
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class PeriodicHttpRequest {
    public static void consulta() {
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

                HtmlParser(response.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.MINUTES); // Ejecutar depedninto de period


        executor.schedule(() -> {
            executor.shutdown();
            System.out.println("Tarea periódica detenida");
        }, 30, TimeUnit.MINUTES);
    }
    public static void HtmlParser(String html) {

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
                    String radiacion = valores[2];
                    String direccionViento = valores[3];
                    String velocidadViento = valores[4];

                    // Imprimir los valores obtenidos
                    System.out.println("Fecha: " + fecha);
                    System.out.println("Hora: " + hora);
                    System.out.println("Radiación: " + radiacion);
                    System.out.println("Dirección del viento: " + direccionViento);
                    System.out.println("Velocidad del viento: " + velocidadViento);
                } else {
                    System.out.println("No se pudieron obtener todos los valores necesarios.");
                }
            } else {
                System.out.println("No se encontró la etiqueta <PRE> en el HTML.");
            }

    }
    public static void main(String[] args) {
        PeriodicHttpRequest objetoconsulta=new PeriodicHttpRequest();
        objetoconsulta.consulta();
    }
}


