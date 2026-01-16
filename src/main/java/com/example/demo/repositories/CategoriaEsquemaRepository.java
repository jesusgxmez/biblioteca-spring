package com.example.demo.repositories;

import com.example.demo.entities.CategoriaEsquema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaEsquemaRepository extends JpaRepository<CategoriaEsquema, Long>
{
    List<CategoriaEsquema> findByCategoria(String categoria);
    boolean existsByCategoria(String categoria);
}
