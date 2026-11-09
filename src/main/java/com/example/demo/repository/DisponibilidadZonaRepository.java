package com.example.demo.repository;

import com.example.demo.model.DisponibilidadZona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock; // <--- AGREGAR ESTO
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType; // <--- AGREGAR ESTO
import java.util.Optional;

@Repository
public interface DisponibilidadZonaRepository extends JpaRepository<DisponibilidadZona, Integer> {

    // Buscar la disponibilidad de una zona para un evento específico CON CANDADO
    @Lock(LockModeType.PESSIMISTIC_WRITE) // <--- ESTO ES EL CANDADO
    Optional<DisponibilidadZona> findByEventoIdAndZonaId(int eventoId, int zonaId);
}