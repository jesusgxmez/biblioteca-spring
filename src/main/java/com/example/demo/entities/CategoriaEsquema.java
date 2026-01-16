package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class CategoriaEsquema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoria;

    @ManyToMany(mappedBy = "categorias")
    private List<LibroEsquema> libros = new ArrayList<>();
}
