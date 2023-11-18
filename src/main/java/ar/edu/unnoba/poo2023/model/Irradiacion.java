package ar.edu.unnoba.poo2023.model;
import jakarta.persistence.*;

@Entity
public class Irradiacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idIrradiacion;

    @ManyToOne
    @JoinColumn(name = "id_datos_sensor", referencedColumnName = "idDatosSensor")
    private DatosSensor datosSensor;

    @Column(nullable = false)
    private Double radiacion;


    public Irradiacion(DatosSensor datosSensor, Double radiacion) {
        this.datosSensor = datosSensor;
        this.radiacion = radiacion;
    }

    public Irradiacion() {

    }

    public long getIdIrradiacion() {
        return idIrradiacion;
    }

    public void setIdIrradiacion(long idIrradiacion) {
        this.idIrradiacion = idIrradiacion;
    }

    public DatosSensor getDatosSensor() {
        return datosSensor;
    }

    public void setDatosSensor(DatosSensor datosSensor) {
        this.datosSensor = datosSensor;
    }

    public Double getRadiacion() {
        return radiacion;
    }

    public void setRadiacion(Double radiacion) {
        this.radiacion = radiacion;
    }
}
