package com.example.demo.service;

import com.example.demo.model.Entrada;
import com.example.demo.model.Estadio;
import com.example.demo.model.Evento;
import com.example.demo.repository.EntradaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EntradaService {

    private static final Logger logger = LoggerFactory.getLogger(EntradaService.class);

    private final EntradaRepository entradaRepository;
    private final ZonaService zonaService;
    private final EventoService eventoService;
    private final EstadioService estadioService;
    private final EmailService emailService;

    // 1. UN SOLO constructor con TODOS los parámetros
    @Autowired
    public EntradaService(EntradaRepository entradaRepository, ZonaService zonaService,
                          EventoService eventoService, EstadioService estadioService,
                          EmailService emailService) {
        this.entradaRepository = entradaRepository;
        this.zonaService = zonaService;
        this.eventoService = eventoService;
        this.estadioService = estadioService;
        this.emailService = emailService;
    }

    public List<Entrada> todos() {
        return entradaRepository.findAll();
    }

    public Entrada obtener(int id) {
        Optional<Entrada> entrada = entradaRepository.findById(id);
        return entrada.orElse(null);
    }

    // =========================================================================
    // REGLA DE NEGOCIO 1: Comprar entrada con validación de fecha y aforo
    // =========================================================================
    @Transactional // <--- ¡IMPORTANTE! Va AQUÍ, en el método real
    public String comprarEntrada(Entrada entrada) {

        // 1. Verificar que el evento existe
        Evento evento = eventoService.obtener(entrada.getEventoId());
        if (evento == null) {
            logger.warn("Intento de compra fallido: evento {} no existe", entrada.getEventoId());
            return "ERROR: el evento no existe";
        }

        // 2. VALIDACIÓN DE TIEMPO: Verificar que el evento no haya pasado
        try {
            LocalDateTime fechaEvento = LocalDateTime.parse(evento.getFecha() + "T" + evento.getHora());
            LocalDateTime ahora = LocalDateTime.now();

            if (ahora.isAfter(fechaEvento)) {
                logger.warn("Intento de compra bloqueado: evento {} ya pasó", entrada.getEventoId());
                return "ERROR: el evento ya pasó. No se pueden comprar entradas";
            }
        } catch (Exception e) {
            logger.error("Error al validar fecha del evento {}: {}", entrada.getEventoId(), e.getMessage());
            return "ERROR: formato de fecha/hora inválido";
        }

        // 3. Verificar disponibilidad de la zona PARA ESTE EVENTO ESPECÍFICO
        if (!zonaService.hayDisponibilidad(entrada.getEventoId(), entrada.getZonaId())) {
            logger.warn("Intento de compra bloqueado: zona {} llena para evento {}", entrada.getZonaId(), entrada.getEventoId());
            return "ERROR: la zona ya no tiene aforo disponible para este evento";
        }

        // 4. Guardar la entrada y obtenerla con su ID generado
        entrada.setEstado("VIGENTE");
        Entrada entradaGuardada = entradaRepository.save(entrada);

        // 5. Ocupar asiento en el "cuaderno" de este evento
        zonaService.ocuparAsiento(entrada.getEventoId(), entrada.getZonaId());

        // LOG: Registrar la compra exitosa
        logger.info("✅ Entrada comprada exitosamente - Evento: {}, Zona: {}, Usuario: {}, Documento: {}",
                entrada.getEventoId(), entrada.getZonaId(), entrada.getNombreUsuario(), entrada.getDocumentoUsuario());

        // 6. Enviar confirmación por correo (E-TICKET)
        emailService.enviarConfirmacionCompra(
                "prueba@email.com", // En el futuro será el correo real del usuario desde el Frontend
                entrada.getNombreUsuario(),
                evento.getNombreEvento(),
                entradaGuardada.getId()
        );

        return "OK: entrada comprada correctamente";
    }

    // =========================================================================
    // REGLA DE NEGOCIO 2: una entrada perdida NO se repone ni se reembolsa
    // =========================================================================
    public String reportarPerdida(int id) {
        Entrada entrada = obtener(id);
        if (entrada == null) {
            logger.warn("Intento de reportar pérdida: entrada {} no existe", id);
            return "ERROR: la entrada no existe";
        }
        entrada.setEstado("PERDIDA");
        entradaRepository.save(entrada);
        logger.info("📝 Entrada {} reportada como PERDIDA", id);
        return "La entrada fue marcada como PERDIDA. No se repone ni se reembolsa.";
    }

    // =========================================================================
    // REGLA DE NEGOCIO 3: solo se puede reimprimir si está dañada, NO si está perdida
    // =========================================================================
    public String solicitarReimpresion(int id) {
        Entrada entrada = obtener(id);
        if (entrada == null) {
            logger.warn("Intento de reimpresión: entrada {} no existe", id);
            return "ERROR: la entrada no existe";
        }
        if (entrada.getEstado().equals("PERDIDA")) {
            logger.warn("Intento de reimpresión bloqueado: entrada {} está PERDIDA", id);
            return "ERROR: una entrada PERDIDA no se puede reimprimir";
        }
        entrada.setEstado("REIMPRESA");
        entradaRepository.save(entrada);
        logger.info("🖨️ Entrada {} reimpresa. Costo adicional: S/10", id);
        return "Reimpresion generada. Costo adicional: S/10";
    }

    // =========================================================================
    // REGLA DE NEGOCIO 4: valida si la hora actual todavía permite el ingreso
    // =========================================================================
    public String validarIngreso(int id) {
        Entrada entrada = obtener(id);
        if (entrada == null) {
            logger.warn("Intento de validar ingreso: entrada {} no existe", id);
            return "ERROR: la entrada no existe";
        }
        Evento evento = eventoService.obtener(entrada.getEventoId());
        if (evento == null) {
            logger.warn("Intento de validar ingreso: evento {} no existe", entrada.getEventoId());
            return "ERROR: el evento no existe";
        }
        Estadio estadio = estadioService.obtener(evento.getEstadioId());
        if (estadio == null) {
            logger.warn("Intento de validar ingreso: estadio {} no existe", evento.getEstadioId());
            return "ERROR: el estadio no existe";
        }

        LocalDateTime horaEvento = LocalDateTime.parse(evento.getFecha() + "T" + evento.getHora());
        LocalDateTime horaAperturaPuertas = horaEvento.minusHours(estadio.getHoraAperturaPuertas());
        LocalDateTime ahora = LocalDateTime.now();

        if (ahora.isBefore(horaAperturaPuertas)) {
            logger.info("Ingreso denegado: entrada {}. Puertas aún no abiertas", id);
            return "Todavia no abren las puertas. Apertura: " + horaAperturaPuertas;
        }
        if (ahora.isAfter(horaEvento)) {
            logger.info("Ingreso denegado: entrada {}. Evento ya empezó", id);
            return "El evento ya empezo. El estadio puede negar el ingreso.";
        }

        // Nota: Aquí validamos que no esté USADA o CANCELADA también
        if (entrada.getEstado().equals("USADA") || entrada.getEstado().equals("CANCELADA")) {
            return "ERROR: Esta entrada ya fue usada o está cancelada.";
        }

        logger.info("✅ Ingreso permitido: entrada {} en estadio {}", id, estadio.getNombre());
        return "Ingreso permitido. Puertas de " + estadio.getNombre() + " abiertas desde: " + horaAperturaPuertas;
    }

    // =========================================================================
    // REGLA DE NEGOCIO 5: cuando la persona ya entró físicamente al estadio
    // =========================================================================
    public String marcarUsada(int id) {
        Entrada entrada = obtener(id);
        if (entrada == null) {
            logger.warn("Intento de marcar como usada: entrada {} no existe", id);
            return "ERROR: la entrada no existe";
        }
        if (entrada.getEstado().equals("PERDIDA") || entrada.getEstado().equals("CANCELADA")) {
            logger.warn("Intento de marcar como usada: entrada {} está en estado inválido", id);
            return "ERROR: una entrada PERDIDA o CANCELADA no puede marcarse como usada";
        }
        entrada.setEstado("USADA");
        entradaRepository.save(entrada);
        logger.info("🎟️ Entrada {} marcada como USADA. Persona ingresó al estadio", id);
        return "Entrada marcada como USADA. La persona ya ingreso al estadio.";
    }

    // =========================================================================
    // REGLA DE NEGOCIO 6: "Eliminar" una entrada cambia estado a CANCELADA y libera cupo
    // =========================================================================
    @Transactional
    public String cancelar(int id) {
        Entrada entrada = obtener(id);
        if (entrada == null) {
            logger.warn("Intento de cancelación: entrada {} no existe", id);
            return "ERROR: la entrada no existe";
        }
        if (entrada.getEstado().equals("CANCELADA")) {
            logger.warn("Intento de cancelación: entrada {} ya estaba cancelada", id);
            return "ERROR: la entrada ya estaba cancelada";
        }
        if (entrada.getEstado().equals("USADA")) {
            return "ERROR: no se puede cancelar una entrada que ya fue USADA";
        }

        entrada.setEstado("CANCELADA");
        entradaRepository.save(entrada);

        // Liberamos el asiento para que otro pueda comprarlo
        zonaService.liberarAsiento(entrada.getEventoId(), entrada.getZonaId());

        logger.info("❌ Entrada {} CANCELADA. Cupo liberado para evento {}", id, entrada.getEventoId());
        return "Entrada CANCELADA. El cupo de la zona fue liberado para otra persona.";
    }
}