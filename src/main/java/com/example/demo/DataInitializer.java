package com.example.demo;

import com.example.demo.entities.CategoriaEsquema;
import com.example.demo.entities.LibroEsquema;
import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.services.CategoriaEsquemaService;
import com.example.demo.services.LibroEsquemaService;
import com.example.demo.services.UsuarioEsquemaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioEsquemaService usuarioService;
    private final CategoriaEsquemaService categoriaService;
    private final LibroEsquemaService libroService;

    public DataInitializer(UsuarioEsquemaService usuarioService,
                           CategoriaEsquemaService categoriaService,
                           LibroEsquemaService libroService) {
        this.usuarioService = usuarioService;
        this.categoriaService = categoriaService;
        this.libroService = libroService;
    }

    @Override
    public void run(String... args) throws Exception {

        // --- 1. USUARIOS ---
        crearUsuarioSiNoExiste("jesus", "jesus@example.com", "1234");
        crearUsuarioSiNoExiste("paco", "paco@example.com", "1234");

        // --- 2. CATEGORÍAS ---
        String[] nombresCategorias = {
                "Novela", "Programación", "Terror", "Ciencia Ficción",
                "Historia", "Fantasía", "Aventura", "Filosofía"
        };

        for (String nombre : nombresCategorias) {
            if (!categoriaService.existePorCategoria(nombre)) {
                CategoriaEsquema cat = new CategoriaEsquema();
                cat.setCategoria(nombre);
                categoriaService.save(cat);
            }
        }

        // --- 3. LIBROS PÚBLICOS (Sin dueño asignado) ---
        if (libroService.findAll().isEmpty()) {
            // Obtenemos algunas categorías para asignar
            CategoriaEsquema novela = categoriaService.findByCategoria("Novela").get(0);
            CategoriaEsquema terror = categoriaService.findByCategoria("Terror").get(0);
            CategoriaEsquema cienciaFiccion = categoriaService.findByCategoria("Ciencia Ficción").get(0);
            CategoriaEsquema prog = categoriaService.findByCategoria("Programación").get(0);
            CategoriaEsquema fantasia = categoriaService.findByCategoria("Fantasía").get(0);

            // Lista de libros para inicializar la biblioteca pública
            crearLibroPublico("Don Quijote de la Mancha", "Miguel de Cervantes", novela);
            crearLibroPublico("Drácula", "Bram Stoker", terror);
            crearLibroPublico("1984", "George Orwell", cienciaFiccion);
            crearLibroPublico("El Hobbit", "J.R.R. Tolkien", fantasia);
            crearLibroPublico("Clean Code", "Robert C. Martin", prog);
            crearLibroPublico("Fundación", "Isaac Asimov", cienciaFiccion);
            crearLibroPublico("It", "Stephen King", terror);
            crearLibroPublico("Crónica de una muerte anunciada", "Gabriel García Márquez", novela);
            crearLibroPublico("El resplandor", "Stephen King", terror);
            crearLibroPublico("Java Effective", "Joshua Bloch", prog);
            crearLibroPublico("Frankenstein", "Mary Shelley", terror);
            crearLibroPublico("Dune", "Frank Herbert", cienciaFiccion);
        }

        System.out.println("¡Base de datos inicializada con éxito!");
    }

    // Método auxiliar para no repetir código de usuarios
    private void crearUsuarioSiNoExiste(String nombre, String email, String pass) {
        if (usuarioService.buscarPorNombre(nombre).isEmpty()) {
            UsuarioEsquema u = new UsuarioEsquema();
            u.setNombre(nombre);
            u.setEmail(email);
            u.setContraseña(pass);
            usuarioService.save(u);
        }
    }

    // Método auxiliar para crear libros rápidamente
    private void crearLibroPublico(String titulo, String autor, CategoriaEsquema categoria) {
        LibroEsquema libro = new LibroEsquema();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setUsuario(null); // Público
        libro.setCategorias(new ArrayList<>(List.of(categoria)));
        libroService.save(libro);
    }
}