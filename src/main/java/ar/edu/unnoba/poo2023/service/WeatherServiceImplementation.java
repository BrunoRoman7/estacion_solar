package ar.edu.unnoba.poo2023.service;

import ar.edu.unnoba.poo2023.model.Irradiacion;
import ar.edu.unnoba.poo2023.repository.DatosSensorRepository;
import ar.edu.unnoba.poo2023.repository.IrradiacionRepository;
import ar.edu.unnoba.poo2023.repository.VientoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
public class WeatherServiceImplementation implements WeatherService {

    private final IrradiacionRepository irradiacionRepository;
    private final DatosSensorRepository datosSensorRepository;
    private final VientoRepository vientoRepository;
    private  PeriodicHttpRequest periodicHttpRequest;
    private final JedisPool pool;
    ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    public WeatherServiceImplementation(IrradiacionRepository irradiacionRepository, DatosSensorRepository datosSensorRepository,VientoRepository vientoRepository,PeriodicHttpRequest periodicHttpRequest) {
        this.irradiacionRepository = irradiacionRepository;
        this.datosSensorRepository = datosSensorRepository;
        this.vientoRepository=vientoRepository;
        this.periodicHttpRequest=periodicHttpRequest;
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMinIdle(5);
        poolConfig.setMaxIdle(10);
        poolConfig.setMaxTotal(20);
        poolConfig.setMinEvictableIdleTimeMillis(30000);
        poolConfig.setTimeBetweenEvictionRunsMillis(30000);

        this.pool = new JedisPool(poolConfig, "localhost", 6379); // Ejemplo de timeouts de 1000ms




    }
    @PostConstruct
    public void init() {
        this.periodicHttpRequest.consulta(this.irradiacionRepository, this.datosSensorRepository, this.vientoRepository,this.pool);
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

        long startTime = System.currentTimeMillis();

            // Obtener los datos de la base de datos y guardar en Redis
            System.out.println("pidiendo datos en POSTGRES");
            List<Irradiacion> dataFromDB =this.irradiacionRepository.obtenerIrradiacionPorFecha(dia, año, mes);
            long endTime = System.currentTimeMillis();
            System.out.println("Tiempo de consulta en Postgres: " + (endTime - startTime) + "ms");



            return dataFromDB;

    }
    public List<Irradiacion> getRadiacionPorMes(int mes, int año) {
        long startTime = System.currentTimeMillis();

        if (existenDatosEnRedis(mes, año)) {
            // Obtener los datos de Redis
            System.out.println("pidiendo datos en REDIS");
            List<Irradiacion> dataFromRedis = obtenerDatosDesdeRedis(mes, año);

            long endTime = System.currentTimeMillis();
            System.out.println("Tiempo de consulta en Redis: " + (endTime - startTime) + "ms");

            return dataFromRedis;
        } else {
            // Obtener los datos de la base de datos y guardar en Redis
            System.out.println("pidiendo datos en POSTGRES");
            List<Irradiacion> dataFromDB = this.irradiacionRepository.obtenerIrradiacionPorMesYAnio(año, mes);
            long endTime = System.currentTimeMillis();
            System.out.println("Tiempo de consulta en Postgres : " + (endTime - startTime) + "ms");

            guardarDatosEnRedis(dataFromDB);


            return dataFromDB;
        }
    }


    public boolean existenDatosEnRedis(int mes, int año) {
        try (Jedis jedis = pool.getResource()) {
            // Construir la clave que coincida con el formato de fecha almacenado en Redis
            String clavePrefix = "irradiacion:" + año + "-" + String.format("%02d", mes);
            Set<String> keys = jedis.keys(clavePrefix + "*");

            // Verificar si se encontraron claves que coincidan con el mes y año dados
            return keys != null && !keys.isEmpty();
        } catch (Exception ex) {
            ex.printStackTrace();
            // Manejo de excepciones al obtener la conexión de Redis
        }
        return false;
    }
    private List<Irradiacion> obtenerDatosDesdeRedis(int mes, int año) {
        List<Irradiacion> datos = new ArrayList<>();
        Set<String> keys=null;
        try (Jedis jedis = pool.getResource()) {
            String clavePrefix = "irradiacion:" + año + "-" + String.format("%02d", mes); // Patrón para buscar las claves correspondientes al mes y año
            keys = jedis.keys(clavePrefix + "*");

            keys.parallelStream().forEach(key -> {
                try (Jedis jedis2 = pool.getResource()) {
                String irradiacionEnJSON = jedis2.get(key);
                try {
                    Irradiacion irradiacion = objectMapper.readValue(irradiacionEnJSON, Irradiacion.class);
                    synchronized (datos) {
                        datos.add(irradiacion);
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    // Manejo de errores de deserialización
                }
                } catch (JedisException e) {
                    e.printStackTrace();
                    // Manejo de excepciones de conexión con Redis
                }
            });
        } catch (JedisException e) {
            e.printStackTrace();
            // Manejo de excepciones de conexión con Redis
        }
        return datos;
    }
    private List<Irradiacion> obtenerDatosDesdeRedisdeldia(int dia, int mes, int año) {
        List<Irradiacion> datos = new ArrayList<>();
        try (Jedis jedis = pool.getResource()) {
            String clavePrefix = "irradiacion:" + año + "-" + String.format("%02d", mes) + "-" + String.format("%02d", dia);
            Set<String> keys = jedis.keys(clavePrefix + "*");

            for (String key : keys) {
                String irradiacionEnJSON = jedis.get(key);
                try {
                    Irradiacion irradiacion = objectMapper.readValue(irradiacionEnJSON, Irradiacion.class);
                    datos.add(irradiacion);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    // Manejo de errores de deserialización
                }
            }
        } catch (JedisException e) {
            e.printStackTrace();
            // Manejo de excepciones de conexión con Redis
        }
        return datos;
    }







    public void guardarDatosEnRedis(List<Irradiacion> data) {

        CompletableFuture.runAsync(() -> {
                data.parallelStream().forEach(irradiacion -> {
                    try (Jedis jedis = pool.getResource()) {
                    try {
                        String irradiacionEnJSON = objectMapper.writeValueAsString(irradiacion);
                        String clave = "irradiacion:" + irradiacion.getDatosSensor().getFecha().toString();
                        jedis.set(clave, irradiacionEnJSON);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        // Manejo de errores de serialización a JSON
                    }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // Manejo de excepciones al obtener la conexión de Redis
                    }
                });

        });





    }






    @Override
    public List<Irradiacion> getRadiacionMensual(Timestamp mes) {
        return this.irradiacionRepository.obtenerIrradiacionPorMes(mes);
    }





}

