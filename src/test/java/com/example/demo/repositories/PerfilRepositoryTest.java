package com.example.demo.repositories;

import com.example.demo.entities.PerfilEsquema;
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
@DisplayName("Pruebas de integración para PerfilEsquemaRepository")
@SuppressWarnings("NonAsciiCharacters")
class PerfilEsquemaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PerfilEsquemaRepository repository;

    private PerfilEsquema perfil1;
    private UsuarioEsquema usuario1;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        entityManager.flush();

        usuario1 = new UsuarioEsquema();
        usuario1.setNombre("Juan Pérez");
        usuario1.setEmail("juan@example.com");
        usuario1.setContraseña("password123");

        perfil1 = new PerfilEsquema();
        perfil1.setBio("Amante de la lectura");
        perfil1.setUsuario(usuario1);
        usuario1.setPerfil(perfil1);
    }

    @Test
    @DisplayName("Debería guardar un perfil correctamente")
    void testGuardarPerfil() {
        entityManager.persist(usuario1);
        PerfilEsquema savedPerfil = repository.save(perfil1);

        assertNotNull(savedPerfil);
        assertNotNull(savedPerfil.getId());
    }

    @Test
    @DisplayName("Debería encontrar un perfil por ID")
    void testFindById() {
        entityManager.persist(usuario1);
        PerfilEsquema savedPerfil = entityManager.persistAndFlush(perfil1);

        Optional<PerfilEsquema> foundPerfil = repository.findById(savedPerfil.getId());

        assertTrue(foundPerfil.isPresent());
    }

    @Test
    @DisplayName("Debería encontrar perfiles por biografía")
    void testFindByBio() {
        entityManager.persist(usuario1);
        entityManager.persistAndFlush(perfil1);

        List<PerfilEsquema> perfiles = repository.findByBio("Amante de la lectura");

        assertEquals(1, perfiles.size());
    }
}
