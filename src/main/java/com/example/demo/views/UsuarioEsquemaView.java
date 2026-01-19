package com.example.demo.views;

import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.services.UsuarioEsquemaService;
import com.example.demo.security.SecurityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "perfil", layout = MainLayout.class)
@PermitAll
public class UsuarioEsquemaView extends VerticalLayout {

    public UsuarioEsquemaView(UsuarioEsquemaService usuarioService, SecurityService securityService) {
        String username = securityService.getAuthenticatedUsername();
        UsuarioEsquema user = usuarioService.buscarPorNombre(username).get(0);

        setAlignItems(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);

        // --- IMAGEN AJUSTADA ---
        Image avatar = new Image("https://api.dicebear.com/7.x/pixel-art/svg?seed=" + username, "User Icon");
        avatar.setWidth("150px");  // Tamaño fijo de ancho
        avatar.setHeight("150px"); // Tamaño fijo de alto
        avatar.getStyle().set("border-radius", "50%"); // La hace redonda
        avatar.getStyle().set("border", "2px solid #ccc");

        H2 titulo = new H2("Configuración de Perfil");

        TextField nombre = new TextField("Nombre de usuario");
        nombre.setValue(user.getNombre());
        nombre.setReadOnly(true);
        nombre.setWidth("350px");

        TextField email = new TextField("Correo electrónico");
        email.setValue(user.getEmail());
        email.setWidth("350px");

        TextArea bio = new TextArea("Biografía del lector");
        bio.setPlaceholder("Cuéntanos qué te gusta leer...");
        bio.setWidth("350px");
        bio.setHeight("120px");

        Button btnGuardar = new Button("Actualizar Datos", VaadinIcon.SAFE.create(), e -> {
            // Aquí deberías llamar a tu servicio para guardar si quieres persistir la Bio o el Email
            Notification.show("Perfil guardado con éxito");
        });
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(avatar, titulo, nombre, email, bio, btnGuardar);
    }
}