package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Entrada;
import com.example.demo.service.EntradaService;
import java.util.List;

@RestController
public class EntradaController {
    private final EntradaService entradaService;

    public EntradaController(EntradaService entradaService) {
        this.entradaService = entradaService;
    }

    // Endpoint para obtener todas las entradas
    @GetMapping("/entradas")
    public List<Entrada> obtenerTodasLasEntradas() {
        return entradaService.todos();
    }

    // Endpoint para obtener una entrada por su ID
    @GetMapping("/entradas/{id}")
    public Entrada obtenerEntradaPorId(@PathVariable int id) {
        return entradaService.obtener(id);
    }

    // Endpoint para comprar (crear) una entrada -> valida aforo disponible
    @PostMapping("/entradas")
    public String comprarEntrada(@RequestBody Entrada entrada) {
        return entradaService.comprarEntrada(entrada);
    }

    // Endpoint para reportar una entrada como perdida
    @PutMapping("/entradas/{id}/reportar-perdida")
    public String reportarEntradaPerdida(@PathVariable int id) {
        return entradaService.reportarPerdida(id);
    }

    // Endpoint para solicitar la reimpresion de una entrada danada
    @PostMapping("/entradas/{id}/reimpresion")
    public String solicitarReimpresionEntrada(@PathVariable int id) {
        return entradaService.solicitarReimpresion(id);
    }

    // Endpoint para validar si, a la hora actual, todavia se puede ingresar
    @GetMapping("/entradas/{id}/validar-ingreso")
    public String validarIngresoEntrada(@PathVariable int id) {
        return entradaService.validarIngreso(id);
    }

    // Endpoint para marcar una entrada como usada (cuando la persona ya entro al estadio)
    @PutMapping("/entradas/{id}/marcar-usada")
    public String marcarEntradaUsada(@PathVariable int id) {
        return entradaService.marcarUsada(id);
    }

    // Endpoint para "eliminar" una entrada. No la borra del todo (no seria realista),
    // solo cambia su estado a CANCELADA y libera el cupo de la zona.
    @DeleteMapping("/entradas/{id}")
    public String cancelarEntrada(@PathVariable int id) {
        return entradaService.cancelar(id);
    }
}
