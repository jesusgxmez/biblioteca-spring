package com.example.demo.repositories;


import com.example.demo.entities.UsuarioEsquema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioEsquemaRepository extends JpaRepository<UsuarioEsquema, Long> {
    List<UsuarioEsquema> findByNombre(String nombre);
    List<UsuarioEsquema> findByEmail(String email);
    boolean existsByEmail(String email);
}