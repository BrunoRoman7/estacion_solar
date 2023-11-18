package ar.edu.unnoba.poo2023.model;

import jakarta.persistence.*;

@Entity
public class Viento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idViento;

    @ManyToOne
    @JoinColumn(name = "id_datos_sensor", referencedColumnName = "idDatosSensor")
    private DatosSensor datosSensor;

    @Column(nullable = false)
    private Double direccion;

    @Column(nullable = false)

    private Double velocidad;


    public Viento(DatosSensor datosSensor, Double direccion, Double velocidad) {
        this.datosSensor = datosSensor;
        this.direccion = direccion;
        this.velocidad = velocidad;
    }

    public Viento() {

    }

    public long getIdViento() {
        return idViento;
    }


    public void setIdViento(Integer idViento) {
        this.idViento = idViento;
    }

    public DatosSensor getDatosSensor() {
        return datosSensor;
    }

    public void setDatosSensor(DatosSensor datosSensor) {
        this.datosSensor = datosSensor;
    }

    public Double getDireccion() {
        return direccion;
    }

    public void setDireccion(Double direccion) {
        this.direccion = direccion;
    }

    public Double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(Double velocidad) {
        this.velocidad = velocidad;
    }
}
