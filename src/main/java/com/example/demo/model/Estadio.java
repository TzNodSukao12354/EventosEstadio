package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estadios")
public class Estadio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String ciudad;
    private String pais;
    private int capacidad;
    private int horaAperturaPuertas; // cuantas horas antes del evento abren las puertas

    public Estadio() {
    }

    public Estadio(int id, String nombre, String ciudad, String pais, int capacidad, int horaAperturaPuertas) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
        this.capacidad = capacidad;
        this.horaAperturaPuertas = horaAperturaPuertas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getHoraAperturaPuertas() {
        return horaAperturaPuertas;
    }

    public void setHoraAperturaPuertas(int horaAperturaPuertas) {
        this.horaAperturaPuertas = horaAperturaPuertas;
    }
}