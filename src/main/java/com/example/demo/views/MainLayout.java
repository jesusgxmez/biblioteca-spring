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
        H1 logo = new H1("BIBLIOTECA PERSONAL");
        logo.getStyle().set("font-size", "1.3em").set("margin", "0").set("color", "#2c3e50");

        Button b1 = new Button("Catálogo", VaadinIcon.SEARCH.create(), e -> UI.getCurrent().navigate(BibliotecaView.class));
        Button b2 = new Button("Mis Libros", VaadinIcon.BOOK.create(), e -> UI.getCurrent().navigate(MisLibrosView.class));
        Button b3 = new Button("Mi Perfil", VaadinIcon.USER.create(), e -> UI.getCurrent().navigate(UsuarioEsquemaView.class));
        Button logout = new Button("Salir", VaadinIcon.EXIT.create(), e -> UI.getCurrent().getPage().setLocation("/logout"));

        logout.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout menu = new HorizontalLayout(b1, b2, b3);
        HorizontalLayout header = new HorizontalLayout(logo, menu, logout);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(menu);
        header.setWidthFull();
        header.setPadding(true);
        header.getStyle().set("border-bottom", "1px solid #ddd");

        addToNavbar(header);
    }
}