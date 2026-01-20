package com.example.demo.services;

import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.repositories.UsuarioEsquemaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para UsuarioEsquemaService")
@SuppressWarnings("NonAsciiCharacters")
class UsuarioEsquemaServiceTest {

    @Mock
    private UsuarioEsquemaRepository repository;

    @InjectMocks
    private UsuarioEsquemaService service;

    private UsuarioEsquema usuario1;
    private UsuarioEsquema usuario2;

    @BeforeEach
    void setUp() {
        usuario1 = new UsuarioEsquema();
        usuario1.setId(1L);
        usuario1.setNombre("Juan Pérez");
        usuario1.setEmail("juan@example.com");
        usuario1.setContraseña("password123");

        usuario2 = new UsuarioEsquema();
        usuario2.setId(2L);
        usuario2.setNombre("María García");
        usuario2.setEmail("maria@example.com");
        usuario2.setContraseña("password456");
    }

    @Test
    @DisplayName("Debería encontrar un usuario por ID")
    void testFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario1));

        UsuarioEsquema result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Juan Pérez", result.getNombre());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debería retornar null cuando no encuentra usuario por ID")
    void testFindByIdNoExistente() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        UsuarioEsquema result = service.findById(999L);

        assertNull(result);
        verify(repository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debería encontrar todos los usuarios")
    void testFindAll() {
        List<UsuarioEsquema> usuarios = Arrays.asList(usuario1, usuario2);
        when(repository.findAll()).thenReturn(usuarios);

        List<UsuarioEsquema> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Juan Pérez", result.get(0).getNombre());
        assertEquals("María García", result.get(1).getNombre());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería guardar un usuario")
    void testSave() {
        when(repository.save(usuario1)).thenReturn(usuario1);

        UsuarioEsquema result = service.save(usuario1);

        assertNotNull(result);
        assertEquals("Juan Pérez", result.getNombre());
        verify(repository, times(1)).save(usuario1);
    }

    @Test
    @DisplayName("Debería buscar usuarios por nombre")
    void testBuscarPorNombre() {
        List<UsuarioEsquema> usuarios = Arrays.asList(usuario1);
        when(repository.findByNombre("Juan Pérez")).thenReturn(usuarios);

        List<UsuarioEsquema> result = service.buscarPorNombre("Juan Pérez");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan Pérez", result.get(0).getNombre());
        verify(repository, times(1)).findByNombre("Juan Pérez");
    }

    @Test
    @DisplayName("Debería verificar si existe un usuario por email")
    void testExistePorEmail() {
        when(repository.existsByEmail("juan@example.com")).thenReturn(true);

        boolean result = service.existePorEmail("juan@example.com");

        assertTrue(result);
        verify(repository, times(1)).existsByEmail("juan@example.com");
    }

    @Test
    @DisplayName("Debería retornar false cuando no existe usuario con el email")
    void testExistePorEmailNoExistente() {
        when(repository.existsByEmail("noexiste@example.com")).thenReturn(false);

        boolean result = service.existePorEmail("noexiste@example.com");

        assertFalse(result);
        verify(repository, times(1)).existsByEmail("noexiste@example.com");
    }

    @Test
    @DisplayName("Debería buscar usuarios por email")
    void testBuscarPorEmail() {
        List<UsuarioEsquema> usuarios = Arrays.asList(usuario1);
        when(repository.findByEmail("juan@example.com")).thenReturn(usuarios);

        List<UsuarioEsquema> result = service.buscarPorEmail("juan@example.com");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("juan@example.com", result.get(0).getEmail());
        verify(repository, times(1)).findByEmail("juan@example.com");
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no encuentra usuarios por nombre")
    void testBuscarPorNombreNoExistente() {
        when(repository.findByNombre("Nombre Inexistente")).thenReturn(Arrays.asList());

        List<UsuarioEsquema> result = service.buscarPorNombre("Nombre Inexistente");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByNombre("Nombre Inexistente");
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no encuentra usuarios por email")
    void testBuscarPorEmailNoExistente() {
        when(repository.findByEmail("noexiste@example.com")).thenReturn(Arrays.asList());

        List<UsuarioEsquema> result = service.buscarPorEmail("noexiste@example.com");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByEmail("noexiste@example.com");
    }

    @Test
    @DisplayName("Debería actualizar un usuario existente")
    void testActualizarUsuario() {
        usuario1.setNombre("Juan Pérez Actualizado");
        when(repository.save(usuario1)).thenReturn(usuario1);

        UsuarioEsquema result = service.save(usuario1);

        assertNotNull(result);
        assertEquals("Juan Pérez Actualizado", result.getNombre());
        verify(repository, times(1)).save(usuario1);
    }
}
