package com.example.demo.views;

import com.example.demo.entities.CategoriaEsquema;
import com.example.demo.entities.LibroEsquema;
import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.security.SecurityService;
import com.example.demo.services.CategoriaEsquemaService;
import com.example.demo.services.LibroEsquemaService;
import com.example.demo.services.UsuarioEsquemaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "mis-libros")
@PermitAll
public class MisLibrosView extends VerticalLayout {

    private final LibroEsquemaService libroService;
    private final String username;
    private final Grid<LibroEsquema> grid = new Grid<>(LibroEsquema.class, false);

    public MisLibrosView(LibroEsquemaService libroService,
                         UsuarioEsquemaService usuarioService,
                         CategoriaEsquemaService categoriaService,
                         SecurityService securityService) {

        this.libroService = libroService;
        this.username = securityService.getAuthenticatedUsername();

        setPadding(true);
        add(new H2("Mis Libros (" + username + ")"));

        // --- FORMULARIO DE ENTRADA ---
        TextField titulo = new TextField("Título");
        TextField autor = new TextField("Autor");
        ComboBox<CategoriaEsquema> catCombo = new ComboBox<>("Categoría");
        catCombo.setItems(categoriaService.findAll());
        catCombo.setItemLabelGenerator(CategoriaEsquema::getCategoria);

        Button añadir = new Button("Añadir Libro", e -> {
            // Usamos buscarPorNombre que devuelve List<UsuarioEsquema>
            List<UsuarioEsquema> usuarios = usuarioService.buscarPorNombre(username);

            if (usuarios.isEmpty()) {
                Notification.show("Error: No existe el usuario '" + username + "' en la base de datos.");
                return;
            }

            UsuarioEsquema dueño = usuarios.get(0);

            LibroEsquema nuevo = new LibroEsquema();
            nuevo.setTitulo(titulo.getValue());
            nuevo.setAutor(autor.getValue());
            nuevo.setUsuario(dueño); // 1:N

            // Inicializamos ArrayList para evitar fallos en N:M
            nuevo.setCategorias(new ArrayList<>());
            if(catCombo.getValue() != null) {
                nuevo.getCategorias().add(catCombo.getValue());
            }

            libroService.save(nuevo);
            actualizarGrid();
            titulo.clear();
            autor.clear();
            Notification.show("Libro guardado");
        });

        add(new HorizontalLayout(titulo, autor, catCombo, añadir));

        // --- CONFIGURACIÓN DEL GRID ---
        grid.addColumn(LibroEsquema::getTitulo).setHeader("Título");
        grid.addColumn(LibroEsquema::getAutor).setHeader("Autor");

        // Columna N:M (Categorías)
        grid.addColumn(l -> l.getCategorias() == null ? "" :
                l.getCategorias().stream()
                        .map(CategoriaEsquema::getCategoria)
                        .collect(Collectors.joining(", "))).setHeader("Categorías");

        // Columna de Acción (Borrar)
        grid.addComponentColumn(libro -> {
            Button btnBorrar = new Button("Borrar", e -> {
                libroService.delete(libro.getId());
                actualizarGrid();
                Notification.show("Libro eliminado");
            });
            btnBorrar.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return btnBorrar;
        }).setHeader("Acciones");

        add(grid);
        actualizarGrid();
    }

    private void actualizarGrid() {
        // Filtramos por el nombre del usuario logueado
        grid.setItems(libroService.findAll().stream()
                .filter(l -> l.getUsuario() != null && l.getUsuario().getNombre().equalsIgnoreCase(username))
                .collect(Collectors.toList()));
    }
}