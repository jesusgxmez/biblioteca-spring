package com.example.demo.services;

import com.example.demo.entities.LibroEsquema;
import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.entities.CategoriaEsquema;
import com.example.demo.repositories.LibroEsquemaRepository;
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
@DisplayName("Pruebas unitarias para LibroEsquemaService")
class LibroEsquemaServiceTest {

    @Mock
    private LibroEsquemaRepository repository;

    @InjectMocks
    private LibroEsquemaService service;

    private LibroEsquema libro1;
    private LibroEsquema libro2;
    private UsuarioEsquema usuario;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioEsquema();
        usuario.setId(1L);
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan@example.com");

        libro1 = new LibroEsquema();
        libro1.setId(1L);
        libro1.setTitulo("Don Quijote");
        libro1.setAutor("Miguel de Cervantes");
        libro1.setImagenUrl("https://example.com/quijote.jpg");
        libro1.setPaginasTotales(500);
        libro1.setPaginasLeidas(250);
        libro1.setUsuario(usuario);

        libro2 = new LibroEsquema();
        libro2.setId(2L);
        libro2.setTitulo("Cien años de soledad");
        libro2.setAutor("Gabriel García Márquez");
        libro2.setImagenUrl("https://example.com/cien-anos.jpg");
        libro2.setPaginasTotales(471);
        libro2.setPaginasLeidas(0);
        libro2.setUsuario(usuario);
    }

    @Test
    @DisplayName("Debería encontrar todos los libros")
    void testFindAll() {
        List<LibroEsquema> libros = Arrays.asList(libro1, libro2);
        when(repository.findAll()).thenReturn(libros);

        List<LibroEsquema> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Don Quijote", result.get(0).getTitulo());
        assertEquals("Cien años de soledad", result.get(1).getTitulo());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería eliminar un libro por ID")
    void testDelete() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debería encontrar un libro por ID")
    void testFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(libro1));

        LibroEsquema result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Don Quijote", result.getTitulo());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debería retornar null cuando no encuentra libro por ID")
    void testFindByIdNoExistente() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        LibroEsquema result = service.findById(999L);

        assertNull(result);
        verify(repository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debería buscar libros por email de usuario")
    void testBuscarPorEmailDeUsuario() {
        List<LibroEsquema> libros = Arrays.asList(libro1, libro2);
        when(repository.findByUsuarioEmail("juan@example.com")).thenReturn(libros);

        List<LibroEsquema> result = service.buscarPorEmailDeUsuario("juan@example.com");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(l -> l.getUsuario().getEmail().equals("juan@example.com")));
        verify(repository, times(1)).findByUsuarioEmail("juan@example.com");
    }

    @Test
    @DisplayName("Debería buscar libros por título")
    void testBuscarPorTitulo() {
        List<LibroEsquema> libros = Arrays.asList(libro1);
        when(repository.findByTitulo("Don Quijote")).thenReturn(libros);

        List<LibroEsquema> result = service.buscarPorTitulo("Don Quijote");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Don Quijote", result.get(0).getTitulo());
        verify(repository, times(1)).findByTitulo("Don Quijote");
    }

    @Test
    @DisplayName("Debería buscar libros por autor")
    void testBuscarPorAutor() {
        List<LibroEsquema> libros = Arrays.asList(libro2);
        when(repository.findByAutor("Gabriel García Márquez")).thenReturn(libros);

        List<LibroEsquema> result = service.buscarPorAutor("Gabriel García Márquez");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Gabriel García Márquez", result.get(0).getAutor());
        verify(repository, times(1)).findByAutor("Gabriel García Márquez");
    }

    @Test
    @DisplayName("Debería buscar libros por URL de imagen")
    void testBuscarPorImagenUrl() {
        List<LibroEsquema> libros = Arrays.asList(libro1);
        when(repository.findByImagenUrl("https://example.com/quijote.jpg")).thenReturn(libros);

        List<LibroEsquema> result = service.buscarPorImagenUrl("https://example.com/quijote.jpg");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("https://example.com/quijote.jpg", result.get(0).getImagenUrl());
        verify(repository, times(1)).findByImagenUrl("https://example.com/quijote.jpg");
    }

    @Test
    @DisplayName("Debería guardar un libro")
    void testSave() {
        when(repository.save(libro1)).thenReturn(libro1);

        service.save(libro1);

        verify(repository, times(1)).save(libro1);
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no encuentra libros por email de usuario")
    void testBuscarPorEmailDeUsuarioNoExistente() {
        when(repository.findByUsuarioEmail("noexiste@example.com")).thenReturn(Arrays.asList());

        List<LibroEsquema> result = service.buscarPorEmailDeUsuario("noexiste@example.com");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByUsuarioEmail("noexiste@example.com");
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no encuentra libros por título")
    void testBuscarPorTituloNoExistente() {
        when(repository.findByTitulo("Título Inexistente")).thenReturn(Arrays.asList());

        List<LibroEsquema> result = service.buscarPorTitulo("Título Inexistente");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByTitulo("Título Inexistente");
    }

    @Test
    @DisplayName("Debería actualizar un libro existente")
    void testActualizarLibro() {
        libro1.setPaginasLeidas(500);
        when(repository.save(libro1)).thenReturn(libro1);

        service.save(libro1);

        verify(repository, times(1)).save(libro1);
    }
}
