package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "asientos_evento")
public class AsientoEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int eventoId;
    private int zonaId;
    private String fila;
    private int columna;
    private String estado;  // DISPONIBLE, OCUPADO, RESERVADO
    private String documentoUsuario; // Quién compró este asiento

    public AsientoEvento() {
        this.estado = "DISPONIBLE";
    }

    public AsientoEvento(int eventoId, int zonaId, String fila, int columna) {
        this.eventoId = eventoId;
        this.zonaId = zonaId;
        this.fila = fila;
        this.columna = columna;
        this.estado = "DISPONIBLE";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEventoId() { return eventoId; }
    public void setEventoId(int eventoId) { this.eventoId = eventoId; }

    public int getZonaId() { return zonaId; }
    public void setZonaId(int zonaId) { this.zonaId = zonaId; }

    public String getFila() { return fila; }
    public void setFila(String fila) { this.fila = fila; }

    public int getColumna() { return columna; }
    public void setColumna(int columna) { this.columna = columna; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDocumentoUsuario() { return documentoUsuario; }
    public void setDocumentoUsuario(String documentoUsuario) { this.documentoUsuario = documentoUsuario; }
}
