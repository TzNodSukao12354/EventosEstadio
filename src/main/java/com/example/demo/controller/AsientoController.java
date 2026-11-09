package com.example.demo.controller;

import com.example.demo.service.AsientoEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/asientos")
@CrossOrigin(origins = "http://localhost:4200")
public class AsientoController {

    private final AsientoEventoService asientoEventoService;

    @Autowired
    public AsientoController(AsientoEventoService asientoEventoService) {
        this.asientoEventoService = asientoEventoService;
    }

    /**
     * GET /api/asientos/evento/{eventoId}/zona/{zonaId}
     * Obtiene el mapa de asientos para una zona de un evento.
     */
    @GetMapping("/evento/{eventoId}/zona/{zonaId}")
    public ResponseEntity<Map<String, Object>> obtenerMapaAsientos(
            @PathVariable int eventoId,
            @PathVariable int zonaId) {
        Map<String, Object> mapa = asientoEventoService.obtenerMapaAsientos(eventoId, zonaId);
        return ResponseEntity.ok(mapa);
    }

    /**
     * POST /api/asientos/reservar
     * Reserva un asiento específico.
     * Body: { eventoId, zonaId, fila, columna, documentoUsuario }
     */
    @PostMapping("/reservar")
    public ResponseEntity<Map<String, String>> reservarAsiento(@RequestBody Map<String, Object> body) {
        int eventoId = Integer.parseInt(body.get("eventoId").toString());
        int zonaId = Integer.parseInt(body.get("zonaId").toString());
        String fila = body.get("fila").toString();
        int columna = Integer.parseInt(body.get("columna").toString());
        String documentoUsuario = body.get("documentoUsuario").toString();

        String resultado = asientoEventoService.reservarAsiento(eventoId, zonaId, fila, columna, documentoUsuario);

        if (resultado.startsWith("ERROR")) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", resultado));
        }

        return ResponseEntity.ok(Map.of("mensaje", resultado, "fila", fila, "columna", String.valueOf(columna)));
    }

    /**
     * POST /api/asientos/liberar
     * Libera un asiento (al cancelar una entrada).
     * Body: { eventoId, zonaId, fila, columna }
     */
    @PostMapping("/liberar")
    public ResponseEntity<Map<String, String>> liberarAsiento(@RequestBody Map<String, Object> body) {
        int eventoId = Integer.parseInt(body.get("eventoId").toString());
        int zonaId = Integer.parseInt(body.get("zonaId").toString());
        String fila = body.get("fila").toString();
        int columna = Integer.parseInt(body.get("columna").toString());

        asientoEventoService.liberarAsiento(eventoId, zonaId, fila, columna);
        return ResponseEntity.ok(Map.of("mensaje", "Asiento " + fila + columna + " liberado"));
    }
}
