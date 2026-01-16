package com.example.demo.services;

import com.example.demo.entities.CategoriaEsquema;
import com.example.demo.repositories.CategoriaEsquemaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaEsquemaService
{
    private final CategoriaEsquemaRepository repo;

    public CategoriaEsquemaService(CategoriaEsquemaRepository repo) {
        this.repo = repo;
    }

    public CategoriaEsquema findById(Long id){
        return repo.findById(id).orElse(null);
    }

    public List<CategoriaEsquema> findAll() {
        return repo.findAll();
    }

    public CategoriaEsquema save(CategoriaEsquema c) {
        return repo.save(c);
    }

    public List<CategoriaEsquema> findByCategoria(String categoria) {
        return repo.findByCategoria(categoria);
    }

    public boolean existePorCategoria(String categoria) {
        return repo.existsByCategoria(categoria);
    }
}
