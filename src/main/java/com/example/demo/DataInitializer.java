package com.example.demo;

import com.example.demo.entities.CategoriaEsquema;
import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.services.CategoriaEsquemaService;
import com.example.demo.services.UsuarioEsquemaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioEsquemaService usuarioService;
    private final CategoriaEsquemaService categoriaService;

    public DataInitializer(UsuarioEsquemaService usuarioService, CategoriaEsquemaService categoriaService) {
        this.usuarioService = usuarioService;
        this.categoriaService = categoriaService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Creamos los usuarios que definimos en SecurityConfig
        if (usuarioService.buscarPorNombre("jesus").isEmpty()) {
            UsuarioEsquema u1 = new UsuarioEsquema();
            u1.setNombre("jesus");
            u1.setEmail("jesus@example.com");
            u1.setContraseña("1234"); // Solo informativo, el login usa el de SecurityConfig
            usuarioService.save(u1);
        }

        if (usuarioService.buscarPorNombre("paco").isEmpty()) {
            UsuarioEsquema u2 = new UsuarioEsquema();
            u2.setNombre("paco");
            u2.setEmail("paco@example.com");
            u2.setContraseña("1234");
            usuarioService.save(u2);
        }

        // Creamos un par de categorías de ejemplo para el ComboBox
        if (!categoriaService.existePorCategoria("Novela")) {
            CategoriaEsquema c1 = new CategoriaEsquema();
            c1.setCategoria("Novela");
            categoriaService.save(c1);

            CategoriaEsquema c2 = new CategoriaEsquema();
            c2.setCategoria("Programación");
            categoriaService.save(c2);
        }

        System.out.println("¡Datos de prueba cargados correctamente!");
    }
}