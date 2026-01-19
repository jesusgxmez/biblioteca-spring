package com.example.demo.repositories;

import com.example.demo.entities.LibroEsquema;
import com.example.demo.entities.PerfilEsquema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroEsquemaRepository extends JpaRepository<LibroEsquema, Long> {
    List<LibroEsquema> findByTitulo(String titulo);
    List<LibroEsquema> findByAutor(String autor);
    List<LibroEsquema> findByImagenUrl(String imagenUrl);
    List<LibroEsquema> findByUsuarioEmail(String email);
}
