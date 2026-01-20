package com.example.demo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas unitarias para LibroEsquema")
@SuppressWarnings("ConstantConditions")
class LibroEsquemaTest {

    private LibroEsquema libro;

    @BeforeEach
    void setUp() {
        libro = new LibroEsquema();
    }

    @Test
    @DisplayName("Debería crear un libro vacío")
    void testCrearLibroVacio() {
        assertNotNull(libro);
        assertNull(libro.getId());
        assertNull(libro.getTitulo());
        assertNull(libro.getAutor());
        assertNull(libro.getImagenUrl());
        assertEquals(0, libro.getPaginasTotales());
        assertEquals(0, libro.getPaginasLeidas());
        assertNull(libro.getDescripcion());
        assertNull(libro.getUsuario());
        assertNotNull(libro.getCategorias());
        assertTrue(libro.getCategorias().isEmpty());
    }

    @Test
    @DisplayName("Debería establecer y obtener el ID correctamente")
    void testSetGetId() {
        Long expectedId = 1L;
        libro.setId(expectedId);
        assertEquals(expectedId, libro.getId());
    }

    @Test
    @DisplayName("Debería establecer y obtener el título correctamente")
    void testSetGetTitulo() {
        String expectedTitulo = "Don Quijote de la Mancha";
        libro.setTitulo(expectedTitulo);
        assertEquals(expectedTitulo, libro.getTitulo());
    }

    @Test
    @DisplayName("Debería establecer y obtener el autor correctamente")
    void testSetGetAutor() {
        String expectedAutor = "Miguel de Cervantes";
        libro.setAutor(expectedAutor);
        assertEquals(expectedAutor, libro.getAutor());
    }

    @Test
    @DisplayName("Debería establecer y obtener la URL de imagen correctamente")
    void testSetGetImagenUrl() {
        String expectedUrl = "https://ejemplo.com/imagen.jpg";
        libro.setImagenUrl(expectedUrl);
        assertEquals(expectedUrl, libro.getImagenUrl());
    }

    @Test
    @DisplayName("Debería establecer y obtener páginas totales correctamente")
    void testSetGetPaginasTotales() {
        Integer expectedPaginas = 500;
        libro.setPaginasTotales(expectedPaginas);
        assertEquals(expectedPaginas, libro.getPaginasTotales());
    }

    @Test
    @DisplayName("Debería establecer y obtener páginas leídas correctamente")
    void testSetGetPaginasLeidas() {
        Integer expectedPaginasLeidas = 250;
        libro.setPaginasLeidas(expectedPaginasLeidas);
        assertEquals(expectedPaginasLeidas, libro.getPaginasLeidas());
    }

    @Test
    @DisplayName("Debería calcular el progreso de lectura correctamente")
    void testProgresoLectura() {
        libro.setPaginasTotales(100);
        libro.setPaginasLeidas(50);

        // El progreso sería 50%
        double progreso = (libro.getPaginasLeidas().doubleValue() / libro.getPaginasTotales()) * 100;
        assertEquals(50.0, progreso, 0.01);
    }

    @Test
    @DisplayName("Debería establecer y obtener la descripción correctamente")
    void testSetGetDescripcion() {
        String expectedDescripcion = "Una obra maestra de la literatura española que narra las aventuras de Don Quijote y Sancho Panza.";
        libro.setDescripcion(expectedDescripcion);
        assertEquals(expectedDescripcion, libro.getDescripcion());
    }

    @Test
    @DisplayName("Debería manejar descripciones largas (hasta 2000 caracteres)")
    void testDescripcionLarga() {
        StringBuilder descripcionLarga = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            descripcionLarga.append("0123456789");
        }
        String expectedDescripcion = descripcionLarga.toString();

        libro.setDescripcion(expectedDescripcion);

        assertEquals(2000, libro.getDescripcion().length());
        assertEquals(expectedDescripcion, libro.getDescripcion());
    }

    @Test
    @DisplayName("Debería establecer y obtener el usuario correctamente")
    void testSetGetUsuario() {
        UsuarioEsquema usuario = new UsuarioEsquema();
        usuario.setId(1L);
        usuario.setNombre("Juan Pérez");

        libro.setUsuario(usuario);

        assertNotNull(libro.getUsuario());
        assertEquals(usuario, libro.getUsuario());
        assertEquals("Juan Pérez", libro.getUsuario().getNombre());
    }

    @Test
    @DisplayName("Debería manejar correctamente la relación ManyToOne con Usuario")
    void testRelacionManyToOneConUsuario() {
        UsuarioEsquema usuario = new UsuarioEsquema();
        usuario.setNombre("María García");

        libro.setUsuario(usuario);

        assertEquals(usuario, libro.getUsuario());
        assertEquals("María García", libro.getUsuario().getNombre());
    }

    @Test
    @DisplayName("Debería permitir establecer usuario como null")
    void testSetUsuarioNull() {
        UsuarioEsquema usuario = new UsuarioEsquema();
        libro.setUsuario(usuario);
        assertNotNull(libro.getUsuario());

        libro.setUsuario(null);
        assertNull(libro.getUsuario());
    }

    @Test
    @DisplayName("Debería inicializar la lista de categorías como vacía")
    void testListaCategoriasInicializada() {
        List<CategoriaEsquema> categorias = libro.getCategorias();
        assertNotNull(categorias);
        assertTrue(categorias.isEmpty());
        assertEquals(0, categorias.size());
    }

    @Test
    @DisplayName("Debería agregar una categoría a la lista correctamente")
    void testAgregarCategoria() {
        CategoriaEsquema categoria = new CategoriaEsquema();
        categoria.setId(1L);
        categoria.setCategoria("Ficción");

        libro.getCategorias().add(categoria);

        assertEquals(1, libro.getCategorias().size());
        assertTrue(libro.getCategorias().contains(categoria));
        assertEquals("Ficción", libro.getCategorias().get(0).getCategoria());
    }

    @Test
    @DisplayName("Debería agregar múltiples categorías a la lista")
    void testAgregarMultiplesCategorias() {
        CategoriaEsquema categoria1 = new CategoriaEsquema();
        categoria1.setId(1L);
        categoria1.setCategoria("Ficción");

        CategoriaEsquema categoria2 = new CategoriaEsquema();
        categoria2.setId(2L);
        categoria2.setCategoria("Aventura");

        CategoriaEsquema categoria3 = new CategoriaEsquema();
        categoria3.setId(3L);
        categoria3.setCategoria("Clásicos");

        libro.getCategorias().add(categoria1);
        libro.getCategorias().add(categoria2);
        libro.getCategorias().add(categoria3);

        assertEquals(3, libro.getCategorias().size());
        assertTrue(libro.getCategorias().contains(categoria1));
        assertTrue(libro.getCategorias().contains(categoria2));
        assertTrue(libro.getCategorias().contains(categoria3));
    }

    @Test
    @DisplayName("Debería eliminar una categoría de la lista")
    void testEliminarCategoria() {
        CategoriaEsquema categoria = new CategoriaEsquema();
        categoria.setId(1L);
        categoria.setCategoria("Terror");

        libro.getCategorias().add(categoria);
        assertEquals(1, libro.getCategorias().size());

        libro.getCategorias().remove(categoria);
        assertEquals(0, libro.getCategorias().size());
        assertFalse(libro.getCategorias().contains(categoria));
    }

    @Test
    @DisplayName("Debería establecer una nueva lista de categorías")
    void testSetListaCategorias() {
        List<CategoriaEsquema> nuevaLista = new ArrayList<>();

        CategoriaEsquema categoria1 = new CategoriaEsquema();
        categoria1.setCategoria("Romance");
        nuevaLista.add(categoria1);

        CategoriaEsquema categoria2 = new CategoriaEsquema();
        categoria2.setCategoria("Drama");
        nuevaLista.add(categoria2);

        libro.setCategorias(nuevaLista);

        assertEquals(2, libro.getCategorias().size());
        assertEquals(nuevaLista, libro.getCategorias());
    }

    @Test
    @DisplayName("Debería crear un libro completo con todos los campos")
    void testCrearLibroCompletoConTodosCampos() {
        libro.setId(1L);
        libro.setTitulo("Cien años de soledad");
        libro.setAutor("Gabriel García Márquez");
        libro.setImagenUrl("https://ejemplo.com/cien-anos.jpg");
        libro.setPaginasTotales(471);
        libro.setPaginasLeidas(235);
        libro.setDescripcion("Una obra maestra del realismo mágico");

        UsuarioEsquema usuario = new UsuarioEsquema();
        usuario.setId(1L);
        usuario.setNombre("Carlos Ruiz");
        libro.setUsuario(usuario);

        CategoriaEsquema categoria1 = new CategoriaEsquema();
        categoria1.setId(1L);
        categoria1.setCategoria("Realismo Mágico");

        CategoriaEsquema categoria2 = new CategoriaEsquema();
        categoria2.setId(2L);
        categoria2.setCategoria("Literatura Latinoamericana");

        libro.getCategorias().add(categoria1);
        libro.getCategorias().add(categoria2);

        // Verificaciones
        assertEquals(1L, libro.getId());
        assertEquals("Cien años de soledad", libro.getTitulo());
        assertEquals("Gabriel García Márquez", libro.getAutor());
        assertEquals("https://ejemplo.com/cien-anos.jpg", libro.getImagenUrl());
        assertEquals(471, libro.getPaginasTotales());
        assertEquals(235, libro.getPaginasLeidas());
        assertEquals("Una obra maestra del realismo mágico", libro.getDescripcion());
        assertNotNull(libro.getUsuario());
        assertEquals("Carlos Ruiz", libro.getUsuario().getNombre());
        assertEquals(2, libro.getCategorias().size());
        assertEquals("Realismo Mágico", libro.getCategorias().get(0).getCategoria());
        assertEquals("Literatura Latinoamericana", libro.getCategorias().get(1).getCategoria());
    }

    @Test
    @DisplayName("Debería manejar páginas totales igual a cero")
    void testPaginasTotalesCero() {
        libro.setPaginasTotales(0);
        assertEquals(0, libro.getPaginasTotales());
    }

    @Test
    @DisplayName("Debería manejar páginas leídas mayor que páginas totales")
    void testPaginasLeidasMayorQueTotales() {
        libro.setPaginasTotales(100);
        libro.setPaginasLeidas(150);

        assertTrue(libro.getPaginasLeidas() > libro.getPaginasTotales());
    }

    @Test
    @DisplayName("Debería verificar si el libro está completo (100% leído)")
    void testLibroLecturaCompleta() {
        libro.setPaginasTotales(300);
        libro.setPaginasLeidas(300);

        assertEquals(libro.getPaginasTotales(), libro.getPaginasLeidas());
    }

    @Test
    @DisplayName("Debería manejar título con caracteres especiales")
    void testTituloConCaracteresEspeciales() {
        String tituloEspecial = "¿Quién movió mi queso? & ¡El éxito!";
        libro.setTitulo(tituloEspecial);
        assertEquals(tituloEspecial, libro.getTitulo());
    }

    @Test
    @DisplayName("Debería limpiar la lista de categorías")
    void testLimpiarListaCategorias() {
        CategoriaEsquema categoria1 = new CategoriaEsquema();
        CategoriaEsquema categoria2 = new CategoriaEsquema();

        List<CategoriaEsquema> categorias = libro.getCategorias();
        categorias.add(categoria1);
        categorias.add(categoria2);
        assertEquals(2, categorias.size());

        categorias.clear();
        assertTrue(categorias.isEmpty());
        assertEquals(0, categorias.size());
    }

    @Test
    @DisplayName("Debería usar valores por defecto para páginas")
    void testValoresPorDefecto() {
        LibroEsquema nuevoLibro = new LibroEsquema();
        assertEquals(0, nuevoLibro.getPaginasTotales());
        assertEquals(0, nuevoLibro.getPaginasLeidas());
    }
}
