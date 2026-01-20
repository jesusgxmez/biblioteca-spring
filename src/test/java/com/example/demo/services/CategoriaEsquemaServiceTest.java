package com.example.demo.services;

import com.example.demo.entities.CategoriaEsquema;
import com.example.demo.repositories.CategoriaEsquemaRepository;
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
@DisplayName("Pruebas unitarias para CategoriaEsquemaService")
class CategoriaEsquemaServiceTest {

    @Mock
    private CategoriaEsquemaRepository repository;

    @InjectMocks
    private CategoriaEsquemaService service;

    private CategoriaEsquema categoria1;
    private CategoriaEsquema categoria2;

    @BeforeEach
    void setUp() {
        categoria1 = new CategoriaEsquema();
        categoria1.setId(1L);
        categoria1.setCategoria("Ficción");

        categoria2 = new CategoriaEsquema();
        categoria2.setId(2L);
        categoria2.setCategoria("Ciencia Ficción");
    }

    @Test
    @DisplayName("Debería encontrar una categoría por ID")
    void testFindById() {
        when(repository.findById(1L)).thenReturn(Optional.of(categoria1));

        CategoriaEsquema result = service.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ficción", result.getCategoria());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debería retornar null cuando no encuentra categoría por ID")
    void testFindByIdNoExistente() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        CategoriaEsquema result = service.findById(999L);

        assertNull(result);
        verify(repository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debería encontrar todas las categorías")
    void testFindAll() {
        List<CategoriaEsquema> categorias = Arrays.asList(categoria1, categoria2);
        when(repository.findAll()).thenReturn(categorias);

        List<CategoriaEsquema> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ficción", result.get(0).getCategoria());
        assertEquals("Ciencia Ficción", result.get(1).getCategoria());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería guardar una categoría")
    void testSave() {
        when(repository.save(categoria1)).thenReturn(categoria1);

        CategoriaEsquema result = service.save(categoria1);

        assertNotNull(result);
        assertEquals("Ficción", result.getCategoria());
        verify(repository, times(1)).save(categoria1);
    }

    @Test
    @DisplayName("Debería buscar categorías por nombre")
    void testFindByCategoria() {
        List<CategoriaEsquema> categorias = Arrays.asList(categoria1);
        when(repository.findByCategoria("Ficción")).thenReturn(categorias);

        List<CategoriaEsquema> result = service.findByCategoria("Ficción");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Ficción", result.get(0).getCategoria());
        verify(repository, times(1)).findByCategoria("Ficción");
    }

    @Test
    @DisplayName("Debería verificar si existe una categoría por nombre")
    void testExistePorCategoria() {
        when(repository.existsByCategoria("Ficción")).thenReturn(true);

        boolean result = service.existePorCategoria("Ficción");

        assertTrue(result);
        verify(repository, times(1)).existsByCategoria("Ficción");
    }

    @Test
    @DisplayName("Debería retornar false cuando no existe categoría con el nombre")
    void testExistePorCategoriaNoExistente() {
        when(repository.existsByCategoria("Categoría Inexistente")).thenReturn(false);

        boolean result = service.existePorCategoria("Categoría Inexistente");

        assertFalse(result);
        verify(repository, times(1)).existsByCategoria("Categoría Inexistente");
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no encuentra categorías por nombre")
    void testFindByCategoriaNoExistente() {
        when(repository.findByCategoria("Categoría Inexistente")).thenReturn(Arrays.asList());

        List<CategoriaEsquema> result = service.findByCategoria("Categoría Inexistente");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByCategoria("Categoría Inexistente");
    }

    @Test
    @DisplayName("Debería actualizar una categoría existente")
    void testActualizarCategoria() {
        categoria1.setCategoria("Ficción Histórica");
        when(repository.save(categoria1)).thenReturn(categoria1);

        CategoriaEsquema result = service.save(categoria1);

        assertNotNull(result);
        assertEquals("Ficción Histórica", result.getCategoria());
        verify(repository, times(1)).save(categoria1);
    }
}
