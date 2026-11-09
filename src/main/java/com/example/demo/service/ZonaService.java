package com.example.demo.service;

import com.example.demo.model.DisponibilidadZona;
import com.example.demo.model.Zona;
import com.example.demo.repository.DisponibilidadZonaRepository;
import com.example.demo.repository.ZonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ZonaService {

    private final ZonaRepository zonaRepository;
    private final DisponibilidadZonaRepository disponibilidadZonaRepository;

    @Autowired
    public ZonaService(ZonaRepository zonaRepository, DisponibilidadZonaRepository disponibilidadZonaRepository) {
        this.zonaRepository = zonaRepository;
        this.disponibilidadZonaRepository = disponibilidadZonaRepository;
    }

    public List<Zona> todos() {
        return zonaRepository.findAll();
    }

    public Zona obtener(int id) {
        Optional<Zona> zona = zonaRepository.findById(id);
        return zona.orElse(null);
    }

    public List<Zona> obtenerPorEstadio(int estadioId) {
        return zonaRepository.findByEstadioId(estadioId);
    }

    // NUEVO: Verificar disponibilidad para un evento específico
    public boolean hayDisponibilidad(int eventoId, int zonaId) {
        Zona zona = obtener(zonaId);
        if (zona == null) {
            return false;
        }

        // Buscar el "cuaderno" de este evento y zona
        Optional<DisponibilidadZona> disponibilidad = disponibilidadZonaRepository.findByEventoIdAndZonaId(eventoId, zonaId);

        if (disponibilidad.isPresent()) {
            // Si existe, verificar si el aforo ocupado es menor al máximo
            return disponibilidad.get().getAforoOcupado() < zona.getAforoMaximo();
        } else {
            // Si no existe, significa que no se ha vendido ninguna entrada para este evento en esta zona
            return true;
        }
    }

    // NUEVO: Ocupar asiento para un evento específico
    public void ocuparAsiento(int eventoId, int zonaId) {
        Optional<DisponibilidadZona> disponibilidad = disponibilidadZonaRepository.findByEventoIdAndZonaId(eventoId, zonaId);

        if (disponibilidad.isPresent()) {
            // Si ya existe el registro, aumentar el aforo ocupado
            DisponibilidadZona disp = disponibilidad.get();
            disp.setAforoOcupado(disp.getAforoOcupado() + 1);
            disponibilidadZonaRepository.save(disp);
        } else {
            // Si no existe, crear un nuevo registro con aforo ocupado = 1
            DisponibilidadZona nuevaDisponibilidad = new DisponibilidadZona(eventoId, zonaId, 1);
            disponibilidadZonaRepository.save(nuevaDisponibilidad);
        }
    }

    // NUEVO: Liberar asiento para un evento específico
    public void liberarAsiento(int eventoId, int zonaId) {
        Optional<DisponibilidadZona> disponibilidad = disponibilidadZonaRepository.findByEventoIdAndZonaId(eventoId, zonaId);

        if (disponibilidad.isPresent()) {
            DisponibilidadZona disp = disponibilidad.get();
            if (disp.getAforoOcupado() > 0) {
                disp.setAforoOcupado(disp.getAforoOcupado() - 1);
                disponibilidadZonaRepository.save(disp);
            }
        }
    }

    public void agregar(Zona zona) {
        zonaRepository.save(zona);
    }

    public void actualizar(int id, Zona nuevaZona) {
        Optional<Zona> zonaExistente = zonaRepository.findById(id);
        if (zonaExistente.isPresent()) {
            Zona zona = zonaExistente.get();
            zona.setEstadioId(nuevaZona.getEstadioId());
            zona.setNombre(nuevaZona.getNombre());
            zona.setPrecio(nuevaZona.getPrecio());
            zona.setAforoMaximo(nuevaZona.getAforoMaximo());
            zona.setAforoActual(nuevaZona.getAforoActual());
            zonaRepository.save(zona);
        }
    }

    public void eliminar(int id) {
        zonaRepository.deleteById(id);
    }
}