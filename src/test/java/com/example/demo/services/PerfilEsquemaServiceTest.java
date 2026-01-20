package com.example.demo.services;

import com.example.demo.entities.PerfilEsquema;
import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.repositories.PerfilEsquemaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para PerfilEsquemaService")
@SuppressWarnings("NonAsciiCharacters")
class PerfilEsquemaServiceTest {

    @Mock
    private PerfilEsquemaRepository repository;

    @InjectMocks
    private PerfilEsquemaService service;

    private PerfilEsquema perfil1;
    private PerfilEsquema perfil2;
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

        perfil1 = new PerfilEsquema();
        perfil1.setId(1L);
        perfil1.setBio("Amante de la lectura y la literatura clásica");
        perfil1.setUsuario(usuario1);

        perfil2 = new PerfilEsquema();
        perfil2.setId(2L);
        perfil2.setBio("Lector de ciencia ficción");
        perfil2.setUsuario(usuario2);
    }

    @Test
    @DisplayName("Debería encontrar un perfil por ID")
    void testFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(perfil1));

        PerfilEsquema result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Amante de la lectura y la literatura clásica", result.getBio());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debería retornar null cuando no encuentra perfil por ID")
    void testFindByIdNoExistente() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        PerfilEsquema result = service.findById(999L);

        assertNull(result);
        verify(repository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debería encontrar todos los perfiles")
    void testFindAll() {
        List<PerfilEsquema> perfiles = Arrays.asList(perfil1, perfil2);
        when(repository.findAll()).thenReturn(perfiles);

        List<PerfilEsquema> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Amante de la lectura y la literatura clásica", result.get(0).getBio());
        assertEquals("Lector de ciencia ficción", result.get(1).getBio());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería guardar un perfil")
    void testSave() {
        when(repository.save(perfil1)).thenReturn(perfil1);

        PerfilEsquema result = service.save(perfil1);

        assertNotNull(result);
        assertEquals("Amante de la lectura y la literatura clásica", result.getBio());
        verify(repository, times(1)).save(perfil1);
    }

    @Test
    @DisplayName("Debería buscar perfiles por biografía")
    void testBuscarPorBio() {
        List<PerfilEsquema> perfiles = Collections.singletonList(perfil2);
        when(repository.findByBio("Lector de ciencia ficción")).thenReturn(perfiles);

        List<PerfilEsquema> result = service.buscarPorBio("Lector de ciencia ficción");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Lector de ciencia ficción", result.get(0).getBio());
        verify(repository, times(1)).findByBio("Lector de ciencia ficción");
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no encuentra perfiles por biografía")
    void testBuscarPorBioNoExistente() {
        when(repository.findByBio("Bio inexistente")).thenReturn(Collections.emptyList());

        List<PerfilEsquema> result = service.buscarPorBio("Bio inexistente");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByBio("Bio inexistente");
    }

    @Test
    @DisplayName("Debería actualizar un perfil existente")
    void testActualizarPerfil() {
        perfil1.setBio("Biografía actualizada");
        when(repository.save(perfil1)).thenReturn(perfil1);

        PerfilEsquema result = service.save(perfil1);

        assertNotNull(result);
        assertEquals("Biografía actualizada", result.getBio());
        verify(repository, times(1)).save(perfil1);
    }

    @Test
    @DisplayName("Debería manejar perfil con usuario asociado")
    void testPerfilConUsuario() {
        when(repository.findById(1L)).thenReturn(Optional.of(perfil1));

        PerfilEsquema result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getUsuario());
        assertEquals("Juan Pérez", result.getUsuario().getNombre());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando findAll no encuentra perfiles")
    void testFindAllVacio() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<PerfilEsquema> result = service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería guardar perfil sin usuario asociado")
    void testGuardarPerfilSinUsuario() {
        PerfilEsquema perfilSinUsuario = new PerfilEsquema();
        perfilSinUsuario.setBio("Perfil sin usuario");

        when(repository.save(perfilSinUsuario)).thenReturn(perfilSinUsuario);

        PerfilEsquema result = service.save(perfilSinUsuario);

        assertNotNull(result);
        assertEquals("Perfil sin usuario", result.getBio());
        assertNull(result.getUsuario());
        verify(repository, times(1)).save(perfilSinUsuario);
    }
}
