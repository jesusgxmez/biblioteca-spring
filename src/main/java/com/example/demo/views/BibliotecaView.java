package com.example.demo.views;

import com.example.demo.entities.CategoriaEsquema;
import com.example.demo.entities.LibroEsquema;
import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.services.CategoriaEsquemaService;
import com.example.demo.services.LibroEsquemaService;
import com.example.demo.services.UsuarioEsquemaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Route("biblioteca")
public class BibliotecaView extends VerticalLayout {

    public BibliotecaView(LibroEsquemaService libroService,
                          UsuarioEsquemaService usuarioService,
                          CategoriaEsquemaService categoriaService) {

        setPadding(true);
        setSpacing(true);

        add(new H2("Gestión de Biblioteca"));

        // Componentes que necesitan refrescarse al añadir datos
        ComboBox<UsuarioEsquema> usuarioCombo = new ComboBox<>("Seleccionar Dueño");
        ComboBox<CategoriaEsquema> categoriaCombo = new ComboBox<>("Seleccionar Categoría");
        Grid<LibroEsquema> gridLibros = new Grid<>(LibroEsquema.class, false);

        // --- 1. SECCIÓN USUARIOS (1:N) ---
        add(new H4("Paso 1: Crear Dueño"));
        TextField nombreUsuario = new TextField("Nombre");
        TextField emailUsuario = new TextField("Email");
        Button btnCrearUsuario = new Button("Registrar Dueño", e -> {
            if (!nombreUsuario.isEmpty()) {
                UsuarioEsquema u = new UsuarioEsquema();
                u.setNombre(nombreUsuario.getValue());
                u.setEmail(emailUsuario.getValue());
                usuarioService.save(u);

                // Actualizamos el desplegable de dueños
                usuarioCombo.setItems(usuarioService.findAll());

                Notification.show("Usuario creado correctamente");
                nombreUsuario.clear(); emailUsuario.clear();
            }
        });
        add(new HorizontalLayout(nombreUsuario, emailUsuario, btnCrearUsuario));

        // --- 2. SECCIÓN CATEGORÍAS (N:M) ---
        add(new H4("Paso 2: Crear Categoría"));
        TextField nombreCat = new TextField("Categoría");
        Button btnCat = new Button("Añadir", e -> {
            if (!nombreCat.isEmpty()) {
                CategoriaEsquema c = new CategoriaEsquema();
                c.setCategoria(nombreCat.getValue());
                categoriaService.save(c);

                // Actualizamos el desplegable de categorías
                categoriaCombo.setItems(categoriaService.findAll());

                Notification.show("Categoría guardada");
                nombreCat.clear();
            }
        });
        add(new HorizontalLayout(nombreCat, btnCat));

        // --- 3. SECCIÓN LIBROS ---
        add(new H4("Paso 3: Registrar Libro"));
        TextField titulo = new TextField("Título");
        TextField autor = new TextField("Autor");

        usuarioCombo.setItems(usuarioService.findAll());
        usuarioCombo.setItemLabelGenerator(u -> u.getNombre() != null ? u.getNombre() : "Sin nombre");

        categoriaCombo.setItems(categoriaService.findAll());
        categoriaCombo.setItemLabelGenerator(c -> c.getCategoria() != null ? c.getCategoria() : "Sin nombre");

        Button guardarLibro = new Button("Guardar Libro", e -> {
            try {
                if (usuarioCombo.getValue() == null) {
                    Notification.show("Error: Debes seleccionar un dueño");
                    return;
                }

                LibroEsquema libro = new LibroEsquema();
                libro.setTitulo(titulo.getValue());
                libro.setAutor(autor.getValue());
                libro.setUsuario(usuarioCombo.getValue());

                // Inicializamos la lista para evitar NullPointerException
                libro.setCategorias(new ArrayList<>());
                if (categoriaCombo.getValue() != null) {
                    libro.getCategorias().add(categoriaCombo.getValue());
                }

                libroService.save(libro);
                gridLibros.setItems(libroService.findAll());

                titulo.clear(); autor.clear();
                Notification.show("Libro registrado con éxito");
            } catch (Exception ex) {
                Notification.show("Error al guardar: " + ex.getMessage());
            }
        });

        // --- CONFIGURACIÓN DEL GRID (Aquí ocurría el error de Sesión) ---
        gridLibros.addColumn(LibroEsquema::getTitulo).setHeader("Título");
        gridLibros.addColumn(l -> l.getUsuario() != null ? l.getUsuario().getNombre() : "N/A").setHeader("Dueño");

        // Columna de categorías protegida contra errores de carga diferida (Lazy)
        gridLibros.addColumn(l -> {
            try {
                if (l.getCategorias() == null || l.getCategorias().isEmpty()) {
                    return "Sin categorías";
                }
                return l.getCategorias().stream()
                        .map(CategoriaEsquema::getCategoria)
                        .collect(Collectors.joining(", "));
            } catch (Exception err) {
                return "Error carga (usa EAGER)";
            }
        }).setHeader("Categorías");

        add(new HorizontalLayout(titulo, autor, usuarioCombo, categoriaCombo, guardarLibro), gridLibros);

        // Carga inicial de datos
        gridLibros.setItems(libroService.findAll());
    }
}