package com.example.demo.repositories;

import com.example.demo.entities.PerfilEsquema;
import com.example.demo.entities.UsuarioEsquema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilEsquemaRepository extends JpaRepository<PerfilEsquema, Long> {
    List<PerfilEsquema> findByBio(String bio);
}
