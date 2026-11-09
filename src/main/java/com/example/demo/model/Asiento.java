package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "asientos")
public class Asiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int zonaId;
    private String fila;      // Ej: "A", "B", "C", ...
    private int columna;      // Ej: 1, 2, 3, ...
    private String estado;    // DISPONIBLE, OCUPADO, RESERVADO

    // Relación con evento específico
    @Transient
    private int eventoId;

    public Asiento() {
        this.estado = "DISPONIBLE";
    }

    public Asiento(int zonaId, String fila, int columna) {
        this.zonaId = zonaId;
        this.fila = fila;
        this.columna = columna;
        this.estado = "DISPONIBLE";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getZonaId() { return zonaId; }
    public void setZonaId(int zonaId) { this.zonaId = zonaId; }

    public String getFila() { return fila; }
    public void setFila(String fila) { this.fila = fila; }

    public int getColumna() { return columna; }
    public void setColumna(int columna) { this.columna = columna; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getEventoId() { return eventoId; }
    public void setEventoId(int eventoId) { this.eventoId = eventoId; }
}
