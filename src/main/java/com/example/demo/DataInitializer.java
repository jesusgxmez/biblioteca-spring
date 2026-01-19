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

        // --- 3. LIBROS PÚBLICOS (Con Portadas Reales) ---
        if (libroService.findAll().isEmpty()) {
            CategoriaEsquema novela = categoriaService.findByCategoria("Novela").get(0);
            CategoriaEsquema terror = categoriaService.findByCategoria("Terror").get(0);
            CategoriaEsquema cienciaFiccion = categoriaService.findByCategoria("Ciencia Ficción").get(0);
            CategoriaEsquema prog = categoriaService.findByCategoria("Programación").get(0);
            CategoriaEsquema fantasia = categoriaService.findByCategoria("Fantasía").get(0);

            // Inicialización con URLs de imágenes
            crearLibroPublico("Don Quijote de la Mancha", "Miguel de Cervantes",
                    "https://algareditorial.com/29329-thickbox_default/don-quijote.jpg", novela);

            crearLibroPublico("Drácula", "Bram Stoker",
                    "https://m.media-amazon.com/images/I/61ZMLiTYxDL._AC_UF1000,1000_QL80_.jpg", terror);

            crearLibroPublico("1984", "George Orwell",
                    "https://m.media-amazon.com/images/I/91SZSW8qSsL.jpg", cienciaFiccion);

            crearLibroPublico("El Hobbit", "J.R.R. Tolkien",
                    "https://m.media-amazon.com/images/I/71qpt1yWfbL._AC_UF1000,1000_QL80_.jpg", fantasia);

            crearLibroPublico("Clean Code", "Robert C. Martin",
                    "https://m.media-amazon.com/images/I/41xShlnTZTL.jpg", prog);

            crearLibroPublico("Fundación", "Isaac Asimov",
                    "https://m.media-amazon.com/images/I/410RP6PK0TL._AC_UF1000,1000_QL80_.jpg", cienciaFiccion);

            crearLibroPublico("It", "Stephen King",
                    "https://m.media-amazon.com/images/I/71BwqlcZyVL._AC_UF1000,1000_QL80_.jpg", terror);

            crearLibroPublico("Crónica de una muerte anunciada", "Gabriel García Márquez",
                    "https://wmagazin.com/wp-content/uploads/2021/04/Cronicadeunamuerteanunciada-Colombia-1-633x1024.jpg", novela);

            crearLibroPublico("El resplandor", "Stephen King",
                    "https://m.media-amazon.com/images/I/91n8sen+w1L.jpg", terror);

            crearLibroPublico("Java Effective", "Joshua Bloch",
                    "https://m.media-amazon.com/images/I/71JAVv3TW4L.jpg", prog);

            crearLibroPublico("Frankenstein", "Mary Shelley",
                    "https://m.media-amazon.com/images/I/71Vr8pSwRqL.jpg", terror);

            crearLibroPublico("The Pragmatic Programmer", "Andrew Hunt & David Thomas", "https://m.media-amazon.com/images/I/71f1jieYHNL._AC_UF1000,1000_QL80_.jpg", prog);

            crearLibroPublico("Introduction to Algorithms", "Thomas H. Cormen", "https://m.media-amazon.com/images/I/616c+ys7RBL._UF1000,1000_QL80_.jpg", prog);

            crearLibroPublico("Structure and Interpretation of Computer Programs", "Harold Abelson", "https://m.media-amazon.com/images/I/71xZu1kgXJL._UF1000,1000_QL80_.jpg", prog);

            crearLibroPublico("Design Patterns", "Erich Gamma", "https://m.media-amazon.com/images/I/81IGFC6oFmL.jpg", prog);

            crearLibroPublico("Dune", "Frank Herbert",
                    "https://www.estudioenescarlata.com/media/img/portadas/_visd_0001JPG01PBE.jpg", cienciaFiccion);
        }

        System.out.println("¡Base de datos inicializada con éxito!");
    }

    private void crearUsuarioSiNoExiste(String nombre, String email, String pass) {
        if (usuarioService.buscarPorNombre(nombre).isEmpty()) {
            UsuarioEsquema u = new UsuarioEsquema();
            u.setNombre(nombre);
            u.setEmail(email);
            u.setContraseña(pass);
            usuarioService.save(u);
        }
    }

    // MÉTODO MODIFICADO PARA INCLUIR IMAGEN
    private void crearLibroPublico(String titulo, String autor, String urlImagen, CategoriaEsquema categoria) {
        LibroEsquema libro = new LibroEsquema();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setImagenUrl(urlImagen); // Se guarda en el campo que antes era editora
        libro.setUsuario(null);
        libro.setCategorias(new ArrayList<>(List.of(categoria)));
        libroService.save(libro);
    }
}