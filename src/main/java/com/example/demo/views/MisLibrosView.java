package com.example.demo.views;

import com.example.demo.entities.*;
import com.example.demo.services.*;
import com.example.demo.security.SecurityService;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Route(value = "mis-libros", layout = MainLayout.class)
@PermitAll
public class MisLibrosView extends VerticalLayout {

    private final LibroEsquemaService libroService;
    private final UsuarioEsquemaService usuarioService;
    private final String username;
    private final Grid<LibroEsquema> grid = new Grid<>(LibroEsquema.class, false);

    public MisLibrosView(LibroEsquemaService libroService, UsuarioEsquemaService usuarioService,
                         CategoriaEsquemaService categoriaService, SecurityService securityService) {
        this.libroService = libroService;
        this.usuarioService = usuarioService;
        this.username = securityService.getAuthenticatedUsername();

        // FORMULARIO DE AÑADIR (ARRIBA)
        TextField t = new TextField("Título");
        TextField a = new TextField("Autor");
        ComboBox<CategoriaEsquema> cat = new ComboBox<>("Categoría");
        cat.setItems(categoriaService.findAll());
        cat.setItemLabelGenerator(CategoriaEsquema::getCategoria);

        Button saveBtn = new Button("Añadir Libro", VaadinIcon.PLUS.create(), e -> {
            if(t.isEmpty() || a.isEmpty()) {
                Notification.show("Título y Autor obligatorios");
                return;
            }
            UsuarioEsquema yo = usuarioService.buscarPorNombre(username).get(0);
            LibroEsquema nuevo = new LibroEsquema();
            nuevo.setTitulo(t.getValue());
            nuevo.setAutor(a.getValue());
            nuevo.setUsuario(yo);
            nuevo.setCategorias(new ArrayList<>());
            if(cat.getValue() != null) nuevo.getCategorias().add(cat.getValue());

            libroService.save(nuevo);
            actualizarLista();
            t.clear(); a.clear(); cat.clear();
            Notification.show("¡Libro guardado!");
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout formulario = new HorizontalLayout(t, a, cat, saveBtn);
        formulario.setVerticalComponentAlignment(Alignment.BASELINE, saveBtn);

        // CONFIGURACIÓN DEL GRID
        grid.addColumn(LibroEsquema::getTitulo).setHeader("Título").setSortable(true);
        grid.addColumn(LibroEsquema::getAutor).setHeader("Autor").setSortable(true);
        grid.addColumn(l -> l.getCategorias().stream()
                        .map(CategoriaEsquema::getCategoria)
                        .collect(Collectors.joining(", ")))
                .setHeader("Género");

        grid.addComponentColumn(libro -> {
            Button btnDel = new Button(VaadinIcon.TRASH.create(), e -> {
                libroService.delete(libro.getId());
                actualizarLista();
            });
            btnDel.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            return btnDel;
        }).setHeader("Eliminar");

        add(new H2("Mi Colección Personal"), formulario, grid);
        actualizarLista();
    }

    private void actualizarLista() {
        grid.setItems(libroService.findAll().stream()
                .filter(l -> l.getUsuario() != null && l.getUsuario().getNombre().equalsIgnoreCase(username))
                .toList());
    }
}