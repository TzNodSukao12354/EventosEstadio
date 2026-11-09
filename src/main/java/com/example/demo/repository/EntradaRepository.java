package com.example.demo.repository;

import com.example.demo.model.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, Integer> {

    // Consulta JPQL con parámetro posicional (?1)
    @Query("SELECT e FROM Entrada e WHERE e.documentoUsuario = ?1")
    List<Entrada> buscarEntradasPorDocumento(String documento);
}