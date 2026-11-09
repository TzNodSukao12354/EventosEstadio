package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombreEvento;
    private String tipoEvento; // NUEVO: fútbol, concierto, box, teatro
    private String equipoLocal; // Opcional (puede ser null si es concierto)
    private String equipoVisitante; // Opcional (puede ser null si es concierto)
    private String artistaPrincipal; // NUEVO: para conciertos (ej: Bad Bunny)

    private int estadioId;
    private String fecha; // formato: 2026-07-19
    private String hora;  // formato: 15:00:00
    private String fase;  // grupos, final, unico, etc.

    // Constructor vacío (obligatorio para JPA)
    public Evento() {
    }

    // Constructor completo
    public Evento(int id, String nombreEvento, String tipoEvento, String equipoLocal,
                  String equipoVisitante, String artistaPrincipal, int estadioId,
                  String fecha, String hora, String fase) {
        this.id = id;
        this.nombreEvento = nombreEvento;
        this.tipoEvento = tipoEvento;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.artistaPrincipal = artistaPrincipal;
        this.estadioId = estadioId;
        this.fecha = fecha;
        this.hora = hora;
        this.fase = fase;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    public String getEquipoLocal() { return equipoLocal; }
    public void setEquipoLocal(String equipoLocal) { this.equipoLocal = equipoLocal; }

    public String getEquipoVisitante() { return equipoVisitante; }
    public void setEquipoVisitante(String equipoVisitante) { this.equipoVisitante = equipoVisitante; }

    public String getArtistaPrincipal() { return artistaPrincipal; }
    public void setArtistaPrincipal(String artistaPrincipal) { this.artistaPrincipal = artistaPrincipal; }

    public int getEstadioId() { return estadioId; }
    public void setEstadioId(int estadioId) { this.estadioId = estadioId; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }
}