package com.example.demo.repositories;

import com.example.demo.entities.CategoriaEsquema;
import com.example.demo.entities.LibroEsquema;
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
@DisplayName("Pruebas de integración para CategoriaEsquemaRepository")
class CategoriaEsquemaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoriaEsquemaRepository repository;

    private CategoriaEsquema categoria1;
    private CategoriaEsquema categoria2;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        entityManager.flush();

        categoria1 = new CategoriaEsquema();
        categoria1.setCategoria("Ficción");

        categoria2 = new CategoriaEsquema();
        categoria2.setCategoria("Ciencia Ficción");
    }

    @Test
    @DisplayName("Debería guardar una categoría correctamente")
    void testGuardarCategoria() {
        CategoriaEsquema savedCategoria = repository.save(categoria1);

        assertNotNull(savedCategoria);
        assertNotNull(savedCategoria.getId());
        assertEquals("Ficción", savedCategoria.getCategoria());
    }

    @Test
    @DisplayName("Debería encontrar una categoría por ID")
    void testFindById() {
        CategoriaEsquema savedCategoria = entityManager.persistAndFlush(categoria1);

        Optional<CategoriaEsquema> foundCategoria = repository.findById(savedCategoria.getId());

        assertTrue(foundCategoria.isPresent());
        assertEquals(savedCategoria.getId(), foundCategoria.get().getId());
        assertEquals("Ficción", foundCategoria.get().getCategoria());
    }

    @Test
    @DisplayName("Debería encontrar todas las categorías")
    void testFindAll() {
        entityManager.persist(categoria1);
        entityManager.persist(categoria2);
        entityManager.flush();

        List<CategoriaEsquema> categorias = repository.findAll();

        assertEquals(2, categorias.size());
    }

    @Test
    @DisplayName("Debería encontrar categorías por nombre")
    void testFindByCategoria() {
        entityManager.persist(categoria1);
        entityManager.persist(categoria2);
        entityManager.flush();

        List<CategoriaEsquema> categorias = repository.findByCategoria("Ficción");

        assertEquals(1, categorias.size());
        assertEquals("Ficción", categorias.get(0).getCategoria());
    }

    @Test
    @DisplayName("Debería verificar si existe una categoría por nombre")
    void testExistsByCategoria() {
        entityManager.persist(categoria1);
        entityManager.flush();

        boolean exists = repository.existsByCategoria("Ficción");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Debería retornar false cuando no existe una categoría con el nombre")
    void testExistsByCategoriaNoExistente() {
        boolean exists = repository.existsByCategoria("Categoría Inexistente");

        assertFalse(exists);
    }

    @Test
    @DisplayName("Debería eliminar una categoría por ID")
    void testDeleteById() {
        CategoriaEsquema savedCategoria = entityManager.persistAndFlush(categoria1);
        Long id = savedCategoria.getId();

        repository.deleteById(id);
        entityManager.flush();

        Optional<CategoriaEsquema> deletedCategoria = repository.findById(id);
        assertFalse(deletedCategoria.isPresent());
    }

    @Test
    @DisplayName("Debería actualizar una categoría existente")
    void testActualizarCategoria() {
        CategoriaEsquema savedCategoria = entityManager.persistAndFlush(categoria1);

        savedCategoria.setCategoria("Ficción Histórica");
        repository.save(savedCategoria);
        entityManager.flush();

        Optional<CategoriaEsquema> updatedCategoria = repository.findById(savedCategoria.getId());
        assertTrue(updatedCategoria.isPresent());
        assertEquals("Ficción Histórica", updatedCategoria.get().getCategoria());
    }

    @Test
    @DisplayName("Debería contar el número de categorías")
    void testCount() {
        entityManager.persist(categoria1);
        entityManager.persist(categoria2);
        entityManager.flush();

        long count = repository.count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no encuentra categorías por nombre")
    void testFindByCategoriaNoExistente() {
        List<CategoriaEsquema> categorias = repository.findByCategoria("Categoría Inexistente");
        assertTrue(categorias.isEmpty());
    }

    @Test
    @DisplayName("Debería eliminar todas las categorías")
    void testDeleteAll() {
        entityManager.persist(categoria1);
        entityManager.persist(categoria2);
        entityManager.flush();

        repository.deleteAll();

        long count = repository.count();
        assertEquals(0, count);
    }
}
