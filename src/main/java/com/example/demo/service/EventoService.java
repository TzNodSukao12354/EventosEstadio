package com.example.demo.service;

import com.example.demo.model.Evento;
import com.example.demo.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    @Autowired
    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public List<Evento> todos() {
        return eventoRepository.findAll();
    }

    public Evento obtener(int id) {
        Optional<Evento> evento = eventoRepository.findById(id);
        return evento.orElse(null);
    }

    public List<Evento> obtenerPorFase(String fase) {
        if (fase == null) {
            return todos();
        }
        return eventoRepository.findByFase(fase);
    }

    public void agregar(Evento evento) {
        // Al usar save(), JPA guardará automáticamente los nuevos campos (tipoEvento, artistaPrincipal)
        eventoRepository.save(evento);
    }

    public void actualizar(int id, Evento nuevoEvento) {
        Optional<Evento> eventoExistente = eventoRepository.findById(id);
        if (eventoExistente.isPresent()) {
            Evento evento = eventoExistente.get();

            // Actualizamos todos los campos, incluyendo los nuevos
            evento.setNombreEvento(nuevoEvento.getNombreEvento());
            evento.setTipoEvento(nuevoEvento.getTipoEvento()); // NUEVO
            evento.setEquipoLocal(nuevoEvento.getEquipoLocal());
            evento.setEquipoVisitante(nuevoEvento.getEquipoVisitante());
            evento.setArtistaPrincipal(nuevoEvento.getArtistaPrincipal()); // NUEVO
            evento.setEstadioId(nuevoEvento.getEstadioId());
            evento.setFecha(nuevoEvento.getFecha());
            evento.setHora(nuevoEvento.getHora());
            evento.setFase(nuevoEvento.getFase());

            eventoRepository.save(evento);
        }
    }

    public void eliminar(int id) {
        eventoRepository.deleteById(id);
    }
}