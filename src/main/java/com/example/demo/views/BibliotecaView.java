package com.example.demo.views;

import com.example.demo.entities.*;
import com.example.demo.services.*;
import com.example.demo.security.SecurityService;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "biblioteca", layout = MainLayout.class)
@PermitAll
public class BibliotecaView extends VerticalLayout {

    private final LibroEsquemaService libroService;
    private final UsuarioEsquemaService usuarioService;
    private final String username;
    private final Grid<LibroEsquema> grid = new Grid<>(LibroEsquema.class, false);

    public BibliotecaView(LibroEsquemaService libroService, UsuarioEsquemaService usuarioService, SecurityService securityService) {
        this.libroService = libroService;
        this.usuarioService = usuarioService;
        this.username = securityService.getAuthenticatedUsername();

        grid.addColumn(LibroEsquema::getTitulo).setHeader("Título");
        grid.addColumn(LibroEsquema::getAutor).setHeader("Autor");

        grid.addComponentColumn(libro -> {
            Button btnAdd = new Button("Me gusta", VaadinIcon.HEART.create(), e -> {
                UsuarioEsquema yo = usuarioService.buscarPorNombre(username).get(0);
                LibroEsquema copia = new LibroEsquema();
                copia.setTitulo(libro.getTitulo());
                copia.setAutor(libro.getAutor());
                copia.setUsuario(yo);
                copia.setCategorias(new ArrayList<>(libro.getCategorias()));

                libroService.save(copia);
                Notification.show("Añadido!");
                actualizarCatalogo(); // Forzamos el refresco para que desaparezca
            });
            btnAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            return btnAdd;
        });

        add(new H2("Libros que aún no tienes"), grid);
        actualizarCatalogo();
    }

    private void actualizarCatalogo() {
        List<LibroEsquema> todos = libroService.findAll();

        // Sacamos los títulos de los libros que YA tiene el usuario
        Set<String> titulosQueYaTengo = todos.stream()
                .filter(l -> l.getUsuario() != null && l.getUsuario().getNombre().equalsIgnoreCase(username))
                .map(LibroEsquema::getTitulo)
                .collect(Collectors.toSet());

        // Filtramos el catálogo: solo libros sin dueño y que no tengan un título que ya poseas
        grid.setItems(todos.stream()
                .filter(l -> l.getUsuario() == null)
                .filter(l -> !titulosQueYaTengo.contains(l.getTitulo()))
                .toList());
    }
}