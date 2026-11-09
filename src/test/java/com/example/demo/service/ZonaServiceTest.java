package com.example.demo.service;

import com.example.demo.model.DisponibilidadZona;
import com.example.demo.model.Zona;
import com.example.demo.repository.DisponibilidadZonaRepository;
import com.example.demo.repository.ZonaRepository;
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
 * Test del ZonaService siguiendo los principios de TDD (Semana 3).
 *
 * Valida las operaciones CRUD de zonas y la lógica de disponibilidad de asientos
 * para eventos específicos.
 *
 * Frase clave para defensa:
 * "Aplicamos principios de TDD para asegurar que las reglas de negocio,
 * como la validación de aforo y fechas, sean robustas y estén libres de errores."
 */
@ExtendWith(MockitoExtension.class)
class ZonaServiceTest {

    @Mock
    private ZonaRepository zonaRepository;

    @Mock
    private DisponibilidadZonaRepository disponibilidadZonaRepository;

    @InjectMocks
    private ZonaService zonaService;

    // ------------------------------------------------------------------
    // Datos de prueba reutilizables
    // ------------------------------------------------------------------

    private Zona crearZonaPlatea() {
        Zona zona = new Zona();
        zona.setId(1);
        zona.setEstadioId(1);
        zona.setNombre("Platea Preferencia");
        zona.setPrecio(250.00);
        zona.setAforoMaximo(5000);
        zona.setAforoActual(0);
        return zona;
    }

    private Zona crearZonaGrada() {
        Zona zona = new Zona();
        zona.setId(2);
        zona.setEstadioId(1);
        zona.setNombre("Grada Superior");
        zona.setPrecio(80.00);
        zona.setAforoMaximo(15000);
        zona.setAforoActual(0);
        return zona;
    }

    private DisponibilidadZona crearDisponibilidad(int eventoId, int zonaId, int aforoOcupado) {
        DisponibilidadZona disp = new DisponibilidadZona();
        disp.setEventoId(eventoId);
        disp.setZonaId(zonaId);
        disp.setAforoOcupado(aforoOcupado);
        return disp;
    }

    // =========================================================================
    // todos() — Obtener todas las zonas
    // =========================================================================

    @Nested
    @DisplayName("Obtener todas las zonas")
    class TodosTests {

        @Test
        @DisplayName("Debe retornar todas las zonas disponibles")
        void todos_retornaLista() {
            // Arrange
            when(zonaRepository.findAll()).thenReturn(Arrays.asList(crearZonaPlatea(), crearZonaGrada()));

            // Act
            List<Zona> resultado = zonaService.todos();

            // Assert
            assertEquals(2, resultado.size());
            assertEquals("Platea Preferencia", resultado.get(0).getNombre());
            verify(zonaRepository).findAll();
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay zonas")
        void todos_listaVacia() {
            // Arrange
            when(zonaRepository.findAll()).thenReturn(List.of());

            // Act
            List<Zona> resultado = zonaService.todos();

            // Assert
            assertTrue(resultado.isEmpty());
        }
    }

    // =========================================================================
    // obtener(id) — Buscar zona por ID
    // =========================================================================

    @Nested
    @DisplayName("Obtener zona por ID")
    class ObtenerTests {

        @Test
        @DisplayName("Debe retornar la zona cuando existe")
        void obtener_zonaExiste() {
            // Arrange
            Zona zona = crearZonaPlatea();
            when(zonaRepository.findById(1)).thenReturn(Optional.of(zona));

            // Act
            Zona resultado = zonaService.obtener(1);

            // Assert
            assertNotNull(resultado);
            assertEquals(1, resultado.getId());
            assertEquals("Platea Preferencia", resultado.getNombre());
            assertEquals(250.00, resultado.getPrecio());
        }

        @Test
        @DisplayName("Debe retornar null cuando la zona no existe")
        void obtener_zonaNoExiste() {
            // Arrange
            when(zonaRepository.findById(999)).thenReturn(Optional.empty());

            // Act
            Zona resultado = zonaService.obtener(999);

            // Assert
            assertNull(resultado);
        }
    }

    // =========================================================================
    // obtenerPorEstadio(estadioId) — Buscar zonas por estadio
    // =========================================================================

    @Nested
    @DisplayName("Obtener zonas por estadio")
    class ObtenerPorEstadioTests {

        @Test
        @DisplayName("Debe retornar las zonas del estadio especificado")
        void obtenerPorEstadio_zonasEncontradas() {
            // Arrange
            when(zonaRepository.findByEstadioId(1)).thenReturn(Arrays.asList(crearZonaPlatea(), crearZonaGrada()));

            // Act
            List<Zona> resultado = zonaService.obtenerPorEstadio(1);

            // Assert
            assertEquals(2, resultado.size());
            assertEquals(1, resultado.get(0).getEstadioId());
            assertEquals(1, resultado.get(1).getEstadioId());
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay zonas para el estadio")
        void obtenerPorEstadio_sinZonas() {
            // Arrange
            when(zonaRepository.findByEstadioId(999)).thenReturn(List.of());

            // Act
            List<Zona> resultado = zonaService.obtenerPorEstadio(999);

            // Assert
            assertTrue(resultado.isEmpty());
        }
    }

    // =========================================================================
    // hayDisponibilidad(eventoId, zonaId) — Verificar disponibilidad
    // =========================================================================

    @Nested
    @DisplayName("Verificar disponibilidad")
    class HayDisponibilidadTests {

        @Test
        @DisplayName("Debe retornar true cuando hay asientos disponibles")
        void hayDisponibilidad_conAsientos() {
            // Arrange
            Zona zona = crearZonaPlatea(); // aforoMaximo = 5000
            DisponibilidadZona disp = crearDisponibilidad(1, 1, 3000); // 3000 ocupados de 5000

            when(zonaRepository.findById(1)).thenReturn(Optional.of(zona));
            when(disponibilidadZonaRepository.findByEventoIdAndZonaId(1, 1)).thenReturn(Optional.of(disp));

            // Act
            boolean resultado = zonaService.hayDisponibilidad(1, 1);

            // Assert
            assertTrue(resultado);
        }

        @Test
        @DisplayName("Debe retornar false cuando la zona está llena")
        void hayDisponibilidad_zonaLlena() {
            // Arrange
            Zona zona = crearZonaPlatea(); // aforoMaximo = 5000
            DisponibilidadZona disp = crearDisponibilidad(1, 1, 5000); // 5000 ocupados de 5000

            when(zonaRepository.findById(1)).thenReturn(Optional.of(zona));
            when(disponibilidadZonaRepository.findByEventoIdAndZonaId(1, 1)).thenReturn(Optional.of(disp));

            // Act
            boolean resultado = zonaService.hayDisponibilidad(1, 1);

            // Assert
            assertFalse(resultado);
        }

        @Test
        @DisplayName("Debe retornar true cuando no existe registro de disponibilidad (primera venta)")
        void hayDisponibilidad_sinRegistro() {
            // Arrange
            Zona zona = crearZonaPlatea();

            when(zonaRepository.findById(1)).thenReturn(Optional.of(zona));
            when(disponibilidadZonaRepository.findByEventoIdAndZonaId(1, 1)).thenReturn(Optional.empty());

            // Act
            boolean resultado = zonaService.hayDisponibilidad(1, 1);

            // Assert
            assertTrue(resultado);
        }

        @Test
        @DisplayName("Debe retornar false cuando la zona no existe")
        void hayDisponibilidad_zonaNoExiste() {
            // Arrange
            when(zonaRepository.findById(999)).thenReturn(Optional.empty());

            // Act
            boolean resultado = zonaService.hayDisponibilidad(1, 999);

            // Assert
            assertFalse(resultado);
        }
    }

    // =========================================================================
    // ocuparAsiento(eventoId, zonaId) — Ocupar un asiento
    // =========================================================================

    @Nested
    @DisplayName("Ocupar asiento")
    class OcuparAsientoTests {

        @Test
        @DisplayName("Debe aumentar el aforo ocupado cuando ya existe registro")
        void ocuparAsiento_registroExiste() {
            // Arrange
            DisponibilidadZona disp = crearDisponibilidad(1, 1, 3000);
            when(disponibilidadZonaRepository.findByEventoIdAndZonaId(1, 1)).thenReturn(Optional.of(disp));
            when(disponibilidadZonaRepository.save(any(DisponibilidadZona.class))).thenReturn(disp);

            // Act
            zonaService.ocuparAsiento(1, 1);

            // Assert
            assertEquals(3001, disp.getAforoOcupado());
            verify(disponibilidadZonaRepository).save(disp);
        }

        @Test
        @DisplayName("Debe crear nuevo registro con aforo 1 cuando no existe")
        void ocuparAsiento_sinRegistro() {
            // Arrange
            when(disponibilidadZonaRepository.findByEventoIdAndZonaId(1, 1)).thenReturn(Optional.empty());
            when(disponibilidadZonaRepository.save(any(DisponibilidadZona.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            zonaService.ocuparAsiento(1, 1);

            // Assert
            verify(disponibilidadZonaRepository).save(argThat((DisponibilidadZona d) ->
                    d.getEventoId() == 1 && d.getZonaId() == 1 && d.getAforoOcupado() == 1
            ));
        }
    }

    // =========================================================================
    // liberarAsiento(eventoId, zonaId) — Liberar un asiento
    // =========================================================================

    @Nested
    @DisplayName("Liberar asiento")
    class LiberarAsientoTests {

        @Test
        @DisplayName("Debe disminuir el aforo ocupado cuando hay asientos ocupados")
        void liberarAsiento_conAsientos() {
            // Arrange
            DisponibilidadZona disp = crearDisponibilidad(1, 1, 3000);
            when(disponibilidadZonaRepository.findByEventoIdAndZonaId(1, 1)).thenReturn(Optional.of(disp));
            when(disponibilidadZonaRepository.save(any(DisponibilidadZona.class))).thenReturn(disp);

            // Act
            zonaService.liberarAsiento(1, 1);

            // Assert
            assertEquals(2999, disp.getAforoOcupado());
            verify(disponibilidadZonaRepository).save(disp);
        }

        @Test
        @DisplayName("No debe disminuir si el aforo ocupado es 0")
        void liberarAsiento_aforoCero() {
            // Arrange
            DisponibilidadZona disp = crearDisponibilidad(1, 1, 0);
            when(disponibilidadZonaRepository.findByEventoIdAndZonaId(1, 1)).thenReturn(Optional.of(disp));

            // Act
            zonaService.liberarAsiento(1, 1);

            // Assert
            assertEquals(0, disp.getAforoOcupado());
            verify(disponibilidadZonaRepository, never()).save(any(DisponibilidadZona.class));
        }

        @Test
        @DisplayName("No debe hacer nada cuando no existe registro de disponibilidad")
        void liberarAsiento_sinRegistro() {
            // Arrange
            when(disponibilidadZonaRepository.findByEventoIdAndZonaId(1, 1)).thenReturn(Optional.empty());

            // Act
            zonaService.liberarAsiento(1, 1);

            // Assert
            verify(disponibilidadZonaRepository, never()).save(any(DisponibilidadZona.class));
        }
    }

    // =========================================================================
    // agregar(zona) — Crear nueva zona
    // =========================================================================

    @Nested
    @DisplayName("Agregar nueva zona")
    class AgregarTests {

        @Test
        @DisplayName("Debe guardar una nueva zona en el repository")
        void agregar_nuevaZona() {
            // Arrange
            Zona nuevaZona = crearZonaGrada();
            when(zonaRepository.save(any(Zona.class))).thenReturn(nuevaZona);

            // Act
            zonaService.agregar(nuevaZona);

            // Assert
            verify(zonaRepository).save(nuevaZona);
        }
    }

    // =========================================================================
    // actualizar(id, nuevaZona) — Actualizar zona existente
    // =========================================================================

    @Nested
    @DisplayName("Actualizar zona existente")
    class ActualizarTests {

        @Test
        @DisplayName("Debe actualizar todos los campos de la zona cuando existe")
        void actualizar_zonaExiste() {
            // Arrange
            Zona existente = crearZonaPlatea();
            Zona actualizada = new Zona();
            actualizada.setEstadioId(2);
            actualizada.setNombre("Platea VIP");
            actualizada.setPrecio(500.00);
            actualizada.setAforoMaximo(2000);
            actualizada.setAforoActual(100);

            when(zonaRepository.findById(1)).thenReturn(Optional.of(existente));
            when(zonaRepository.save(any(Zona.class))).thenReturn(existente);

            // Act
            zonaService.actualizar(1, actualizada);

            // Assert
            assertEquals(2, existente.getEstadioId());
            assertEquals("Platea VIP", existente.getNombre());
            assertEquals(500.00, existente.getPrecio());
            assertEquals(2000, existente.getAforoMaximo());
            assertEquals(100, existente.getAforoActual());
            verify(zonaRepository).save(existente);
        }

        @Test
        @DisplayName("No debe hacer nada cuando la zona no existe")
        void actualizar_zonaNoExiste() {
            // Arrange
            Zona actualizada = crearZonaPlatea();
            when(zonaRepository.findById(999)).thenReturn(Optional.empty());

            // Act
            zonaService.actualizar(999, actualizada);

            // Assert
            verify(zonaRepository, never()).save(any(Zona.class));
        }
    }

    // =========================================================================
    // eliminar(id) — Eliminar zona
    // =========================================================================

    @Nested
    @DisplayName("Eliminar zona")
    class EliminarTests {

        @Test
        @DisplayName("Debe eliminar la zona por ID")
        void eliminar_zona() {
            // Arrange
            doNothing().when(zonaRepository).deleteById(1);

            // Act
            zonaService.eliminar(1);

            // Assert
            verify(zonaRepository).deleteById(1);
        }
    }
}
