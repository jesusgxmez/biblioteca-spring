package com.example.demo.repositories;

import com.example.demo.entities.UsuarioEsquema;
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
@DisplayName("Pruebas de integración para UsuarioEsquemaRepository")
@SuppressWarnings("NonAsciiCharacters")
class UsuarioEsquemaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioEsquemaRepository repository;

    private UsuarioEsquema usuario1;
    private UsuarioEsquema usuario2;

    @BeforeEach
    void setUp() {
        // Limpiar base de datos antes de cada prueba
        repository.deleteAll();
        entityManager.flush();

        usuario1 = new UsuarioEsquema();
        usuario1.setNombre("Juan Pérez");
        usuario1.setEmail("juan@example.com");
        usuario1.setContraseña("password123");

        usuario2 = new UsuarioEsquema();
        usuario2.setNombre("María García");
        usuario2.setEmail("maria@example.com");
        usuario2.setContraseña("password456");
    }

    @Test
    @DisplayName("Debería guardar un usuario correctamente")
    void testGuardarUsuario() {
        UsuarioEsquema savedUsuario = repository.save(usuario1);

        assertNotNull(savedUsuario);
        assertNotNull(savedUsuario.getId());
        assertEquals("Juan Pérez", savedUsuario.getNombre());
        assertEquals("juan@example.com", savedUsuario.getEmail());
    }

    @Test
    @DisplayName("Debería encontrar un usuario por ID")
    void testFindById() {
        UsuarioEsquema savedUsuario = entityManager.persistAndFlush(usuario1);

        Optional<UsuarioEsquema> foundUsuario = repository.findById(savedUsuario.getId());

        assertTrue(foundUsuario.isPresent());
        assertEquals(savedUsuario.getId(), foundUsuario.get().getId());
        assertEquals("Juan Pérez", foundUsuario.get().getNombre());
    }

    @Test
    @DisplayName("Debería retornar Optional.empty cuando no encuentra usuario por ID")
    void testFindByIdNoExistente() {
        Optional<UsuarioEsquema> foundUsuario = repository.findById(999L);

        assertFalse(foundUsuario.isPresent());
    }

    @Test
    @DisplayName("Debería encontrar todos los usuarios")
    void testFindAll() {
        entityManager.persist(usuario1);
        entityManager.persist(usuario2);
        entityManager.flush();

        List<UsuarioEsquema> usuarios = repository.findAll();

        assertEquals(2, usuarios.size());
    }

    @Test
    @DisplayName("Debería encontrar usuarios por nombre")
    void testFindByNombre() {
        entityManager.persist(usuario1);
        entityManager.persist(usuario2);
        entityManager.flush();

        List<UsuarioEsquema> usuarios = repository.findByNombre("Juan Pérez");

        assertEquals(1, usuarios.size());
        assertEquals("Juan Pérez", usuarios.get(0).getNombre());
        assertEquals("juan@example.com", usuarios.get(0).getEmail());
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no encuentra usuarios por nombre")
    void testFindByNombreNoExistente() {
        entityManager.persist(usuario1);
        entityManager.flush();

        List<UsuarioEsquema> usuarios = repository.findByNombre("Nombre Inexistente");

        assertTrue(usuarios.isEmpty());
    }

    @Test
    @DisplayName("Debería encontrar usuarios por email")
    void testFindByEmail() {
        entityManager.persist(usuario1);
        entityManager.persist(usuario2);
        entityManager.flush();

        List<UsuarioEsquema> usuarios = repository.findByEmail("maria@example.com");

        assertEquals(1, usuarios.size());
        assertEquals("María García", usuarios.get(0).getNombre());
    }

    @Test
    @DisplayName("Debería verificar si existe un usuario por email")
    void testExistsByEmail() {
        entityManager.persist(usuario1);
        entityManager.flush();

        boolean exists = repository.existsByEmail("juan@example.com");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Debería retornar false cuando no existe un usuario con el email")
    void testExistsByEmailNoExistente() {
        boolean exists = repository.existsByEmail("noexiste@example.com");

        assertFalse(exists);
    }

    @Test
    @DisplayName("Debería eliminar un usuario por ID")
    void testDeleteById() {
        UsuarioEsquema savedUsuario = entityManager.persistAndFlush(usuario1);
        Long id = savedUsuario.getId();

        repository.deleteById(id);
        entityManager.flush();

        Optional<UsuarioEsquema> deletedUsuario = repository.findById(id);
        assertFalse(deletedUsuario.isPresent());
    }

    @Test
    @DisplayName("Debería actualizar un usuario existente")
    void testActualizarUsuario() {
        UsuarioEsquema savedUsuario = entityManager.persistAndFlush(usuario1);

        savedUsuario.setNombre("Juan Pérez Actualizado");
        savedUsuario.setEmail("juan.nuevo@example.com");
        repository.save(savedUsuario);
        entityManager.flush();

        Optional<UsuarioEsquema> updatedUsuario = repository.findById(savedUsuario.getId());
        assertTrue(updatedUsuario.isPresent());
        assertEquals("Juan Pérez Actualizado", updatedUsuario.get().getNombre());
        assertEquals("juan.nuevo@example.com", updatedUsuario.get().getEmail());
    }

    @Test
    @DisplayName("Debería contar el número de usuarios")
    void testCount() {
        entityManager.persist(usuario1);
        entityManager.persist(usuario2);
        entityManager.flush();

        long count = repository.count();

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Debería eliminar todos los usuarios")
    void testDeleteAll() {
        entityManager.persist(usuario1);
        entityManager.persist(usuario2);
        entityManager.flush();

        repository.deleteAll();

        long count = repository.count();
        assertEquals(0, count);
    }

    @Test
    @DisplayName("Debería manejar múltiples usuarios con el mismo nombre")
    void testMultiplesUsuariosMismoNombre() {
        usuario1.setNombre("Juan");
        usuario2.setNombre("Juan");

        entityManager.persist(usuario1);
        entityManager.persist(usuario2);
        entityManager.flush();

        List<UsuarioEsquema> usuarios = repository.findByNombre("Juan");

        assertEquals(2, usuarios.size());
    }

    @Test
    @DisplayName("Debería verificar que el email es único para existsByEmail")
    void testEmailUnico() {
        entityManager.persist(usuario1);
        entityManager.flush();

        assertTrue(repository.existsByEmail("juan@example.com"));
        assertFalse(repository.existsByEmail("otro@example.com"));
    }
}
