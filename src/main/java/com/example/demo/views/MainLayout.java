package com.example.demo.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MainLayout extends AppLayout {
    public MainLayout() {
        // Logo con la fuente estándar y el color azul neón del login
        H1 logo = new H1("READER APP");
        logo.getStyle()
                .set("font-size", "1.5em")
                .set("margin", "0 20px")
                .set("color", "#00d4ff") // El azul neón del borde del login
                .set("font-weight", "bold");

        // Botones del menú
        Button b1 = createMenuButton("Catálogo", VaadinIcon.SEARCH, BibliotecaView.class);
        Button b2 = createMenuButton("Mis Libros", VaadinIcon.BOOK, MisLibrosView.class);
        Button b3 = createMenuButton("Perfil", VaadinIcon.USER, UsuarioEsquemaView.class);

        Button logout = new Button("Salir", VaadinIcon.EXIT.create(), e -> UI.getCurrent().getPage().setLocation("/logout"));
        logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);

        HorizontalLayout menu = new HorizontalLayout(b1, b2, b3);
        menu.setSpacing(true);

        HorizontalLayout header = new HorizontalLayout(logo, menu, logout);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(menu);
        header.setWidthFull();
        header.setPadding(true);

        // Header oscuro para que resalte el azul neón, igual que el fondo del login
        header.getStyle()
                .set("background-color", "#0a1423") // El azul oscuro profundo del login
                .set("box-shadow", "0 2px 10px rgba(0, 212, 255, 0.2)")
                .set("border-bottom", "1px solid #00d4ff"); // Línea neón sutil

        addToNavbar(header);
    }

    private Button createMenuButton(String text, VaadinIcon icon, Class<? extends com.vaadin.flow.component.Component> view) {
        Button btn = new Button(text, icon.create(), e -> UI.getCurrent().navigate(view));
        btn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btn.getStyle()
                .set("color", "white")
                .set("font-weight", "600");

        // Efecto al pasar el ratón para que brille en azul
        btn.getElement().addEventListener("mouseenter", e -> btn.getStyle().set("color", "#00d4ff"));
        btn.getElement().addEventListener("mouseleave", e -> btn.getStyle().set("color", "white"));

        return btn;
    }
}