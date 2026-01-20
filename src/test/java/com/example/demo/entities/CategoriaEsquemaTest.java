package com.example.demo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas unitarias para CategoriaEsquema")
@SuppressWarnings("ConstantConditions")
class CategoriaEsquemaTest {

    private CategoriaEsquema categoria;

    @BeforeEach
    void setUp() {
        categoria = new CategoriaEsquema();
    }

    @Test
    @DisplayName("Debería crear una categoría vacía")
    void testCrearCategoriaVacia() {
        assertNotNull(categoria);
        assertNull(categoria.getId());
        assertNull(categoria.getCategoria());
        assertNotNull(categoria.getLibros());
        assertTrue(categoria.getLibros().isEmpty());
    }

    @Test
    @DisplayName("Debería establecer y obtener el ID correctamente")
    void testSetGetId() {
        Long expectedId = 1L;
        categoria.setId(expectedId);
        assertEquals(expectedId, categoria.getId());
    }

    @Test
    @DisplayName("Debería establecer y obtener la categoría correctamente")
    void testSetGetCategoria() {
        String expectedCategoria = "Ficción";
        categoria.setCategoria(expectedCategoria);
        assertEquals(expectedCategoria, categoria.getCategoria());
    }

    @Test
    @DisplayName("Debería manejar nombres de categoría con espacios")
    void testCategoriaNombreConEspacios() {
        String nombreCategoria = "Ciencia Ficción";
        categoria.setCategoria(nombreCategoria);
        assertEquals(nombreCategoria, categoria.getCategoria());
    }

    @Test
    @DisplayName("Debería manejar nombres de categoría con caracteres especiales")
    void testCategoriaNombreConCaracteresEspeciales() {
        String nombreCategoria = "Ficción & Fantasía";
        categoria.setCategoria(nombreCategoria);
        assertEquals(nombreCategoria, categoria.getCategoria());
    }

    @Test
    @DisplayName("Debería manejar nombres de categoría en diferentes idiomas")
    void testCategoriaNombreEnDiferentesIdiomas() {
        String nombreCategoria = "Histórico/História";
        categoria.setCategoria(nombreCategoria);
        assertEquals(nombreCategoria, categoria.getCategoria());
    }

    @Test
    @DisplayName("Debería inicializar la lista de libros como vacía")
    void testListaLibrosInicializada() {
        List<LibroEsquema> libros = categoria.getLibros();
        assertNotNull(libros);
        assertTrue(libros.isEmpty());
        assertEquals(0, libros.size());
    }

    @Test
    @DisplayName("Debería agregar un libro a la lista correctamente")
    void testAgregarLibro() {
        LibroEsquema libro = new LibroEsquema();
        libro.setId(1L);
        libro.setTitulo("1984");
        libro.setAutor("George Orwell");

        categoria.getLibros().add(libro);

        assertEquals(1, categoria.getLibros().size());
        assertTrue(categoria.getLibros().contains(libro));
        assertEquals("1984", categoria.getLibros().get(0).getTitulo());
    }

    @Test
    @DisplayName("Debería agregar múltiples libros a la lista")
    void testAgregarMultiplesLibros() {
        LibroEsquema libro1 = new LibroEsquema();
        libro1.setId(1L);
        libro1.setTitulo("Libro 1");

        LibroEsquema libro2 = new LibroEsquema();
        libro2.setId(2L);
        libro2.setTitulo("Libro 2");

        LibroEsquema libro3 = new LibroEsquema();
        libro3.setId(3L);
        libro3.setTitulo("Libro 3");

        categoria.getLibros().add(libro1);
        categoria.getLibros().add(libro2);
        categoria.getLibros().add(libro3);

        assertEquals(3, categoria.getLibros().size());
        assertTrue(categoria.getLibros().contains(libro1));
        assertTrue(categoria.getLibros().contains(libro2));
        assertTrue(categoria.getLibros().contains(libro3));
    }

    @Test
    @DisplayName("Debería eliminar un libro de la lista")
    void testEliminarLibro() {
        LibroEsquema libro = new LibroEsquema();
        libro.setId(1L);
        libro.setTitulo("Libro a eliminar");

        categoria.getLibros().add(libro);
        assertEquals(1, categoria.getLibros().size());

        categoria.getLibros().remove(libro);
        assertEquals(0, categoria.getLibros().size());
        assertFalse(categoria.getLibros().contains(libro));
    }

    @Test
    @DisplayName("Debería establecer una nueva lista de libros")
    void testSetListaLibros() {
        List<LibroEsquema> nuevaLista = new ArrayList<>();

        LibroEsquema libro1 = new LibroEsquema();
        libro1.setTitulo("Libro A");
        nuevaLista.add(libro1);

        LibroEsquema libro2 = new LibroEsquema();
        libro2.setTitulo("Libro B");
        nuevaLista.add(libro2);

        categoria.setLibros(nuevaLista);

        assertEquals(2, categoria.getLibros().size());
        assertEquals(nuevaLista, categoria.getLibros());
    }

    @Test
    @DisplayName("Debería manejar la relación ManyToMany con libros")
    void testRelacionManyToManyConLibros() {
        categoria.setCategoria("Aventura");

        LibroEsquema libro1 = new LibroEsquema();
        libro1.setTitulo("La isla del tesoro");

        LibroEsquema libro2 = new LibroEsquema();
        libro2.setTitulo("Viaje al centro de la Tierra");

        categoria.getLibros().add(libro1);
        categoria.getLibros().add(libro2);

        // Un libro puede tener múltiples categorías
        libro1.getCategorias().add(categoria);
        libro2.getCategorias().add(categoria);

        assertEquals(2, categoria.getLibros().size());
        assertTrue(libro1.getCategorias().contains(categoria));
        assertTrue(libro2.getCategorias().contains(categoria));
    }

    @Test
    @DisplayName("Debería crear una categoría completa con libros")
    void testCategoriaCompletaConLibros() {
        categoria.setId(1L);
        categoria.setCategoria("Fantasía");

        LibroEsquema libro1 = new LibroEsquema();
        libro1.setId(1L);
        libro1.setTitulo("El Señor de los Anillos");
        libro1.setAutor("J.R.R. Tolkien");

        LibroEsquema libro2 = new LibroEsquema();
        libro2.setId(2L);
        libro2.setTitulo("Harry Potter");
        libro2.setAutor("J.K. Rowling");

        LibroEsquema libro3 = new LibroEsquema();
        libro3.setId(3L);
        libro3.setTitulo("Crónicas de Narnia");
        libro3.setAutor("C.S. Lewis");

        categoria.getLibros().add(libro1);
        categoria.getLibros().add(libro2);
        categoria.getLibros().add(libro3);

        // Verificaciones
        assertEquals(1L, categoria.getId());
        assertEquals("Fantasía", categoria.getCategoria());
        assertEquals(3, categoria.getLibros().size());
        assertEquals("El Señor de los Anillos", categoria.getLibros().get(0).getTitulo());
        assertEquals("Harry Potter", categoria.getLibros().get(1).getTitulo());
        assertEquals("Crónicas de Narnia", categoria.getLibros().get(2).getTitulo());
    }

    @Test
    @DisplayName("Debería limpiar la lista de libros")
    void testLimpiarListaLibros() {
        LibroEsquema libro1 = new LibroEsquema();
        LibroEsquema libro2 = new LibroEsquema();

        List<LibroEsquema> libros = categoria.getLibros();
        libros.add(libro1);
        libros.add(libro2);
        assertEquals(2, libros.size());

        libros.clear();
        assertTrue(libros.isEmpty());
        assertEquals(0, libros.size());
    }

    @Test
    @DisplayName("Debería verificar si un libro pertenece a la categoría")
    void testVerificarLibroEnCategoria() {
        LibroEsquema libro = new LibroEsquema();
        libro.setId(1L);
        libro.setTitulo("Libro de prueba");

        assertFalse(categoria.getLibros().contains(libro));

        categoria.getLibros().add(libro);
        assertTrue(categoria.getLibros().contains(libro));
    }

    @Test
    @DisplayName("Debería contar el número de libros en la categoría")
    void testContarLibrosEnCategoria() {
        assertEquals(0, categoria.getLibros().size());

        categoria.getLibros().add(new LibroEsquema());
        assertEquals(1, categoria.getLibros().size());

        categoria.getLibros().add(new LibroEsquema());
        categoria.getLibros().add(new LibroEsquema());
        assertEquals(3, categoria.getLibros().size());
    }

    @Test
    @DisplayName("Debería manejar categorías con acentos y ñ")
    void testCategoriaConAcentosYÑ() {
        String nombreCategoria = "Histórico Español";
        categoria.setCategoria(nombreCategoria);
        assertEquals(nombreCategoria, categoria.getCategoria());
    }

    @Test
    @DisplayName("Debería permitir categoría vacía")
    void testCategoriaVacia() {
        categoria.setCategoria("");
        assertEquals("", categoria.getCategoria());
        assertTrue(categoria.getCategoria().isEmpty());
    }

    @Test
    @DisplayName("Debería permitir categoría null")
    void testCategoriaNull() {
        categoria.setCategoria("Una categoría");
        assertNotNull(categoria.getCategoria());

        categoria.setCategoria(null);
        assertNull(categoria.getCategoria());
    }

    @Test
    @DisplayName("Debería permitir múltiples categorías con el mismo nombre de libro")
    void testMultiplesCategoriasConMismoLibro() {
        LibroEsquema libro = new LibroEsquema();
        libro.setId(1L);
        libro.setTitulo("Libro Multigenero");

        CategoriaEsquema categoria1 = new CategoriaEsquema();
        categoria1.setCategoria("Aventura");
        categoria1.getLibros().add(libro);

        CategoriaEsquema categoria2 = new CategoriaEsquema();
        categoria2.setCategoria("Misterio");
        categoria2.getLibros().add(libro);

        assertTrue(categoria1.getLibros().contains(libro));
        assertTrue(categoria2.getLibros().contains(libro));
        assertEquals(1, categoria1.getLibros().size());
        assertEquals(1, categoria2.getLibros().size());
    }

    @Test
    @DisplayName("Debería manejar nombres de categorías populares")
    void testCategoriasPopulares() {
        String[] categoriasPopulares = {
            "Ficción", "No Ficción", "Romance", "Misterio", "Thriller",
            "Ciencia Ficción", "Fantasía", "Biografía", "Historia", "Autoayuda"
        };

        for (String nombreCategoria : categoriasPopulares) {
            CategoriaEsquema cat = new CategoriaEsquema();
            cat.setCategoria(nombreCategoria);
            assertEquals(nombreCategoria, cat.getCategoria());
        }
    }
}
