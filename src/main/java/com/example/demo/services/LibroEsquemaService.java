package com.example.demo.services;

import com.example.demo.entities.LibroEsquema;
import com.example.demo.repositories.LibroEsquemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LibroEsquemaService {
    @Autowired
    private LibroEsquemaRepository repo;

    public LibroEsquemaService(LibroEsquemaRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<LibroEsquema> findAll() {
        List<LibroEsquema> libros = repo.findAll();
        libros.forEach(l -> l.getCategorias().size());
        return libros;
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    public LibroEsquema findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public List<LibroEsquema> buscarPorEmailDeUsuario(String email) {
        return repo.findByUsuarioEmail(email);
    }

    public List<LibroEsquema> buscarPorTitulo(String titulo) {
        return repo.findByTitulo(titulo);
    }

    public List<LibroEsquema> buscarPorAutor(String autor) {
        return repo.findByAutor(autor);
    }

    public List<LibroEsquema> buscarPorImagenUrl(String imagenUrl) {
        return repo.findByImagenUrl(imagenUrl);
    }

    @org.springframework.transaction.annotation.Transactional
    public void save(LibroEsquema libro) {
        repo.save(libro);
    }
}
