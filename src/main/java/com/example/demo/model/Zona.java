package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "zonas")
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int estadioId;
    private String nombre; // Ej: Popular, Oriente, VIP, Categoria 1
    private double precio;
    private int aforoMaximo;
    private int aforoActual;
    
    /**
     * Formación del escenario para esta zona.
     * ESTADIO = gradas rectangulares con curvatura (fútbol)
     * CIRCULAR = pista en el centro, asientos en círculo (BTS)
     * ARENA = escenario en frente, semicírculo (Bad Bunny)
     */
    private String formacion; // ESTADIO, CIRCULAR, ARENA

    public Zona() {
    }

    public Zona(int id, int estadioId, String nombre, double precio, int aforoMaximo, int aforoActual) {
        this.id = id;
        this.estadioId = estadioId;
        this.nombre = nombre;
        this.precio = precio;
        this.aforoMaximo = aforoMaximo;
        this.aforoActual = aforoActual;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstadioId() {
        return estadioId;
    }

    public void setEstadioId(int estadioId) {
        this.estadioId = estadioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getAforoMaximo() {
        return aforoMaximo;
    }

    public void setAforoMaximo(int aforoMaximo) {
        this.aforoMaximo = aforoMaximo;
    }

    public int getAforoActual() {
        return aforoActual;
    }

    public void setAforoActual(int aforoActual) {
        this.aforoActual = aforoActual;
    }

    public String getFormacion() {
        return formacion;
    }

    public void setFormacion(String formacion) {
        this.formacion = formacion;
    }
}