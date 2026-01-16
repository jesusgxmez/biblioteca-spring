package com.example.demo.services;

import com.example.demo.entities.PerfilEsquema;
import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.repositories.PerfilEsquemaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilEsquemaService {

    private final PerfilEsquemaRepository repo;

    public PerfilEsquemaService(PerfilEsquemaRepository repo) {
        this.repo = repo;
    }

    public PerfilEsquema findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public List<PerfilEsquema> findAll() {
        return repo.findAll();
    }

    public PerfilEsquema save(PerfilEsquema u) {
        return repo.save(u);
    }

    public List<PerfilEsquema> buscarPorBio(String bio) {
        return repo.findByBio(bio);
    }
}
