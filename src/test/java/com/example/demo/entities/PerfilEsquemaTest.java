package com.example.demo.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas unitarias para PerfilEsquema")
class PerfilEsquemaTest {

    private PerfilEsquema perfil;

    @BeforeEach
    void setUp() {
        perfil = new PerfilEsquema();
    }

    @Test
    @DisplayName("Debería crear un perfil vacío")
    void testCrearPerfilVacio() {
        assertNotNull(perfil);
        assertNull(perfil.getId());
        assertNull(perfil.getBio());
        assertNull(perfil.getUsuario());
    }

    @Test
    @DisplayName("Debería establecer y obtener el ID correctamente")
    void testSetGetId() {
        Long expectedId = 1L;
        perfil.setId(expectedId);
        assertEquals(expectedId, perfil.getId());
    }

    @Test
    @DisplayName("Debería establecer y obtener la biografía correctamente")
    void testSetGetBio() {
        String expectedBio = "Amante de la lectura y la literatura clásica";
        perfil.setBio(expectedBio);
        assertEquals(expectedBio, perfil.getBio());
    }

    @Test
    @DisplayName("Debería manejar biografías vacías")
    void testBioVacia() {
        perfil.setBio("");
        assertEquals("", perfil.getBio());
        assertTrue(perfil.getBio().isEmpty());
    }

    @Test
    @DisplayName("Debería manejar biografías largas")
    void testBioLarga() {
        StringBuilder bioLarga = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            bioLarga.append("Me encanta leer libros de todo tipo. ");
        }
        String expectedBio = bioLarga.toString();

        perfil.setBio(expectedBio);

        assertEquals(expectedBio, perfil.getBio());
        assertTrue(perfil.getBio().length() > 1000);
    }

    @Test
    @DisplayName("Debería establecer y obtener el usuario correctamente")
    void testSetGetUsuario() {
        UsuarioEsquema usuario = new UsuarioEsquema();
        usuario.setId(1L);
        usuario.setNombre("Ana López");
        usuario.setEmail("ana@example.com");

        perfil.setUsuario(usuario);

        assertNotNull(perfil.getUsuario());
        assertEquals(usuario, perfil.getUsuario());
        assertEquals("Ana López", perfil.getUsuario().getNombre());
        assertEquals("ana@example.com", perfil.getUsuario().getEmail());
    }

    @Test
    @DisplayName("Debería manejar correctamente la relación OneToOne con Usuario")
    void testRelacionOneToOneConUsuario() {
        UsuarioEsquema usuario = new UsuarioEsquema();
        usuario.setId(1L);
        usuario.setNombre("Pedro Martínez");

        perfil.setUsuario(usuario);
        usuario.setPerfil(perfil);

        assertEquals(usuario, perfil.getUsuario());
        assertEquals(perfil, usuario.getPerfil());
        assertEquals("Pedro Martínez", perfil.getUsuario().getNombre());
    }

    @Test
    @DisplayName("Debería permitir establecer usuario como null")
    void testSetUsuarioNull() {
        UsuarioEsquema usuario = new UsuarioEsquema();
        perfil.setUsuario(usuario);
        assertNotNull(perfil.getUsuario());

        perfil.setUsuario(null);
        assertNull(perfil.getUsuario());
    }

    @Test
    @DisplayName("Debería crear un perfil completo con todos los campos")
    void testPerfilCompleto() {
        perfil.setId(1L);
        perfil.setBio("Escritor y lector apasionado. Me encantan los clásicos de la literatura universal y la ciencia ficción moderna.");

        UsuarioEsquema usuario = new UsuarioEsquema();
        usuario.setId(1L);
        usuario.setNombre("Carlos Ruiz");
        usuario.setEmail("carlos@example.com");

        perfil.setUsuario(usuario);
        usuario.setPerfil(perfil);

        // Verificaciones
        assertEquals(1L, perfil.getId());
        assertTrue(perfil.getBio().contains("literatura universal"));
        assertTrue(perfil.getBio().contains("ciencia ficción"));
        assertNotNull(perfil.getUsuario());
        assertEquals("Carlos Ruiz", perfil.getUsuario().getNombre());
        assertEquals("carlos@example.com", perfil.getUsuario().getEmail());
        assertEquals(perfil, usuario.getPerfil());
    }

    @Test
    @DisplayName("Debería manejar biografía con caracteres especiales")
    void testBioConCaracteresEspeciales() {
        String bioEspecial = "¡Hola! Soy un lector apasionado de la literatura española, francesa & italiana. ¿Te gusta leer? #Lectura";
        perfil.setBio(bioEspecial);
        assertEquals(bioEspecial, perfil.getBio());
    }

    @Test
    @DisplayName("Debería manejar biografía con saltos de línea")
    void testBioConSaltosDeLinea() {
        String bioConSaltos = "Línea 1\nLínea 2\nLínea 3";
        perfil.setBio(bioConSaltos);
        assertEquals(bioConSaltos, perfil.getBio());
        assertTrue(perfil.getBio().contains("\n"));
    }

    @Test
    @DisplayName("Debería permitir actualizar la biografía")
    void testActualizarBio() {
        perfil.setBio("Biografía inicial");
        assertEquals("Biografía inicial", perfil.getBio());

        perfil.setBio("Biografía actualizada");
        assertEquals("Biografía actualizada", perfil.getBio());
        assertNotEquals("Biografía inicial", perfil.getBio());
    }

    @Test
    @DisplayName("Debería manejar biografía null")
    void testBioNull() {
        perfil.setBio("Una biografía");
        assertNotNull(perfil.getBio());

        perfil.setBio(null);
        assertNull(perfil.getBio());
    }

    @Test
    @DisplayName("Debería verificar que el perfil pertenece a un único usuario")
    void testPerfilUnicoUsuario() {
        UsuarioEsquema usuario1 = new UsuarioEsquema();
        usuario1.setId(1L);
        usuario1.setNombre("Usuario 1");

        perfil.setUsuario(usuario1);
        assertEquals(usuario1, perfil.getUsuario());

        // Cambiar a otro usuario
        UsuarioEsquema usuario2 = new UsuarioEsquema();
        usuario2.setId(2L);
        usuario2.setNombre("Usuario 2");

        perfil.setUsuario(usuario2);
        assertEquals(usuario2, perfil.getUsuario());
        assertNotEquals(usuario1, perfil.getUsuario());
    }

    @Test
    @DisplayName("Debería verificar la relación bidireccional completa")
    void testRelacionBidireccionalCompleta() {
        UsuarioEsquema usuario = new UsuarioEsquema();
        usuario.setId(1L);
        usuario.setNombre("Test Usuario");

        perfil.setId(1L);
        perfil.setBio("Test Bio");
        perfil.setUsuario(usuario);

        usuario.setPerfil(perfil);

        // Verificar que la relación es bidireccional
        assertEquals(usuario, perfil.getUsuario());
        assertEquals(perfil, usuario.getPerfil());
        assertEquals(perfil.getUsuario().getPerfil(), perfil);
        assertEquals(usuario.getPerfil().getUsuario(), usuario);
    }

    @Test
    @DisplayName("Debería manejar biografía con emojis y caracteres Unicode")
    void testBioConEmojis() {
        String bioConEmojis = "📚 Amante de los libros 📖 | Lector empedernido 🤓";
        perfil.setBio(bioConEmojis);
        assertEquals(bioConEmojis, perfil.getBio());
    }

    @Test
    @DisplayName("Debería manejar biografía con HTML o código")
    void testBioConHTML() {
        String bioConHTML = "<p>Me gusta leer</p> & escribir código";
        perfil.setBio(bioConHTML);
        assertEquals(bioConHTML, perfil.getBio());
    }
}
