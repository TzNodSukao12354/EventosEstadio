package com.example.demo.service;

import com.example.demo.model.Evento;
import com.example.demo.repository.EventoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test del EventoService siguiendo los principios de TDD (Semana 3).
 *
 * Valida las operaciones CRUD y búsqueda por fase de los eventos.
 *
 * Frase clave para defensa:
 * "Aplicamos principios de TDD para asegurar que las reglas de negocio,
 * como la validación de aforo y fechas, sean robustas y estén libres de errores."
 */
@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private EventoService eventoService;

    // ------------------------------------------------------------------
    // Datos de prueba reutilizables
    // ------------------------------------------------------------------

    private Evento crearEventoFutbol() {
        Evento evento = new Evento();
        evento.setId(1);
        evento.setNombreEvento("Alianza vs Universitario");
        evento.setTipoEvento("fútbol");
        evento.setEquipoLocal("Alianza Lima");
        evento.setEquipoVisitante("Universitario");
        evento.setEstadioId(1);
        evento.setFecha("2026-11-10");
        evento.setHora("15:30:00");
        evento.setFase("final");
        return evento;
    }

    private Evento crearEventoConcierto() {
        Evento evento = new Evento();
        evento.setId(2);
        evento.setNombreEvento("Concierto Coldplay");
        evento.setTipoEvento("concierto");
        evento.setArtistaPrincipal("Coldplay");
        evento.setEstadioId(1);
        evento.setFecha("2026-12-15");
        evento.setHora("20:00:00");
        evento.setFase(null);
        return evento;
    }

    // =========================================================================
    // todos() — Obtener todos los eventos
    // =========================================================================

    @Nested
    @DisplayName("Obtener todos los eventos")
    class TodosTests {

        @Test
        @DisplayName("Debe retornar todos los eventos disponibles")
        void todos_retornaLista() {
            // Arrange
            when(eventoRepository.findAll()).thenReturn(Arrays.asList(crearEventoFutbol(), crearEventoConcierto()));

            // Act
            List<Evento> resultado = eventoService.todos();

            // Assert
            assertEquals(2, resultado.size());
            assertEquals("Alianza vs Universitario", resultado.get(0).getNombreEvento());
            assertEquals("Concierto Coldplay", resultado.get(1).getNombreEvento());
            verify(eventoRepository).findAll();
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay eventos")
        void todos_listaVacia() {
            // Arrange
            when(eventoRepository.findAll()).thenReturn(List.of());

            // Act
            List<Evento> resultado = eventoService.todos();

            // Assert
            assertTrue(resultado.isEmpty());
        }
    }

    // =========================================================================
    // obtener(id) — Buscar evento por ID
    // =========================================================================

    @Nested
    @DisplayName("Obtener evento por ID")
    class ObtenerTests {

        @Test
        @DisplayName("Debe retornar el evento cuando existe")
        void obtener_eventoExiste() {
            // Arrange
            Evento evento = crearEventoFutbol();
            when(eventoRepository.findById(1)).thenReturn(Optional.of(evento));

            // Act
            Evento resultado = eventoService.obtener(1);

            // Assert
            assertNotNull(resultado);
            assertEquals(1, resultado.getId());
            assertEquals("Alianza vs Universitario", resultado.getNombreEvento());
        }

        @Test
        @DisplayName("Debe retornar null cuando el evento no existe")
        void obtener_eventoNoExiste() {
            // Arrange
            when(eventoRepository.findById(999)).thenReturn(Optional.empty());

            // Act
            Evento resultado = eventoService.obtener(999);

            // Assert
            assertNull(resultado);
        }
    }

    // =========================================================================
    // obtenerPorFase(fase) — Buscar eventos por fase
    // =========================================================================

    @Nested
    @DisplayName("Obtener eventos por fase")
    class ObtenerPorFaseTests {

        @Test
        @DisplayName("Debe retornar eventos filtrados por fase")
        void obtenerPorFase_conFase() {
            // Arrange
            Evento finalEvento = crearEventoFutbol();
            when(eventoRepository.findByFase("final")).thenReturn(List.of(finalEvento));

            // Act
            List<Evento> resultado = eventoService.obtenerPorFase("final");

            // Assert
            assertEquals(1, resultado.size());
            assertEquals("final", resultado.get(0).getFase());
        }

        @Test
        @DisplayName("Debe retornar todos los eventos cuando fase es null")
        void obtenerPorFase_nullRetornaTodos() {
            // Arrange
            when(eventoRepository.findAll()).thenReturn(Arrays.asList(crearEventoFutbol(), crearEventoConcierto()));

            // Act
            List<Evento> resultado = eventoService.obtenerPorFase(null);

            // Assert
            assertEquals(2, resultado.size());
            verify(eventoRepository).findAll();
            verify(eventoRepository, never()).findByFase(anyString());
        }
    }

    // =========================================================================
    // agregar(evento) — Crear nuevo evento
    // =========================================================================

    @Nested
    @DisplayName("Agregar nuevo evento")
    class AgregarTests {

        @Test
        @DisplayName("Debe guardar un nuevo evento en el repository")
        void agregar_nuevoEvento() {
            // Arrange
            Evento nuevoEvento = crearEventoConcierto();
            when(eventoRepository.save(any(Evento.class))).thenReturn(nuevoEvento);

            // Act
            eventoService.agregar(nuevoEvento);

            // Assert
            verify(eventoRepository).save(nuevoEvento);
        }
    }

    // =========================================================================
    // actualizar(id, nuevoEvento) — Actualizar evento existente
    // =========================================================================

    @Nested
    @DisplayName("Actualizar evento existente")
    class ActualizarTests {

        @Test
        @DisplayName("Debe actualizar todos los campos del evento cuando existe")
        void actualizar_eventoExiste() {
            // Arrange
            Evento existente = crearEventoFutbol();
            Evento actualizado = new Evento();
            actualizado.setNombreEvento("Perú vs Chile");
            actualizado.setTipoEvento("fútbol");
            actualizado.setEquipoLocal("Perú");
            actualizado.setEquipoVisitante("Chile");
            actualizado.setEstadioId(2);
            actualizado.setFecha("2026-11-18");
            actualizado.setHora("20:00:00");
            actualizado.setFase("eliminatorias");

            when(eventoRepository.findById(1)).thenReturn(Optional.of(existente));
            when(eventoRepository.save(any(Evento.class))).thenReturn(existente);

            // Act
            eventoService.actualizar(1, actualizado);

            // Assert
            assertEquals("Perú vs Chile", existente.getNombreEvento());
            assertEquals("fútbol", existente.getTipoEvento());
            assertEquals("Perú", existente.getEquipoLocal());
            assertEquals("Chile", existente.getEquipoVisitante());
            assertEquals(2, existente.getEstadioId());
            assertEquals("2026-11-18", existente.getFecha());
            assertEquals("eliminatorias", existente.getFase());
            verify(eventoRepository).save(existente);
        }

        @Test
        @DisplayName("Debe actualizar artista principal en conciertos")
        void actualizar_conciertoArtista() {
            // Arrange
            Evento existente = crearEventoConcierto();
            Evento actualizado = new Evento();
            actualizado.setNombreEvento("Concierto Bad Bunny");
            actualizado.setTipoEvento("concierto");
            actualizado.setArtistaPrincipal("Bad Bunny");
            actualizado.setEstadioId(3);
            actualizado.setFecha("2026-12-01");
            actualizado.setHora("21:00:00");

            when(eventoRepository.findById(2)).thenReturn(Optional.of(existente));
            when(eventoRepository.save(any(Evento.class))).thenReturn(existente);

            // Act
            eventoService.actualizar(2, actualizado);

            // Assert
            assertEquals("Bad Bunny", existente.getArtistaPrincipal());
            assertEquals("concierto", existente.getTipoEvento());
        }

        @Test
        @DisplayName("No debe hacer nada cuando el evento no existe")
        void actualizar_eventoNoExiste() {
            // Arrange
            Evento actualizado = crearEventoFutbol();
            when(eventoRepository.findById(999)).thenReturn(Optional.empty());

            // Act
            eventoService.actualizar(999, actualizado);

            // Assert
            verify(eventoRepository, never()).save(any(Evento.class));
        }
    }

    // =========================================================================
    // eliminar(id) — Eliminar evento
    // =========================================================================

    @Nested
    @DisplayName("Eliminar evento")
    class EliminarTests {

        @Test
        @DisplayName("Debe eliminar el evento por ID")
        void eliminar_evento() {
            // Arrange
            doNothing().when(eventoRepository).deleteById(1);

            // Act
            eventoService.eliminar(1);

            // Assert
            verify(eventoRepository).deleteById(1);
        }
    }
}
