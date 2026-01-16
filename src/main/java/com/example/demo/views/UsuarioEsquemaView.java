package com.example.demo.views;

import com.example.demo.entities.PerfilEsquema;
import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.services.PerfilEsquemaService;
import com.example.demo.services.UsuarioEsquemaService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(value = "esquema") // Accede a http://localhost:8080/esquema
public class UsuarioEsquemaView extends VerticalLayout {

    public UsuarioEsquemaView(UsuarioEsquemaService usuarioService, PerfilEsquemaService perfilService) {
        setSpacing(true);
        setPadding(true);

        TextField nombre = new TextField("Nombre");
        TextField email = new TextField("Email");
        TextField bio = new TextField("Biografía");

        Grid<UsuarioEsquema> gridUsuario = new Grid<>(UsuarioEsquema.class, false);
        gridUsuario.addColumn(UsuarioEsquema::getId).setHeader("ID");
        gridUsuario.addColumn(UsuarioEsquema::getNombre).setHeader("Nombre");
        gridUsuario.addColumn(u -> u.getPerfil() != null ? u.getPerfil().getBio() : "Sin Bio").setHeader("Bio (desde Usuario)");

        Grid<PerfilEsquema> gridPerfil = new Grid<>(PerfilEsquema.class, false);
        gridPerfil.addColumn(PerfilEsquema::getId).setHeader("ID Perfil");
        gridPerfil.addColumn(PerfilEsquema::getBio).setHeader("Biografía");
        gridPerfil.addColumn(p -> p.getUsuario() != null ? p.getUsuario().getId() : "N/A").setHeader("ID Usuario (FK)");

        Button agregar = new Button("Guardar Todo", e -> {
            UsuarioEsquema u = new UsuarioEsquema();
            u.setNombre(nombre.getValue());
            u.setEmail(email.getValue());

            PerfilEsquema p = new PerfilEsquema();
            p.setBio(bio.getValue());

            p.setUsuario(u);
            u.setPerfil(p);

            usuarioService.save(u);

            gridUsuario.setItems(usuarioService.findAll());
            gridPerfil.setItems(perfilService.findAll());

            nombre.clear(); email.clear(); bio.clear();
            Notification.show("¡Guardado en ambas tablas!");
        });

        HorizontalLayout formulario = new HorizontalLayout(nombre, email, bio, agregar);
        formulario.setVerticalComponentAlignment(Alignment.BASELINE, agregar);

        add(
                new H3("1. Crear Usuario y Perfil"),
                formulario,
                new H3("2. Tabla Usuarios"),
                gridUsuario,
                new H3("3. Tabla Perfiles"),
                gridPerfil
        );

        gridUsuario.setItems(usuarioService.findAll());
        gridPerfil.setItems(perfilService.findAll());
    }
}