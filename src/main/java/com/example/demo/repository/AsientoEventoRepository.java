package com.example.demo.repository;

import com.example.demo.model.AsientoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AsientoEventoRepository extends JpaRepository<AsientoEvento, Integer> {

    // Obtener todos los asientos de una zona para un evento específico
    @Query("SELECT a FROM AsientoEvento a WHERE a.eventoId = :eventoId AND a.zonaId = :zonaId ORDER BY a.fila, a.columna")
    List<AsientoEvento> findByEventoIdAndZonaId(@Param("eventoId") int eventoId, @Param("zonaId") int zonaId);

    // Buscar un asiento específico por fila y columna
    @Query("SELECT a FROM AsientoEvento a WHERE a.eventoId = :eventoId AND a.zonaId = :zonaId AND a.fila = :fila AND a.columna = :columna")
    AsientoEvento findByEventoIdAndZonaIdAndFilaAndColumna(
            @Param("eventoId") int eventoId,
            @Param("zonaId") int zonaId,
            @Param("fila") String fila,
            @Param("columna") int columna
    );

    // Contar asientos ocupados para un evento y zona
    @Query("SELECT COUNT(a) FROM AsientoEvento a WHERE a.eventoId = :eventoId AND a.zonaId = :zonaId AND a.estado = 'OCUPADO'")
    long countOcupadosByEventoIdAndZonaId(@Param("eventoId") int eventoId, @Param("zonaId") int zonaId);

    // Eliminar todos los asientos de un evento y zona (para regenerar)
    @Query("DELETE FROM AsientoEvento a WHERE a.eventoId = :eventoId AND a.zonaId = :zonaId")
    void deleteByEventoIdAndZonaId(@Param("eventoId") int eventoId, @Param("zonaId") int zonaId);
}
