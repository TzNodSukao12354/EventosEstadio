package com.example.demo.service;

import com.example.demo.model.Zona;
import com.example.demo.model.AsientoEvento;
import com.example.demo.repository.AsientoEventoRepository;
import com.example.demo.repository.ZonaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AsientoEventoService {

    private static final Logger logger = LoggerFactory.getLogger(AsientoEventoService.class);

    private final AsientoEventoRepository asientoEventoRepository;
    private final ZonaRepository zonaRepository;

    @Autowired
    public AsientoEventoService(AsientoEventoRepository asientoEventoRepository, ZonaRepository zonaRepository) {
        this.asientoEventoRepository = asientoEventoRepository;
        this.zonaRepository = zonaRepository;
    }

    /**
     * Genera los asientos para una zona de un evento si aún no existen.
     * Calcula las filas y columnas basándose en el aforo máximo de la zona.
     */
    @Transactional
    public void generarAsientosParaZona(int eventoId, int zonaId) {
        // Verificar si ya existen asientos
        List<AsientoEvento> existentes = asientoEventoRepository.findByEventoIdAndZonaId(eventoId, zonaId);
        if (!existentes.isEmpty()) {
            logger.info("Asientos ya generados para evento {} zona {}", eventoId, zonaId);
            return;
        }

        Zona zona = zonaRepository.findById(zonaId).orElse(null);
        if (zona == null) {
            logger.warn("Zona {} no encontrada para generar asientos", zonaId);
            return;
        }

        int aforo = zona.getAforoMaximo();
        // Calcular filas y columnas según el aforo
        int columnas = calcularColumnas(aforo);
        int filas = (int) Math.ceil((double) aforo / columnas);

        List<String> letrasFilas = generarLetrasFilas(filas);
        List<AsientoEvento> asientos = new ArrayList<>();
        int contador = 0;

        for (int f = 0; f < filas && contador < aforo; f++) {
            for (int c = 1; c <= columnas && contador < aforo; c++) {
                AsientoEvento asiento = new AsientoEvento(eventoId, zonaId, letrasFilas.get(f), c);
                asientos.add(asiento);
                contador++;
            }
        }

        asientoEventoRepository.saveAll(asientos);
        logger.info("✅ Generados {} asientos para evento {} zona {} ({})", asientos.size(), eventoId, zonaId, zona.getNombre());
    }

    /**
     * Obtiene el mapa de asientos para una zona de un evento.
     */
    public Map<String, Object> obtenerMapaAsientos(int eventoId, int zonaId) {
        // Asegurar que existen los asientos
        generarAsientosParaZona(eventoId, zonaId);

        Zona zona = zonaRepository.findById(zonaId).orElse(null);
        if (zona == null) {
            return Map.of("error", "Zona no encontrada");
        }

        List<AsientoEvento> asientos = asientoEventoRepository.findByEventoIdAndZonaId(eventoId, zonaId);

        // Organizar por filas
        Map<String, List<Map<String, Object>>> filas = new HashMap<>();
        for (AsientoEvento asiento : asientos) {
            filas.computeIfAbsent(asiento.getFila(), k -> new ArrayList<>()).add(Map.of(
                    "columna", asiento.getColumna(),
                    "estado", asiento.getEstado(),
                    "id", asiento.getId()
            ));
        }

        long ocupados = asientoEventoRepository.countOcupadosByEventoIdAndZonaId(eventoId, zonaId);

        return Map.of(
                "zonaId", zonaId,
                "zonaNombre", zona.getNombre(),
                "precio", zona.getPrecio(),
                "aforoMaximo", zona.getAforoMaximo(),
                "ocupados", ocupados,
                "disponibles", asientos.size() - ocupados,
                "formacion", zona.getFormacion() != null ? zona.getFormacion() : "ESTADIO",
                "filas", filas
        );
    }

    /**
     * Reserva un asiento específico para un evento.
     */
    @Transactional
    public String reservarAsiento(int eventoId, int zonaId, String fila, int columna, String documentoUsuario) {
        List<AsientoEvento> asientos = asientoEventoRepository.findByEventoIdAndZonaId(eventoId, zonaId);
        if (asientos.isEmpty()) {
            generarAsientosParaZona(eventoId, zonaId);
        }

        AsientoEvento asiento = asientoEventoRepository
                .findByEventoIdAndZonaIdAndFilaAndColumna(eventoId, zonaId, fila, columna);

        if (asiento == null) {
            return "ERROR: El asiento no existe";
        }

        if (!"DISPONIBLE".equals(asiento.getEstado())) {
            return "ERROR: El asiento " + fila + columna + " ya está " + asiento.getEstado().toLowerCase();
        }

        asiento.setEstado("OCUPADO");
        asiento.setDocumentoUsuario(documentoUsuario);
        asientoEventoRepository.save(asiento);

        logger.info("✅ Asiento {}{} reservado para evento {} zona {} - Documento: {}",
                fila, columna, eventoId, zonaId, documentoUsuario);

        return "OK: asiento " + fila + columna + " reservado";
    }

    /**
     * Libera un asiento (al cancelar una entrada).
     */
    @Transactional
    public void liberarAsiento(int eventoId, int zonaId, String fila, int columna) {
        AsientoEvento asiento = asientoEventoRepository
                .findByEventoIdAndZonaIdAndFilaAndColumna(eventoId, zonaId, fila, columna);

        if (asiento != null) {
            asiento.setEstado("DISPONIBLE");
            asiento.setDocumentoUsuario(null);
            asientoEventoRepository.save(asiento);
            logger.info("Asiento {}{} liberado para evento {} zona {}", fila, columna, eventoId, zonaId);
        }
    }

    /**
     * Obtiene el asiento comprado por un usuario para un evento.
     */
    public AsientoEvento obtenerAsientoPorDocumento(int eventoId, String documentoUsuario) {
        // Buscar en todas las zonas del evento
        // Esto se puede optimizar con una consulta directa si es necesario
        return null; // Por ahora se maneja desde Entrada
    }

    // =========================================================================
    // Métodos auxiliares para calcular la distribución de asientos
    // =========================================================================

    private int calcularColumnas(int aforo) {
        // Estadios grandes: más columnas
        if (aforo >= 10000) return 50;
        if (aforo >= 5000) return 40;
        if (aforo >= 2000) return 30;
        if (aforo >= 500) return 20;
        return 15; // Arenas pequeñas
    }

    private List<String> generarLetrasFilas(int cantidad) {
        List<String> letras = new ArrayList<>();
        int charCode = 65; // 'A'

        for (int i = 0; i < cantidad; i++) {
            if (i < 26) {
                // Filas A-Z
                letras.add(String.valueOf((char) charCode));
                charCode++;
            } else {
                // Filas AA, AB, AC, ...
                int first = (i - 26) / 26;
                int second = (i - 26) % 26;
                letras.add(String.valueOf((char) ('A' + first)) + String.valueOf((char) ('A' + second)));
            }
        }

        return letras;
    }
}
