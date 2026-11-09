package com.example.demo.service;

import com.example.demo.model.Estadio;
import com.example.demo.repository.EstadioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadioService {

    private final EstadioRepository estadioRepository;

    @Autowired
    public EstadioService(EstadioRepository estadioRepository) {
        this.estadioRepository = estadioRepository;
    }

    // Obtener todos los estadios de la base de datos
    public List<Estadio> todos() {
        return estadioRepository.findAll();
    }

    // Buscar un estadio por ID
    public Estadio obtener(int id) {
        Optional<Estadio> estadio = estadioRepository.findById(id);
        return estadio.orElse(null); // Si no existe, retorna null
    }

    // Guardar un nuevo estadio en la base de datos
    public void agregar(Estadio estadio) {
        estadioRepository.save(estadio);
    }

    // Actualizar un estadio existente
    public void actualizar(int id, Estadio nuevoEstadio) {
        Optional<Estadio> estadioExistente = estadioRepository.findById(id);
        if (estadioExistente.isPresent()) {
            Estadio estadio = estadioExistente.get();
            // Actualizamos solo los campos necesarios
            estadio.setNombre(nuevoEstadio.getNombre());
            estadio.setCiudad(nuevoEstadio.getCiudad());
            estadio.setPais(nuevoEstadio.getPais());
            estadio.setCapacidad(nuevoEstadio.getCapacidad());
            estadio.setHoraAperturaPuertas(nuevoEstadio.getHoraAperturaPuertas());

            // Guardamos los cambios en la base de datos
            estadioRepository.save(estadio);
        }
    }

    // Eliminar un estadio de la base de datos
    public void eliminar(int id) {
        estadioRepository.deleteById(id);
    }
}