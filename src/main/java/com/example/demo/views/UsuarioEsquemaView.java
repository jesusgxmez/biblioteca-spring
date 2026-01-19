package com.example.demo.views;

import com.example.demo.entities.PerfilEsquema;
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
import com.vaadin.flow.router.PageTitle;
import jakarta.annotation.security.PermitAll;

@Route(value = "perfil", layout = MainLayout.class)
@PageTitle("Mi Perfil | ReaderApp")
@PermitAll
public class UsuarioEsquemaView extends VerticalLayout {

    public UsuarioEsquemaView(UsuarioEsquemaService usuarioService, SecurityService securityService) {
        String username = securityService.getAuthenticatedUsername();
        UsuarioEsquema user = usuarioService.buscarPorNombre(username).get(0);

        setAlignItems(Alignment.CENTER);
        setPadding(true);
        setSpacing(true);

        Image avatar = new Image("https://api.dicebear.com/7.x/avataaars/svg?seed=" + username, "User Icon");
        avatar.setWidth("150px");
        avatar.setHeight("150px");
        avatar.getStyle().set("border-radius", "50%").set("border", "4px solid #007bff").set("background", "#f0f0f0");

        H2 titulo = new H2("Mi Perfil de Lector");

        TextField nombre = new TextField("Nombre de usuario");
        nombre.setValue(user.getNombre());
        nombre.setReadOnly(true);
        nombre.setWidth("350px");

        EmailField email = new EmailField("Correo electrónico");
        email.setValue(user.getEmail() != null ? user.getEmail() : "");
        email.setWidth("350px");

        TextArea bioArea = new TextArea("Biografía");

        if (user.getPerfil() != null) {
            bioArea.setValue(user.getPerfil().getBio() != null ? user.getPerfil().getBio() : "");
        } else {
            bioArea.setPlaceholder("Aún no tienes biografía. ¡Escribe algo!");
        }

        bioArea.setWidth("350px");
        bioArea.setHeight("150px");

        Button btnGuardar = new Button("Guardar Cambios", VaadinIcon.CHECK_CIRCLE.create(), e -> {
            try {
                user.setEmail(email.getValue());

                PerfilEsquema perfil = user.getPerfil();
                if (perfil == null) {
                    perfil = new PerfilEsquema();
                    perfil.setUsuario(user);
                    user.setPerfil(perfil);
                }
                perfil.setBio(bioArea.getValue());

                usuarioService.save(user);

                Notification.show("¡Perfil actualizado correctamente!");
            } catch (Exception ex) {
                Notification.show("Error al guardar: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);

        add(avatar, titulo, nombre, email, bioArea, btnGuardar);
    }
}