package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Evento;
import com.example.demo.service.EventoService;
import java.util.List;

@RestController
public class EventoController {
    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping("/eventos")
    public List<Evento> obtenerEventos(@RequestParam(required = false) String fase) {
        if (fase == null) {
            return eventoService.todos();
        }
        return eventoService.obtenerPorFase(fase);
    }

    @GetMapping("/eventos/{id}")
    public Evento obtenerEventoPorId(@PathVariable int id) {
        return eventoService.obtener(id);
    }

    @PostMapping("/eventos")
    public void agregarNuevoEvento(@RequestBody Evento evento) {
        eventoService.agregar(evento);
    }

    @PutMapping("/eventos/{id}")
    public void actualizarEvento(@PathVariable int id, @RequestBody Evento nuevoEvento) {
        eventoService.actualizar(id, nuevoEvento);
    }

    @DeleteMapping("/eventos/{id}")
    public void eliminarEvento(@PathVariable int id) {
        eventoService.eliminar(id);
    }
}