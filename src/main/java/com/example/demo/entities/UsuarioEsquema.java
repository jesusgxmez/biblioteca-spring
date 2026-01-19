package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class UsuarioEsquema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String email;
    private String contraseña;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private PerfilEsquema perfil;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LibroEsquema> libros = new ArrayList<>();

    @Override
    public String toString() {
        return "UsuarioEsquema{id=" + id + ", nombre='" + nombre + "'}";
    }
}