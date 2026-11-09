package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Zona;
import com.example.demo.service.ZonaService;
import java.util.List;

@RestController
public class ZonaController {
    private final ZonaService zonaService;

    public ZonaController(ZonaService zonaService) {
        this.zonaService = zonaService;
    }

    // Endpoint para obtener todas las zonas
    @GetMapping("/zonas")
    public List<Zona> obtenerTodasLasZonas() {
        return zonaService.todos();
    }

    // Endpoint para obtener una zona por su ID
    @GetMapping("/zonas/{id}")
    public Zona obtenerZonaPorId(@PathVariable int id) {
        return zonaService.obtener(id);
    }

    // Endpoint para obtener las zonas de un estadio especifico
    @GetMapping("/estadios/{estadioId}/zonas")
    public List<Zona> obtenerZonasPorEstadio(@PathVariable int estadioId) {
        return zonaService.obtenerPorEstadio(estadioId);
    }

    // Endpoint para agregar una nueva zona
    @PostMapping("/zonas")
    public void agregarNuevaZona(@RequestBody Zona zona) {
        zonaService.agregar(zona);
    }

    // Endpoint para actualizar una zona existente
    @PutMapping("/zonas/{id}")
    public void actualizarZona(@PathVariable int id, @RequestBody Zona nuevaZona) {
        zonaService.actualizar(id, nuevaZona);
    }

    // Endpoint para eliminar una zona por su ID
    @DeleteMapping("/zonas/{id}")
    public void eliminarZona(@PathVariable int id) {
        zonaService.eliminar(id);
    }
}
