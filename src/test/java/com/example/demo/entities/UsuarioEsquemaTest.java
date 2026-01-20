package com.example.demo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas unitarias para UsuarioEsquema")
@SuppressWarnings({"NonAsciiCharacters", "ConstantConditions"})
class UsuarioEsquemaTest {

    private UsuarioEsquema usuario;

    @BeforeEach
    void setUp() {
        usuario = new UsuarioEsquema();
    }

    @Test
    @DisplayName("Debería crear un usuario vacío")
    void testCrearUsuarioVacio() {
        assertNotNull(usuario);
        assertNull(usuario.getId());
        assertNull(usuario.getNombre());
        assertNull(usuario.getEmail());
        assertNull(usuario.getContraseña());
        assertNull(usuario.getPerfil());
        assertNotNull(usuario.getLibros());
        assertTrue(usuario.getLibros().isEmpty());
    }

    @Test
    @DisplayName("Debería establecer y obtener el ID correctamente")
    void testSetGetId() {
        Long expectedId = 1L;
        usuario.setId(expectedId);
        assertEquals(expectedId, usuario.getId());
    }

    @Test
    @DisplayName("Debería establecer y obtener el nombre correctamente")
    void testSetGetNombre() {
        String expectedNombre = "Juan Pérez";
        usuario.setNombre(expectedNombre);
        assertEquals(expectedNombre, usuario.getNombre());
    }

    @Test
    @DisplayName("Debería establecer y obtener el email correctamente")
    void testSetGetEmail() {
        String expectedEmail = "juan@example.com";
        usuario.setEmail(expectedEmail);
        assertEquals(expectedEmail, usuario.getEmail());
    }

    @Test
    @DisplayName("Debería establecer y obtener la contraseña correctamente")
    @SuppressWarnings("NonAsciiCharacters")
    void testSetGetContraseña() {
        String expectedContraseña = "password123";
        usuario.setContraseña(expectedContraseña);
        assertEquals(expectedContraseña, usuario.getContraseña());
    }

    @Test
    @DisplayName("Debería establecer y obtener el perfil correctamente")
    void testSetGetPerfil() {
        PerfilEsquema perfil = new PerfilEsquema();
        perfil.setId(1L);
        perfil.setBio("Biografía de prueba");
        perfil.setUsuario(usuario);

        usuario.setPerfil(perfil);

        assertNotNull(usuario.getPerfil());
        assertEquals(perfil, usuario.getPerfil());
        assertEquals("Biografía de prueba", usuario.getPerfil().getBio());
    }

    @Test
    @DisplayName("Debería manejar correctamente la relación OneToOne con Perfil")
    void testRelacionOneToOneConPerfil() {
        PerfilEsquema perfil = new PerfilEsquema();
        perfil.setUsuario(usuario);
        usuario.setPerfil(perfil);

        assertEquals(usuario, perfil.getUsuario());
        assertEquals(perfil, usuario.getPerfil());
    }

    @Test
    @DisplayName("Debería permitir establecer perfil como null")
    void testSetPerfilNull() {
        PerfilEsquema perfil = new PerfilEsquema();
        usuario.setPerfil(perfil);
        assertNotNull(usuario.getPerfil());

        usuario.setPerfil(null);
        assertNull(usuario.getPerfil());
    }

    @Test
    @DisplayName("Debería inicializar la lista de libros como vacía")
    void testListaLibrosInicializada() {
        assertNotNull(usuario.getLibros());
        assertTrue(usuario.getLibros().isEmpty());
        assertEquals(0, usuario.getLibros().size());
    }

    @Test
    @DisplayName("Debería agregar un libro a la lista correctamente")
    void testAgregarLibro() {
        LibroEsquema libro = new LibroEsquema();
        libro.setId(1L);
        libro.setTitulo("El Quijote");
        libro.setAutor("Miguel de Cervantes");
        libro.setUsuario(usuario);

        usuario.getLibros().add(libro);

        assertEquals(1, usuario.getLibros().size());
        assertTrue(usuario.getLibros().contains(libro));
        assertEquals("El Quijote", usuario.getLibros().get(0).getTitulo());
    }

    @Test
    @DisplayName("Debería agregar múltiples libros a la lista")
    void testAgregarMultiplesLibros() {
        LibroEsquema libro1 = new LibroEsquema();
        libro1.setId(1L);
        libro1.setTitulo("Libro 1");
        libro1.setUsuario(usuario);

        LibroEsquema libro2 = new LibroEsquema();
        libro2.setId(2L);
        libro2.setTitulo("Libro 2");
        libro2.setUsuario(usuario);

        LibroEsquema libro3 = new LibroEsquema();
        libro3.setId(3L);
        libro3.setTitulo("Libro 3");
        libro3.setUsuario(usuario);

        usuario.getLibros().add(libro1);
        usuario.getLibros().add(libro2);
        usuario.getLibros().add(libro3);

        assertEquals(3, usuario.getLibros().size());
        assertTrue(usuario.getLibros().contains(libro1));
        assertTrue(usuario.getLibros().contains(libro2));
        assertTrue(usuario.getLibros().contains(libro3));
    }

    @Test
    @DisplayName("Debería eliminar un libro de la lista")
    void testEliminarLibro() {
        LibroEsquema libro = new LibroEsquema();
        libro.setId(1L);
        libro.setTitulo("Libro a eliminar");
        libro.setUsuario(usuario);

        usuario.getLibros().add(libro);
        assertEquals(1, usuario.getLibros().size());

        usuario.getLibros().remove(libro);
        assertEquals(0, usuario.getLibros().size());
        assertFalse(usuario.getLibros().contains(libro));
    }

    @Test
    @DisplayName("Debería establecer una nueva lista de libros")
    void testSetListaLibros() {
        List<LibroEsquema> nuevaLista = new ArrayList<>();

        LibroEsquema libro1 = new LibroEsquema();
        libro1.setTitulo("Libro 1");
        nuevaLista.add(libro1);

        LibroEsquema libro2 = new LibroEsquema();
        libro2.setTitulo("Libro 2");
        nuevaLista.add(libro2);

        usuario.setLibros(nuevaLista);

        assertEquals(2, usuario.getLibros().size());
        assertEquals(nuevaLista, usuario.getLibros());
    }

    @Test
    @DisplayName("Debería mantener la relación bidireccional con libros")
    void testRelacionBidireccionalConLibros() {
        LibroEsquema libro = new LibroEsquema();
        libro.setTitulo("Libro de prueba");
        libro.setUsuario(usuario);

        usuario.getLibros().add(libro);

        assertEquals(usuario, libro.getUsuario());
        assertTrue(usuario.getLibros().contains(libro));
    }

    @Test
    @DisplayName("Debería generar toString correctamente sin ID")
    void testToStringSinId() {
        usuario.setNombre("Carlos");

        String resultado = usuario.toString();

        assertTrue(resultado.contains("UsuarioEsquema"));
        assertTrue(resultado.contains("id=null"));
        assertTrue(resultado.contains("nombre='Carlos'"));
    }

    @Test
    @DisplayName("Debería generar toString correctamente con ID")
    void testToStringConId() {
        usuario.setId(5L);
        usuario.setNombre("María");

        String resultado = usuario.toString();

        assertTrue(resultado.contains("UsuarioEsquema"));
        assertTrue(resultado.contains("id=5"));
        assertTrue(resultado.contains("nombre='María'"));
    }

    @Test
    @DisplayName("Debería crear un usuario completo con todos los campos")
    @SuppressWarnings("NonAsciiCharacters")
    void testUsuarioCompleto() {
        usuario.setId(1L);
        usuario.setNombre("Ana García");
        usuario.setEmail("ana@example.com");
        usuario.setContraseña("securePassword");

        PerfilEsquema perfil = new PerfilEsquema();
        perfil.setId(1L);
        perfil.setBio("Amante de la lectura");
        perfil.setUsuario(usuario);
        usuario.setPerfil(perfil);

        LibroEsquema libro1 = new LibroEsquema();
        libro1.setId(1L);
        libro1.setTitulo("Cien años de soledad");
        libro1.setAutor("Gabriel García Márquez");
        libro1.setUsuario(usuario);

        LibroEsquema libro2 = new LibroEsquema();
        libro2.setId(2L);
        libro2.setTitulo("1984");
        libro2.setAutor("George Orwell");
        libro2.setUsuario(usuario);

        usuario.getLibros().add(libro1);
        usuario.getLibros().add(libro2);

        // Verificaciones
        assertEquals(1L, usuario.getId());
        assertEquals("Ana García", usuario.getNombre());
        assertEquals("ana@example.com", usuario.getEmail());
        assertEquals("securePassword", usuario.getContraseña());
        assertNotNull(usuario.getPerfil());
        assertEquals("Amante de la lectura", usuario.getPerfil().getBio());
        assertEquals(2, usuario.getLibros().size());
        assertEquals("Cien años de soledad", usuario.getLibros().get(0).getTitulo());
        assertEquals("1984", usuario.getLibros().get(1).getTitulo());
    }

    @Test
    @DisplayName("Debería manejar email con formato especial")
    void testEmailConCaracteresEspeciales() {
        String emailEspecial = "usuario.test+123@dominio-ejemplo.com.es";
        usuario.setEmail(emailEspecial);
        assertEquals(emailEspecial, usuario.getEmail());
    }

    @Test
    @DisplayName("Debería permitir nombre con caracteres acentuados")
    void testNombreConAcentos() {
        String nombreConAcentos = "José María Ñoño";
        usuario.setNombre(nombreConAcentos);
        assertEquals(nombreConAcentos, usuario.getNombre());
    }

    @Test
    @DisplayName("Debería limpiar la lista de libros")
    void testLimpiarListaLibros() {
        LibroEsquema libro1 = new LibroEsquema();
        LibroEsquema libro2 = new LibroEsquema();

        List<LibroEsquema> libros = usuario.getLibros();
        libros.add(libro1);
        libros.add(libro2);
        assertEquals(2, libros.size());

        libros.clear();
        assertTrue(libros.isEmpty());
        assertEquals(0, libros.size());
    }
}
