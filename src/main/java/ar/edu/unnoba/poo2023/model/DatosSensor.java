package ar.edu.unnoba.poo2023.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
@Entity
public class DatosSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDatosSensor;


    @Column(nullable = false)
    private Timestamp fecha;
    @Column(nullable = false)
    private int año;
    @Column(nullable = false)
    private int mes;
    @Column(nullable = false)
    private int dia;

    public DatosSensor( Timestamp fecha, int año, int mes, int dia) {
        this.fecha = fecha;
        this.año = año;
        this.mes = mes;
        this.dia = dia;
    }

    public DatosSensor() {

    }


    public long getIdDatosSensor() {
        return idDatosSensor;
    }

    public void setIdDatosSensor(long idDatosSensor) {
        this.idDatosSensor = idDatosSensor;
    }

    public void setIdDatosSensor(int idDatosSensor) {
        this.idDatosSensor = idDatosSensor;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

}
