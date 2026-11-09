package com.example.demo.repository;

import com.example.demo.model.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EstadioRepository extends JpaRepository<Estadio, Integer> {

    // Consulta JPQL con parámetro nombrado (:capacidad)
    @Query("SELECT est FROM Estadio est WHERE est.capacidad > :capacidad")
    List<Estadio> buscarEstadiosGrandes(@Param("capacidad") int capacidad);
}