package com.example.demo.views;

import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.services.UsuarioEsquemaService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;

@Route("registro")
@PageTitle("Registro | Biblioteca")
@AnonymousAllowed
public class RegistroView extends VerticalLayout {

    public RegistroView(UsuarioEsquemaService usuarioService, PasswordEncoder passwordEncoder) {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        getStyle().set("background-image", "url('https://images.unsplash.com/photo-1521587760476-6c12a4b040da?fm=jpg&q=60&w=3000&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Zm9uZG8lMjBkZSUyMGxhJTIwYmlibGlvdGVjYXxlbnwwfHwwfHx8MA%3D%3D')");
        getStyle().set("background-size", "cover");
        getStyle().set("background-position", "center");

        Div marcoExterior = new Div();
        marcoExterior.getStyle()
                .set("background", "rgba(10, 20, 35, 0.8)")
                .set("backdrop-filter", "blur(12px)")
                .set("padding", "40px")
                .set("border-radius", "25px")
                .set("border", "2px solid #00d4ff")
                .set("box-shadow", "0 0 20px rgba(0, 212, 255, 0.3)")
                .set("width", "400px");

        Div formBlanco = new Div();
        formBlanco.getStyle()
                .set("background", "white")
                .set("padding", "30px")
                .set("border-radius", "15px")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "15px");

        H2 titulo = new H2("CREAR CUENTA");
        titulo.getStyle()
                .set("color", "#0a1423")
                .set("margin-top", "0")
                .set("text-align", "center")
                .set("font-family", "sans-serif");

        TextField nombre = new TextField("Usuario");
        EmailField email = new EmailField("Email");
        PasswordField pass = new PasswordField("Contraseña");

        nombre.setWidthFull();
        email.setWidthFull();
        pass.setWidthFull();

        Button btnRegistrar = new Button("Registrarse", e -> {
            if (nombre.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Notification.show("Por favor, completa todos los campos");
                return;
            }

            UsuarioEsquema nuevo = new UsuarioEsquema();
            nuevo.setNombre(nombre.getValue());
            nuevo.setEmail(email.getValue());
            nuevo.setContraseña(passwordEncoder.encode(pass.getValue()));

            usuarioService.save(nuevo);
            Notification.show("¡Registro exitoso! Ya puedes entrar");
            UI.getCurrent().navigate(LoginView.class);
        });

        btnRegistrar.setWidthFull();
        btnRegistrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnRegistrar.getStyle()
                .set("background", "linear-gradient(45deg, #00d4ff, #0055ff)")
                .set("color", "white")
                .set("font-weight", "bold")
                .set("margin-top", "10px")
                .set("cursor", "pointer");

        Button btnVolver = new Button("Volver al Login", e -> UI.getCurrent().navigate(LoginView.class));
        btnVolver.setWidthFull();
        btnVolver.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnVolver.getStyle().set("color", "#0055ff");

        formBlanco.add(titulo, nombre, email, pass, btnRegistrar, btnVolver);
        marcoExterior.add(formBlanco);
        add(marcoExterior);
    }
}