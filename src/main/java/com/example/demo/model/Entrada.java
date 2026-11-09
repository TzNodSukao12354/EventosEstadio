package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "entradas")
public class Entrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int eventoId;
    private int zonaId;
    private String nombreUsuario;
    private String documentoUsuario;
    private String estado;      // VIGENTE, USADA, PERDIDA, REIMPRESA, CANCELADA
    private String tipoEntrega; // FISICA, E-TICKET

    public Entrada() {
    }

    public Entrada(int id, int eventoId, int zonaId, String nombreUsuario, String documentoUsuario, String estado,
                   String tipoEntrega) {
        this.id = id;
        this.eventoId = eventoId;
        this.zonaId = zonaId;
        this.nombreUsuario = nombreUsuario;
        this.documentoUsuario = documentoUsuario;
        this.estado = estado;
        this.tipoEntrega = tipoEntrega;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventoId() {
        return eventoId;
    }

    public void setEventoId(int eventoId) {
        this.eventoId = eventoId;
    }

    public int getZonaId() {
        return zonaId;
    }

    public void setZonaId(int zonaId) {
        this.zonaId = zonaId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getDocumentoUsuario() {
        return documentoUsuario;
    }

    public void setDocumentoUsuario(String documentoUsuario) {
        this.documentoUsuario = documentoUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(String tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }
}