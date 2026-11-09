package com.example.demo.service;

import com.example.demo.model.Entrada;
import com.example.demo.model.Estadio;
import com.example.demo.model.Evento;
import com.example.demo.repository.EntradaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test del EntradaService siguiendo los principios de TDD (Semana 3).
 *
 * Estos tests validan las reglas de negocio más importantes del sistema de boletería:
 * - Validación de fecha del evento (no vender si ya pasó)
 * - Validación de aforo de zona (no vender si está llena)
 * - Estados de entrada: VIGENTE, PERDIDA, USADA, CANCELADA, REIMPRESA
 *
 * Frase clave para defensa:
 * "Aplicamos principios de TDD para asegurar que las reglas de negocio,
 * como la validación de aforo y fechas, sean robustas y estén libres de errores."
 */
@ExtendWith(MockitoExtension.class)
class EntradaServiceTest {

    @Mock
    private EntradaRepository entradaRepository;

    @Mock
    private ZonaService zonaService;

    @Mock
    private EventoService eventoService;

    @Mock
    private EstadioService estadioService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EntradaService entradaService;

    // ------------------------------------------------------------------
    // Datos de prueba reutilizables
    // ------------------------------------------------------------------

    private Entrada crearEntrada(int eventoId, int zonaId) {
        Entrada entrada = new Entrada();
        entrada.setEventoId(eventoId);
        entrada.setZonaId(zonaId);
        entrada.setNombreUsuario("Juan Pérez");
        entrada.setDocumentoUsuario("12345678");
        entrada.setTipoEntrega("E-TICKET");
        return entrada;
    }

    private Evento crearEventoFuturo() {
        Evento evento = new Evento();
        evento.setId(1);
        evento.setNombreEvento("Final Copa Perú");
        evento.setFecha("2027-12-31");
        evento.setHora("20:00:00");
        evento.setEstadioId(1);
        return evento;
    }

    private Evento crearEventoPasado() {
        Evento evento = new Evento();
        evento.setId(2);
        evento.setNombreEvento("Concierto Pasado");
        evento.setFecha("2020-01-01");
        evento.setHora("18:00:00");
        evento.setEstadioId(1);
        return evento;
    }

    private Estadio crearEstadio() {
        Estadio estadio = new Estadio();
        estadio.setId(1);
        estadio.setNombre("Estadio Nacional");
        estadio.setHoraAperturaPuertas(2);
        return estadio;
    }

    // =========================================================================
    // REGla 1: comprarEntrada() — Validación de fecha y aforo
    // =========================================================================

    @Nested
    @DisplayName("REGla 1: Comprar entrada")
    class ComprarEntradaTests {

        @Test
        @DisplayName("Debe comprar exitosamente cuando el evento es futuro y hay aforo")
        void comprarEntrada_exitoso() {
            // Arrange
            Entrada entrada = crearEntrada(1, 1);
            Evento eventoFuturo = crearEventoFuturo();

            when(eventoService.obtener(1)).thenReturn(eventoFuturo);
            when(zonaService.hayDisponibilidad(1, 1)).thenReturn(true);
            when(entradaRepository.save(any(Entrada.class))).thenAnswer(invocation -> {
                Entrada saved = invocation.getArgument(0);
                saved.setId(100); // Simula el ID generado por la base de datos
                return saved;
            });
            doNothing().when(zonaService).ocuparAsiento(1, 1);
            doNothing().when(emailService).enviarConfirmacionCompra(anyString(), anyString(), anyString(), anyInt());

            // Act
            String resultado = entradaService.comprarEntrada(entrada);

            // Assert
            assertEquals("OK: entrada comprada correctamente", resultado);
            assertEquals("VIGENTE", entrada.getEstado());
            verify(entradaRepository).save(entrada);
            verify(zonaService).ocuparAsiento(1, 1);
            verify(emailService).enviarConfirmacionCompra(eq("prueba@email.com"), eq("Juan Pérez"),
                    eq("Final Copa Perú"), eq(100));
        }

        @Test
        @DisplayName("Debe rechazar compra si el evento no existe")
        void comprarEntrada_eventoNoExiste() {
            // Arrange
            Entrada entrada = crearEntrada(999, 1);
            when(eventoService.obtener(999)).thenReturn(null);

            // Act
            String resultado = entradaService.comprarEntrada(entrada);

            // Assert
            assertTrue(resultado.startsWith("ERROR:"));
            assertTrue(resultado.contains("evento no existe"));
            verify(entradaRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe rechazar compra si el evento ya pasó")
        void comprarEntrada_eventoYaPaso() {
            // Arrange
            Entrada entrada = crearEntrada(2, 1);
            when(eventoService.obtener(2)).thenReturn(crearEventoPasado());

            // Act
            String resultado = entradaService.comprarEntrada(entrada);

            // Assert
            assertTrue(resultado.startsWith("ERROR:"));
            assertTrue(resultado.contains("evento ya pasó"));
            verify(entradaRepository, never()).save(any());
            verify(zonaService, never()).ocuparAsiento(anyInt(), anyInt());
        }

        @Test
        @DisplayName("Debe rechazar compra si la zona no tiene aforo disponible")
        void comprarEntrada_zonaLlena() {
            // Arrange
            Entrada entrada = crearEntrada(1, 1);
            when(eventoService.obtener(1)).thenReturn(crearEventoFuturo());
            when(zonaService.hayDisponibilidad(1, 1)).thenReturn(false);

            // Act
            String resultado = entradaService.comprarEntrada(entrada);

            // Assert
            assertTrue(resultado.startsWith("ERROR:"));
            assertTrue(resultado.contains("aforo disponible"));
            verify(entradaRepository, never()).save(any());
        }
    }

    // =========================================================================
    // REGla 2: reportarPerdida() — Una entrada perdida NO se repone
    // =========================================================================

    @Nested
    @DisplayName("REGla 2: Reportar pérdida")
    class ReportarPerdidaTests {

        @Test
        @DisplayName("Debe marcar la entrada como PERDIDA")
        void reportarPerdida_exitoso() {
            // Arrange
            Entrada entradaExistente = new Entrada(10, 1, 1, "Juan", "12345678", "VIGENTE", "E-TICKET");
            when(entradaRepository.findById(10)).thenReturn(java.util.Optional.of(entradaExistente));
            when(entradaRepository.save(any(Entrada.class))).thenReturn(entradaExistente);

            // Act
            String resultado = entradaService.reportarPerdida(10);

            // Assert
            assertEquals("La entrada fue marcada como PERDIDA. No se repone ni se reembolsa.", resultado);
            assertEquals("PERDIDA", entradaExistente.getEstado());
            verify(entradaRepository).save(entradaExistente);
        }

        @Test
        @DisplayName("Debe retornar error si la entrada no existe")
        void reportarPerdida_noExiste() {
            // Arrange
            when(entradaRepository.findById(999)).thenReturn(java.util.Optional.empty());

            // Act
            String resultado = entradaService.reportarPerdida(999);

            // Assert
            assertTrue(resultado.startsWith("ERROR:"));
            assertTrue(resultado.contains("entrada no existe"));
        }
    }

    // =========================================================================
    // REGla 3: solicitarReimpresion() — Solo si NO está perdida
    // =========================================================================

    @Nested
    @DisplayName("REGla 3: Solicitar reimpresión")
    class SolicitarReimpresionTests {

        @Test
        @DisplayName("Debe permitir reimpresión si la entrada NO está perdida")
        void solicitarReimpresion_exitoso() {
            // Arrange
            Entrada entrada = new Entrada(10, 1, 1, "Juan", "12345678", "VIGENTE", "FISICA");
            when(entradaRepository.findById(10)).thenReturn(java.util.Optional.of(entrada));
            when(entradaRepository.save(any(Entrada.class))).thenReturn(entrada);

            // Act
            String resultado = entradaService.solicitarReimpresion(10);

            // Assert
            assertTrue(resultado.contains("Reimpresion generada"));
            assertTrue(resultado.contains("S/10"));
            assertEquals("REIMPRESA", entrada.getEstado());
        }

        @Test
        @DisplayName("Debe rechazar reimpresión si la entrada está PERDIDA")
        void solicitarReimpresion_entradaPerdida() {
            // Arrange
            Entrada entrada = new Entrada(10, 1, 1, "Juan", "12345678", "PERDIDA", "FISICA");
            when(entradaRepository.findById(10)).thenReturn(java.util.Optional.of(entrada));

            // Act
            String resultado = entradaService.solicitarReimpresion(10);

            // Assert
            assertTrue(resultado.startsWith("ERROR:"));
            assertTrue(resultado.contains("PERDIDA"));
        }
    }

    // =========================================================================
    // REGla 5: marcarUsada() — Cuando la persona entra físicamente
    // =========================================================================

    @Nested
    @DisplayName("REGla 5: Marcar entrada como usada")
    class MarcarUsadaTests {

        @Test
        @DisplayName("Debe marcar la entrada como USADA")
        void marcarUsada_exitoso() {
            // Arrange
            Entrada entrada = new Entrada(10, 1, 1, "Juan", "12345678", "VIGENTE", "E-TICKET");
            when(entradaRepository.findById(10)).thenReturn(java.util.Optional.of(entrada));
            when(entradaRepository.save(any(Entrada.class))).thenReturn(entrada);

            // Act
            String resultado = entradaService.marcarUsada(10);

            // Assert
            assertTrue(resultado.contains("USADA"));
            assertEquals("USADA", entrada.getEstado());
        }

        @Test
        @DisplayName("Debe rechazar si la entrada está PERDIDA")
        void marcarUsada_entradaPerdida() {
            // Arrange
            Entrada entrada = new Entrada(10, 1, 1, "Juan", "12345678", "PERDIDA", "E-TICKET");
            when(entradaRepository.findById(10)).thenReturn(java.util.Optional.of(entrada));

            // Act
            String resultado = entradaService.marcarUsada(10);

            // Assert
            assertTrue(resultado.startsWith("ERROR:"));
        }

        @Test
        @DisplayName("Debe rechazar si la entrada está CANCELADA")
        void marcarUsada_entradaCancelada() {
            // Arrange
            Entrada entrada = new Entrada(10, 1, 1, "Juan", "12345678", "CANCELADA", "E-TICKET");
            when(entradaRepository.findById(10)).thenReturn(java.util.Optional.of(entrada));

            // Act
            String resultado = entradaService.marcarUsada(10);

            // Assert
            assertTrue(resultado.startsWith("ERROR:"));
        }
    }

    // =========================================================================
    // REGla 6: cancelar() — Libera el cupo de la zona
    // =========================================================================

    @Nested
    @DisplayName("REGla 6: Cancelar entrada")
    class CancelarEntradaTests {

        @Test
        @DisplayName("Debe cancelar la entrada y liberar el asiento")
        void cancelar_exitoso() {
            // Arrange
            Entrada entrada = new Entrada(10, 1, 1, "Juan", "12345678", "VIGENTE", "E-TICKET");
            when(entradaRepository.findById(10)).thenReturn(java.util.Optional.of(entrada));
            when(entradaRepository.save(any(Entrada.class))).thenReturn(entrada);
            doNothing().when(zonaService).liberarAsiento(1, 1);

            // Act
            String resultado = entradaService.cancelar(10);

            // Assert
            assertTrue(resultado.contains("CANCELADA"));
            assertEquals("CANCELADA", entrada.getEstado());
            verify(zonaService).liberarAsiento(1, 1);
        }

        @Test
        @DisplayName("Debe rechazar cancelación si ya está CANCELADA")
        void cancelar_yaCancelada() {
            // Arrange
            Entrada entrada = new Entrada(10, 1, 1, "Juan", "12345678", "CANCELADA", "E-TICKET");
            when(entradaRepository.findById(10)).thenReturn(java.util.Optional.of(entrada));

            // Act
            String resultado = entradaService.cancelar(10);

            // Assert
            assertTrue(resultado.startsWith("ERROR:"));
            assertTrue(resultado.contains("ya estaba cancelada"));
        }

        @Test
        @DisplayName("Debe rechazar cancelación si ya fue USADA")
        void cancelar_yaUsada() {
            // Arrange
            Entrada entrada = new Entrada(10, 1, 1, "Juan", "12345678", "USADA", "E-TICKET");
            when(entradaRepository.findById(10)).thenReturn(java.util.Optional.of(entrada));

            // Act
            String resultado = entradaService.cancelar(10);

            // Assert
            assertTrue(resultado.startsWith("ERROR:"));
            assertTrue(resultado.contains("USADA"));
            verify(zonaService, never()).liberarAsiento(anyInt(), anyInt());
        }
    }
}
