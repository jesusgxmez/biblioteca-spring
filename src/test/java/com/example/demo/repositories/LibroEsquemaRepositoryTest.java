package com.example.demo.repositories;

import com.example.demo.entities.LibroEsquema;
import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.entities.CategoriaEsquema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Pruebas de integración para LibroEsquemaRepository")
class LibroEsquemaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LibroEsquemaRepository repository;

    private LibroEsquema libro1;
    private LibroEsquema libro2;
    private UsuarioEsquema usuario;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        entityManager.flush();

        usuario = new UsuarioEsquema();
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan@example.com");
        usuario.setContraseña("password123");
        entityManager.persist(usuario);

        libro1 = new LibroEsquema();
        libro1.setTitulo("Don Quijote");
        libro1.setAutor("Miguel de Cervantes");
        libro1.setImagenUrl("https://example.com/quijote.jpg");
        libro1.setPaginasTotales(500);
        libro1.setPaginasLeidas(250);
        libro1.setUsuario(usuario);

        libro2 = new LibroEsquema();
        libro2.setTitulo("Cien años de soledad");
        libro2.setAutor("Gabriel García Márquez");
        libro2.setImagenUrl("https://example.com/cien-anos.jpg");
        libro2.setPaginasTotales(471);
        libro2.setPaginasLeidas(0);
        libro2.setUsuario(usuario);
    }

    @Test
    @DisplayName("Debería guardar un libro correctamente")
    void testGuardarLibro() {
        LibroEsquema savedLibro = repository.save(libro1);

        assertNotNull(savedLibro);
        assertNotNull(savedLibro.getId());
        assertEquals("Don Quijote", savedLibro.getTitulo());
        assertEquals("Miguel de Cervantes", savedLibro.getAutor());
    }

    @Test
    @DisplayName("Debería encontrar un libro por ID")
    void testFindById() {
        LibroEsquema savedLibro = entityManager.persistAndFlush(libro1);

        Optional<LibroEsquema> foundLibro = repository.findById(savedLibro.getId());

        assertTrue(foundLibro.isPresent());
        assertEquals(savedLibro.getId(), foundLibro.get().getId());
        assertEquals("Don Quijote", foundLibro.get().getTitulo());
    }

    @Test
    @DisplayName("Debería encontrar todos los libros")
    void testFindAll() {
        entityManager.persist(libro1);
        entityManager.persist(libro2);
        entityManager.flush();

        List<LibroEsquema> libros = repository.findAll();

        assertEquals(2, libros.size());
    }

    @Test
    @DisplayName("Debería encontrar libros por título")
    void testFindByTitulo() {
        entityManager.persist(libro1);
        entityManager.persist(libro2);
        entityManager.flush();

        List<LibroEsquema> libros = repository.findByTitulo("Don Quijote");

        assertEquals(1, libros.size());
        assertEquals("Don Quijote", libros.get(0).getTitulo());
        assertEquals("Miguel de Cervantes", libros.get(0).getAutor());
    }

    @Test
    @DisplayName("Debería encontrar libros por autor")
    void testFindByAutor() {
        entityManager.persist(libro1);
        entityManager.persist(libro2);
        entityManager.flush();

        List<LibroEsquema> libros = repository.findByAutor("Gabriel García Márquez");

        assertEquals(1, libros.size());
        assertEquals("Cien años de soledad", libros.get(0).getTitulo());
    }

    @Test
    @DisplayName("Debería encontrar libros por URL de imagen")
    void testFindByImagenUrl() {
        entityManager.persist(libro1);
        entityManager.persist(libro2);
        entityManager.flush();

        List<LibroEsquema> libros = repository.findByImagenUrl("https://example.com/quijote.jpg");

        assertEquals(1, libros.size());
        assertEquals("Don Quijote", libros.get(0).getTitulo());
    }

    @Test
    @DisplayName("Debería encontrar libros por email del usuario")
    void testFindByUsuarioEmail() {
        entityManager.persist(libro1);
        entityManager.persist(libro2);
        entityManager.flush();

        List<LibroEsquema> libros = repository.findByUsuarioEmail("juan@example.com");

        assertEquals(2, libros.size());
        assertTrue(libros.stream().allMatch(l -> l.getUsuario().getEmail().equals("juan@example.com")));
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no encuentra libros por email de usuario")
    void testFindByUsuarioEmailNoExistente() {
        entityManager.persist(libro1);
        entityManager.flush();

        List<LibroEsquema> libros = repository.findByUsuarioEmail("noexiste@example.com");

        assertTrue(libros.isEmpty());
    }

    @Test
    @DisplayName("Debería eliminar un libro por ID")
    void testDeleteById() {
        LibroEsquema savedLibro = entityManager.persistAndFlush(libro1);
        Long id = savedLibro.getId();

        repository.deleteById(id);
        entityManager.flush();

        Optional<LibroEsquema> deletedLibro = repository.findById(id);
        assertFalse(deletedLibro.isPresent());
    }

    @Test
    @DisplayName("Debería actualizar un libro existente")
    void testActualizarLibro() {
        LibroEsquema savedLibro = entityManager.persistAndFlush(libro1);

        savedLibro.setTitulo("Don Quijote - Edición Actualizada");
        savedLibro.setPaginasLeidas(500);
        repository.save(savedLibro);
        entityManager.flush();

        Optional<LibroEsquema> updatedLibro = repository.findById(savedLibro.getId());
        assertTrue(updatedLibro.isPresent());
        assertEquals("Don Quijote - Edición Actualizada", updatedLibro.get().getTitulo());
        assertEquals(500, updatedLibro.get().getPaginasLeidas());
    }

    @Test
    @DisplayName("Debería contar el número de libros")
    void testCount() {
        entityManager.persist(libro1);
        entityManager.persist(libro2);
        entityManager.flush();

        long count = repository.count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Debería guardar libro con categorías")
    void testGuardarLibroConCategorias() {
        CategoriaEsquema categoria1 = new CategoriaEsquema();
        categoria1.setCategoria("Ficción");
        entityManager.persist(categoria1);

        CategoriaEsquema categoria2 = new CategoriaEsquema();
        categoria2.setCategoria("Clásicos");
        entityManager.persist(categoria2);

        libro1.getCategorias().add(categoria1);
        libro1.getCategorias().add(categoria2);

        LibroEsquema savedLibro = entityManager.persistAndFlush(libro1);

        Optional<LibroEsquema> foundLibro = repository.findById(savedLibro.getId());
        assertTrue(foundLibro.isPresent());
        assertEquals(2, foundLibro.get().getCategorias().size());
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no encuentra libros por título")
    void testFindByTituloNoExistente() {
        List<LibroEsquema> libros = repository.findByTitulo("Título Inexistente");
        assertTrue(libros.isEmpty());
    }

    @Test
    @DisplayName("Debería manejar múltiples libros del mismo autor")
    void testMultiplesLibrosMismoAutor() {
        libro1.setAutor("George Orwell");
        libro2.setAutor("George Orwell");

        entityManager.persist(libro1);
        entityManager.persist(libro2);
        entityManager.flush();

        List<LibroEsquema> libros = repository.findByAutor("George Orwell");

        assertEquals(2, libros.size());
    }
}
