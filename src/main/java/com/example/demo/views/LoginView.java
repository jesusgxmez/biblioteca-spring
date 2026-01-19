package com.example.demo.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login | Biblioteca")
@AnonymousAllowed // Importante para que te deje ver esta pantalla sin estar logueado
public class LoginView extends VerticalLayout { // Cambiado de AppLayout a VerticalLayout

    public LoginView() {
        // Configuramos el layout principal
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle().set("background-color", "#f8f9fa");

        // 1. Cabecera (similar a lo que tenías)
        H1 logo = new H1("BIBLIOTECA PERSONAL");
        logo.getStyle().set("color", "#2c3e50");

        // 2. FORMULARIO DE LOGIN (Esto es lo que faltaba para poder entrar)
        LoginForm loginForm = new LoginForm();
        loginForm.setAction("login"); // Esto conecta con Spring Security automáticamente

        // 3. Añadimos todo al centro de la pantalla
        add(VaadinIcon.BOOK.create(), logo, loginForm);

        // Opcional: Un botón para volver si te pierdes
        Button backBtn = new Button("Ir a inicio", e -> UI.getCurrent().navigate(""));
        backBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        add(backBtn);
    }
}