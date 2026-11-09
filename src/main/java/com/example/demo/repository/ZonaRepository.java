package com.example.demo.repository;

import com.example.demo.model.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, Integer> {
    List<Zona> findByEstadioId(int estadioId);
}