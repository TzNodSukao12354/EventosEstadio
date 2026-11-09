package com.example.demo.service;

import com.example.demo.model.Estadio;
import com.example.demo.repository.EstadioRepository;
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
 * Test del EstadioService siguiendo los principios de TDD (Semana 3).
 *
 * Valida las operaciones CRUD de los estadios.
 *
 * Frase clave para defensa:
 * "Aplicamos principios de TDD para asegurar que las reglas de negocio,
 * como la validación de aforo y fechas, sean robustas y estén libres de errores."
 */
@ExtendWith(MockitoExtension.class)
class EstadioServiceTest {

    @Mock
    private EstadioRepository estadioRepository;

    @InjectMocks
    private EstadioService estadioService;

    // ------------------------------------------------------------------
    // Datos de prueba reutilizables
    // ------------------------------------------------------------------

    private Estadio crearEstadioNacional() {
        Estadio estadio = new Estadio();
        estadio.setId(1);
        estadio.setNombre("Estadio Nacional");
        estadio.setCiudad("Lima");
        estadio.setPais("Perú");
        estadio.setCapacidad(50000);
        estadio.setHoraAperturaPuertas(2);
        return estadio;
    }

    private Estadio crearEstadioVillanueva() {
        Estadio estadio = new Estadio();
        estadio.setId(2);
        estadio.setNombre("Estadio Alejandro Villanueva");
        estadio.setCiudad("Lima");
        estadio.setPais("Perú");
        estadio.setCapacidad(35000);
        estadio.setHoraAperturaPuertas(2);
        return estadio;
    }

    // =========================================================================
    // todos() — Obtener todos los estadios
    // =========================================================================

    @Nested
    @DisplayName("Obtener todos los estadios")
    class TodosTests {

        @Test
        @DisplayName("Debe retornar todos los estadios disponibles")
        void todos_retornaLista() {
            // Arrange
            when(estadioRepository.findAll()).thenReturn(Arrays.asList(crearEstadioNacional(), crearEstadioVillanueva()));

            // Act
            List<Estadio> resultado = estadioService.todos();

            // Assert
            assertEquals(2, resultado.size());
            assertEquals("Estadio Nacional", resultado.get(0).getNombre());
            assertEquals("Estadio Alejandro Villanueva", resultado.get(1).getNombre());
            verify(estadioRepository).findAll();
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay estadios")
        void todos_listaVacia() {
            // Arrange
            when(estadioRepository.findAll()).thenReturn(List.of());

            // Act
            List<Estadio> resultado = estadioService.todos();

            // Assert
            assertTrue(resultado.isEmpty());
        }
    }

    // =========================================================================
    // obtener(id) — Buscar estadio por ID
    // =========================================================================

    @Nested
    @DisplayName("Obtener estadio por ID")
    class ObtenerTests {

        @Test
        @DisplayName("Debe retornar el estadio cuando existe")
        void obtener_estadioExiste() {
            // Arrange
            Estadio estadio = crearEstadioNacional();
            when(estadioRepository.findById(1)).thenReturn(Optional.of(estadio));

            // Act
            Estadio resultado = estadioService.obtener(1);

            // Assert
            assertNotNull(resultado);
            assertEquals(1, resultado.getId());
            assertEquals("Estadio Nacional", resultado.getNombre());
            assertEquals(50000, resultado.getCapacidad());
        }

        @Test
        @DisplayName("Debe retornar null cuando el estadio no existe")
        void obtener_estadioNoExiste() {
            // Arrange
            when(estadioRepository.findById(999)).thenReturn(Optional.empty());

            // Act
            Estadio resultado = estadioService.obtener(999);

            // Assert
            assertNull(resultado);
        }
    }

    // =========================================================================
    // agregar(estadio) — Crear nuevo estadio
    // =========================================================================

    @Nested
    @DisplayName("Agregar nuevo estadio")
    class AgregarTests {

        @Test
        @DisplayName("Debe guardar un nuevo estadio en el repository")
        void agregar_nuevoEstadio() {
            // Arrange
            Estadio nuevoEstadio = crearEstadioVillanueva();
            when(estadioRepository.save(any(Estadio.class))).thenReturn(nuevoEstadio);

            // Act
            estadioService.agregar(nuevoEstadio);

            // Assert
            verify(estadioRepository).save(nuevoEstadio);
        }
    }

    // =========================================================================
    // actualizar(id, nuevoEstadio) — Actualizar estadio existente
    // =========================================================================

    @Nested
    @DisplayName("Actualizar estadio existente")
    class ActualizarTests {

        @Test
        @DisplayName("Debe actualizar todos los campos del estadio cuando existe")
        void actualizar_estadioExiste() {
            // Arrange
            Estadio existente = crearEstadioNacional();
            Estadio actualizado = new Estadio();
            actualizado.setNombre("Estadio Nacional Renovado");
            actualizado.setCiudad("Lima");
            actualizado.setPais("Perú");
            actualizado.setCapacidad(55000);
            actualizado.setHoraAperturaPuertas(3);

            when(estadioRepository.findById(1)).thenReturn(Optional.of(existente));
            when(estadioRepository.save(any(Estadio.class))).thenReturn(existente);

            // Act
            estadioService.actualizar(1, actualizado);

            // Assert
            assertEquals("Estadio Nacional Renovado", existente.getNombre());
            assertEquals(55000, existente.getCapacidad());
            assertEquals(3, existente.getHoraAperturaPuertas());
            verify(estadioRepository).save(existente);
        }

        @Test
        @DisplayName("No debe hacer nada cuando el estadio no existe")
        void actualizar_estadioNoExiste() {
            // Arrange
            Estadio actualizado = crearEstadioNacional();
            when(estadioRepository.findById(999)).thenReturn(Optional.empty());

            // Act
            estadioService.actualizar(999, actualizado);

            // Assert
            verify(estadioRepository, never()).save(any(Estadio.class));
        }
    }

    // =========================================================================
    // eliminar(id) — Eliminar estadio
    // =========================================================================

    @Nested
    @DisplayName("Eliminar estadio")
    class EliminarTests {

        @Test
        @DisplayName("Debe eliminar el estadio por ID")
        void eliminar_estadio() {
            // Arrange
            doNothing().when(estadioRepository).deleteById(1);

            // Act
            estadioService.eliminar(1);

            // Assert
            verify(estadioRepository).deleteById(1);
        }
    }
}
