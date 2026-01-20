package com.example.demo;

import com.example.demo.entities.CategoriaEsquema;
import com.example.demo.entities.LibroEsquema;
import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.services.CategoriaEsquemaService;
import com.example.demo.services.LibroEsquemaService;
import com.example.demo.services.UsuarioEsquemaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioEsquemaService usuarioService;
    private final CategoriaEsquemaService categoriaService;
    private final LibroEsquemaService libroService;
    private final PasswordEncoder passwordEncoder; // <--- INYECTADO

    public DataInitializer(UsuarioEsquemaService usuarioService,
                           CategoriaEsquemaService categoriaService,
                           LibroEsquemaService libroService,
                           PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.categoriaService = categoriaService;
        this.libroService = libroService;
        this.passwordEncoder = passwordEncoder;
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

            crearLibroPublico("Don Quijote de la Mancha", "Miguel de Cervantes",
                    "https://algareditorial.com/29329-thickbox_default/don-quijote.jpg", novela, 1032,
                    "Alonso Quijano es un hidalgo que, tras leer demasiados libros de caballería, pierde la cordura y decide recorrer España como caballero andante bajo el nombre de Don Quijote.");

            crearLibroPublico("No me puedes lastimar", "David Goggins",
                    "https://m.media-amazon.com/images/I/61wkLGRVqOL._AC_UF1000,1000_QL80_.jpg", superacionPersonal, 364,
                    "David Goggins comparte su asombrosa transformación de un joven deprimido con sobrepeso a una leyenda de las Fuerzas Armadas y uno de los mejores atletas de resistencia del mundo.");

            crearLibroPublico("Drácula", "Bram Stoker",
                    "https://m.media-amazon.com/images/I/61ZMLiTYxDL._AC_UF1000,1000_QL80_.jpg", terror, 418,
                    "A través de diarios y cartas, se narra la llegada del vampiro más famoso del mundo a Inglaterra y los esfuerzos de un pequeño grupo por detener sus planes de inmortalidad.");

            crearLibroPublico("1984", "George Orwell",
                    "https://m.media-amazon.com/images/I/91SZSW8qSsL.jpg", cienciaFiccion, 328,
                    "En una sociedad vigilada constantemente por el Gran Hermano, Winston Smith intenta rebelarse contra un sistema que controla no solo las acciones, sino también los pensamientos.");

            crearLibroPublico("El Hobbit", "J.R.R. Tolkien",
                    "https://m.media-amazon.com/images/I/71qpt1yWfbL._AC_UF1000,1000_QL80_.jpg", fantasia, 310,
                    "Bilbo Bolsón es un hobbit que disfruta de su vida tranquila hasta que el mago Gandalf y un grupo de enanos lo arrastran a una aventura épica para recuperar un tesoro custodiado por un dragón.");

            crearLibroPublico("Clean Code", "Robert C. Martin",
                    "https://m.media-amazon.com/images/I/41xShlnTZTL.jpg", prog, 464,
                    "Incluso el código malo puede funcionar. Pero si no es limpio, puede acabar con una empresa de desarrollo. Este libro enseña a escribir código profesional que sea fácil de leer y mantener.");

            crearLibroPublico("Fundación", "Isaac Asimov",
                    "https://m.media-amazon.com/images/I/410RP6PK0TL._AC_UF1000,1000_QL80_.jpg", cienciaFiccion, 255,
                    "El psicohistoriador Hari Seldon prevé la caída del Imperio Galáctico y crea la Fundación para preservar el conocimiento humano y reducir la era de barbarie que está por venir.");

            crearLibroPublico("Cien años de soledad", "Gabriel García Márquez",
                    "https://images.cdn3.buscalibre.com/fit-in/360x360/61/8d/618d227e8967274cd9589a549adff52d.jpg", novela, 471,
                    "La saga de la familia Buendía en el pueblo de Macondo, una obra cumbre del realismo mágico que entrelaza generaciones de amores, guerras y soledad absoluta.");

            crearLibroPublico("La sombra del viento", "Carlos Ruiz Zafón",
                    "https://m.media-amazon.com/images/I/61ZSuWFzQRL._AC_UF1000,1000_QL80_.jpg", novela, 576,
                    "En la Barcelona de posguerra, un joven es llevado al Cementerio de los Libros Olvidados, donde descubre un ejemplar que cambiará su vida y lo envolverá en un misterio oscuro.");

            crearLibroPublico("It", "Stephen King",
                    "https://m.media-amazon.com/images/I/71BwqlcZyVL._AC_UF1000,1000_QL80_.jpg", terror, 1138,
                    "Un grupo de siete niños se enfrenta a una criatura milenaria que toma la forma de un payaso y se alimenta del miedo de los habitantes del pueblo de Derry.");

            crearLibroPublico("Crónica de una muerte anunciada", "Gabriel García Márquez",
                    "https://wmagazin.com/wp-content/uploads/2021/04/Cronicadeunamuerteanunciada-Colombia-1-633x1024.jpg", novela, 158,
                    "Un relato magistral que reconstruye el asesinato de Santiago Nasar a manos de los gemelos Vicario, un crimen que todo el pueblo conocía antes de que ocurriera.");

            crearLibroPublico("El resplandor", "Stephen King",
                    "https://m.media-amazon.com/images/I/91n8sen+w1L.jpg", terror, 447,
                    "Jack Torrance acepta un trabajo como cuidador de un hotel aislado durante el invierno, ignorando que las fuerzas oscuras del lugar acechan la cordura de su familia.");

            crearLibroPublico("Java Effective", "Joshua Bloch",
                    "https://m.media-amazon.com/images/I/71JAVv3TW4L.jpg", prog, 412,
                    "La guía definitiva de mejores prácticas para el lenguaje Java, enseñando cómo usar las bibliotecas y el lenguaje de la forma más eficiente y robusta posible.");

            crearLibroPublico("Frankenstein", "Mary Shelley",
                    "https://m.media-amazon.com/images/I/71Vr8pSwRqL.jpg", terror, 280,
                    "Victor Frankenstein logra dar vida a una criatura creada a partir de restos humanos, solo para horrorizarse ante su creación y desencadenar una tragedia de rechazo y venganza.");

            crearLibroPublico("The Pragmatic Programmer", "Andrew Hunt & David Thomas",
                    "https://m.media-amazon.com/images/I/71f1jieYHNL._AC_UF1000,1000_QL80_.jpg", prog, 352,
                    "Un libro clásico que ofrece consejos prácticos para mejorar en la ingeniería de software, desde la responsabilidad personal hasta el desarrollo profesional y técnico.");

            crearLibroPublico("Introduction to Algorithms", "Thomas H. Cormen",
                    "https://m.media-amazon.com/images/I/616c+ys7RBL._UF1000,1000_QL80_.jpg", prog, 1312,
                    "Conocido como la biblia de los algoritmos, este texto cubre de manera exhaustiva el diseño y análisis de algoritmos para estudiantes y profesionales del desarrollo.");

            crearLibroPublico("Structure and Interpretation of Computer Programs", "Harold Abelson",
                    "https://m.media-amazon.com/images/I/71xZu1kgXJL._UF1000,1000_QL80_.jpg", prog, 657,
                    "Un texto fundamental que explora los conceptos básicos de la programación y la informática, enfocándose en la abstracción y la gestión de la complejidad.");

            crearLibroPublico("Design Patterns", "Erich Gamma",
                    "https://m.media-amazon.com/images/I/81IGFC6oFmL.jpg", prog, 395,
                    "El libro que popularizó los patrones de diseño en el desarrollo de software orientado a objetos, ofreciendo soluciones probadas a problemas comunes de arquitectura.");

            crearLibroPublico("Dune", "Frank Herbert",
                    "https://www.estudioenescarlata.com/media/img/portadas/_visd_0001JPG01PBE.jpg", cienciaFiccion, 412,
                    "En un futuro lejano, Paul Atreides viaja al planeta desértico Arrakis para asegurar el suministro de la especia, la sustancia más importante del universo galáctico.");
        }
    }

    private void crearUsuarioSiNoExiste(String nombre, String email, String pass) {
        if (usuarioService.buscarPorNombre(nombre).isEmpty()) {
            UsuarioEsquema u = new UsuarioEsquema();
            u.setNombre(nombre);
            u.setEmail(email);
            u.setContraseña(passwordEncoder.encode(pass)); // <--- CIFRADO
            usuarioService.save(u);
        }
    }

    private void crearLibroPublico(String titulo, String autor, String urlImagen, CategoriaEsquema categoria, Integer paginas, String descripcion) {
        LibroEsquema libro = new LibroEsquema();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setImagenUrl(urlImagen);
        libro.setUsuario(null);
        libro.setPaginasTotales(paginas);
        libro.setPaginasLeidas(0);
        libro.setDescripcion(descripcion);
        libro.setCategorias(new ArrayList<>(List.of(categoria)));
        libroService.save(libro);
    }
}