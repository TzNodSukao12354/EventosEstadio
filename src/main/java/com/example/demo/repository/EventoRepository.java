package com.example.demo.repository;

import com.example.demo.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Integer> {

    // Forma automática de Spring Data JPA
    List<Evento> findByFase(String fase);

    // Consulta JPQL con parámetro nombrado (:fase)
    @Query("SELECT ev FROM Evento ev WHERE ev.fase = :fase")
    List<Evento> buscarEventosPorFaseJPQL(@Param("fase") String fase);
}