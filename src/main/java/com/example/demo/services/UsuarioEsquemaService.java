package com.example.demo.services;


import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.repositories.UsuarioEsquemaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsuarioEsquemaService {

    private final UsuarioEsquemaRepository repo;

    public UsuarioEsquemaService(UsuarioEsquemaRepository repo) {
        this.repo = repo;
    }

    public UsuarioEsquema findById(Long id){
        return repo.findById(id).orElse(null);
    }

    public List<UsuarioEsquema> findAll() {
        return repo.findAll();
    }

    public UsuarioEsquema save(UsuarioEsquema u) {
        return repo.save(u);
    }

    public List<UsuarioEsquema> buscarPorNombre(String nombre) {
        return repo.findByNombre(nombre);
    }

    public boolean existePorEmail(String email) {
        return repo.existsByEmail(email);
    }

    public List<UsuarioEsquema> buscarPorEmail(String email) {
        return repo.findByEmail(email);
    }
}