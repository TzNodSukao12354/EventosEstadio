package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "disponibilidad_zona")
public class DisponibilidadZona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int eventoId;
    private int zonaId;
    private int aforoOcupado; // Cuántas entradas se han vendido para este evento en esta zona

    public DisponibilidadZona() {
    }

    public DisponibilidadZona(int eventoId, int zonaId, int aforoOcupado) {
        this.eventoId = eventoId;
        this.zonaId = zonaId;
        this.aforoOcupado = aforoOcupado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEventoId() { return eventoId; }
    public void setEventoId(int eventoId) { this.eventoId = eventoId; }

    public int getZonaId() { return zonaId; }
    public void setZonaId(int zonaId) { this.zonaId = zonaId; }

    public int getAforoOcupado() { return aforoOcupado; }
    public void setAforoOcupado(int aforoOcupado) { this.aforoOcupado = aforoOcupado; }
}