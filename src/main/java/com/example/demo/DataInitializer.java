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

        crearUsuarioSiNoExiste("jesus", "jesus@example.com", "1234");
        crearUsuarioSiNoExiste("paco", "paco@example.com", "1234");

        String[] nombresCategorias = {
                "Novela", "Programación", "Terror", "Ciencia Ficción",
                "Historia", "Fantasía", "Aventura", "Filosofía", "Superación Personal"
        };

        for (String nombre : nombresCategorias) {
            if (!categoriaService.existePorCategoria(nombre)) {
                CategoriaEsquema cat = new CategoriaEsquema();
                cat.setCategoria(nombre);
                categoriaService.save(cat);
            }
        }

        if (libroService.findAll().isEmpty()) {
            CategoriaEsquema novela = categoriaService.findByCategoria("Novela").get(0);
            CategoriaEsquema terror = categoriaService.findByCategoria("Terror").get(0);
            CategoriaEsquema cienciaFiccion = categoriaService.findByCategoria("Ciencia Ficción").get(0);
            CategoriaEsquema prog = categoriaService.findByCategoria("Programación").get(0);
            CategoriaEsquema fantasia = categoriaService.findByCategoria("Fantasía").get(0);
            CategoriaEsquema superacionPersonal = categoriaService.findByCategoria("Superación Personal").get(0);

            // Lista de libros con páginas estimadas según ediciones populares
            crearLibroPublico("Don Quijote de la Mancha", "Miguel de Cervantes",
                    "https://algareditorial.com/29329-thickbox_default/don-quijote.jpg", novela, 1032);

            crearLibroPublico("No me puedes lastimar", "David Goggins",
                    "https://m.media-amazon.com/images/I/61wkLGRVqOL._AC_UF1000,1000_QL80_.jpg", superacionPersonal, 364);

            crearLibroPublico("Drácula", "Bram Stoker",
                    "https://m.media-amazon.com/images/I/61ZMLiTYxDL._AC_UF1000,1000_QL80_.jpg", terror, 418);

            crearLibroPublico("1984", "George Orwell",
                    "https://m.media-amazon.com/images/I/91SZSW8qSsL.jpg", cienciaFiccion, 328);

            crearLibroPublico("El Hobbit", "J.R.R. Tolkien",
                    "https://m.media-amazon.com/images/I/71qpt1yWfbL._AC_UF1000,1000_QL80_.jpg", fantasia, 310);

            crearLibroPublico("Clean Code", "Robert C. Martin",
                    "https://m.media-amazon.com/images/I/41xShlnTZTL.jpg", prog, 464);

            crearLibroPublico("Fundación", "Isaac Asimov",
                    "https://m.media-amazon.com/images/I/410RP6PK0TL._AC_UF1000,1000_QL80_.jpg", cienciaFiccion, 255);

            crearLibroPublico("Cien años de soledad", "Gabriel García Márquez",
                    "https://images.cdn3.buscalibre.com/fit-in/360x360/61/8d/618d227e8967274cd9589a549adff52d.jpg", novela, 471);

            crearLibroPublico("La sombra del viento", "Carlos Ruiz Zafón",
                    "https://m.media-amazon.com/images/I/61ZSuWFzQRL._AC_UF1000,1000_QL80_.jpg", novela, 576);

            crearLibroPublico("It", "Stephen King",
                    "https://m.media-amazon.com/images/I/71BwqlcZyVL._AC_UF1000,1000_QL80_.jpg", terror, 1138);

            crearLibroPublico("Crónica de una muerte anunciada", "Gabriel García Márquez",
                    "https://wmagazin.com/wp-content/uploads/2021/04/Cronicadeunamuerteanunciada-Colombia-1-633x1024.jpg", novela, 158);

            crearLibroPublico("El resplandor", "Stephen King",
                    "https://m.media-amazon.com/images/I/91n8sen+w1L.jpg", terror, 447);

            crearLibroPublico("Java Effective", "Joshua Bloch",
                    "https://m.media-amazon.com/images/I/71JAVv3TW4L.jpg", prog, 412);

            crearLibroPublico("Frankenstein", "Mary Shelley",
                    "https://m.media-amazon.com/images/I/71Vr8pSwRqL.jpg", terror, 280);

            crearLibroPublico("The Pragmatic Programmer", "Andrew Hunt & David Thomas",
                    "https://m.media-amazon.com/images/I/71f1jieYHNL._AC_UF1000,1000_QL80_.jpg", prog, 352);

            crearLibroPublico("Introduction to Algorithms", "Thomas H. Cormen",
                    "https://m.media-amazon.com/images/I/616c+ys7RBL._UF1000,1000_QL80_.jpg", prog, 1312);

            crearLibroPublico("Structure and Interpretation of Computer Programs", "Harold Abelson",
                    "https://m.media-amazon.com/images/I/71xZu1kgXJL._UF1000,1000_QL80_.jpg", prog, 657);

            crearLibroPublico("Design Patterns", "Erich Gamma",
                    "https://m.media-amazon.com/images/I/81IGFC6oFmL.jpg", prog, 395);

            crearLibroPublico("Dune", "Frank Herbert",
                    "https://www.estudioenescarlata.com/media/img/portadas/_visd_0001JPG01PBE.jpg", cienciaFiccion, 412);
        }
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

    private void crearLibroPublico(String titulo, String autor, String urlImagen, CategoriaEsquema categoria, Integer paginas) {
        LibroEsquema libro = new LibroEsquema();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setImagenUrl(urlImagen);
        libro.setUsuario(null);
        libro.setPaginasTotales(paginas);
        libro.setPaginasLeidas(0); // Por defecto 0
        libro.setCategorias(new ArrayList<>(List.of(categoria)));
        libroService.save(libro);
    }
}