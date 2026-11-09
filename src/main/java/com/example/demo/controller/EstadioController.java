package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Estadio;
import com.example.demo.service.EstadioService;
import java.util.List;

@RestController
public class EstadioController {
    private final EstadioService estadioService;

    public EstadioController(EstadioService estadioService) {
        this.estadioService = estadioService;
    }

    // Endpoint para obtener todos los estadios
    @GetMapping("/estadios")
    public List<Estadio> obtenerTodosLosEstadios() {
        return estadioService.todos();
    }

    // Endpoint para obtener un estadio por su ID
    @GetMapping("/estadios/{id}")
    public Estadio obtenerEstadioPorId(@PathVariable int id) {
        return estadioService.obtener(id);
    }

    // Endpoint para agregar un nuevo estadio
    @PostMapping("/estadios")
    public void agregarNuevoEstadio(@RequestBody Estadio estadio) {
        estadioService.agregar(estadio);
    }

    // Endpoint para actualizar un estadio existente
    @PutMapping("/estadios/{id}")
    public void actualizarEstadio(@PathVariable int id, @RequestBody Estadio nuevoEstadio) {
        estadioService.actualizar(id, nuevoEstadio);
    }

    // Endpoint para eliminar un estadio por su ID
    @DeleteMapping("/estadios/{id}")
    public void eliminarEstadio(@PathVariable int id) {
        estadioService.eliminar(id);
    }
}
