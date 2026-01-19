package com.example.demo.views;

import com.example.demo.entities.*;
import com.example.demo.services.*;
import com.example.demo.security.SecurityService;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import jakarta.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Biblioteca Pública")
@PermitAll
public class BibliotecaView extends VerticalLayout {

    private final LibroEsquemaService libroService;
    private final UsuarioEsquemaService usuarioService;
    private final String username;

    private final FlexLayout contenedorCards = new FlexLayout();
    private final TextField buscador = new TextField("Buscar por título o autor");
    private final ComboBox<CategoriaEsquema> filtroCategoria = new ComboBox<>("Filtrar por categoría");

    public BibliotecaView(LibroEsquemaService libroService,
                          UsuarioEsquemaService usuarioService,
                          CategoriaEsquemaService categoriaService,
                          SecurityService securityService) {
        this.libroService = libroService;
        this.usuarioService = usuarioService;
        this.username = securityService.getAuthenticatedUsername();

        setAlignItems(Alignment.CENTER);
        setPadding(true);

        HorizontalLayout filtros = new HorizontalLayout();
        filtros.setWidthFull();
        filtros.setMaxWidth("1100px");
        filtros.setAlignItems(Alignment.BASELINE);

        buscador.setPlaceholder("Escribe algo...");
        buscador.setClearButtonVisible(true);
        buscador.setPrefixComponent(VaadinIcon.SEARCH.create());
        buscador.addValueChangeListener(e -> actualizarCatalogo());

        filtroCategoria.setItems(categoriaService.findAll());
        filtroCategoria.setItemLabelGenerator(CategoriaEsquema::getCategoria);
        filtroCategoria.setPlaceholder("Todas");
        filtroCategoria.setClearButtonVisible(true);
        filtroCategoria.addValueChangeListener(e -> actualizarCatalogo());

        filtros.add(buscador, filtroCategoria);
        filtros.expand(buscador);

        contenedorCards.setWidthFull();
        contenedorCards.setMaxWidth("1300px"); // Aumentado para dar aire a las 4 columnas
        contenedorCards.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        contenedorCards.getStyle().set("gap", "25px");
        contenedorCards.setJustifyContentMode(JustifyContentMode.CENTER);

        add(new H2("Explorar Biblioteca"), filtros, contenedorCards);
        actualizarCatalogo();
    }

    private void actualizarCatalogo() {
        contenedorCards.removeAll();
        List<LibroEsquema> todos = libroService.findAll();

        Set<String> misTitulos = todos.stream()
                .filter(l -> l.getUsuario() != null && l.getUsuario().getNombre().equalsIgnoreCase(username))
                .map(l -> l.getTitulo().toLowerCase().trim())
                .collect(Collectors.toSet());

        String textoBusqueda = buscador.getValue().toLowerCase().trim();
        CategoriaEsquema catSeleccionada = filtroCategoria.getValue();

        todos.stream()
                .filter(l -> l.getUsuario() == null)
                .filter(l -> !misTitulos.contains(l.getTitulo().toLowerCase().trim()))
                .filter(l -> {
                    boolean coincideTexto = textoBusqueda.isEmpty() ||
                            l.getTitulo().toLowerCase().contains(textoBusqueda) ||
                            l.getAutor().toLowerCase().contains(textoBusqueda);

                    boolean coincideCat = (catSeleccionada == null) ||
                            l.getCategorias().stream().anyMatch(c -> c.getId().equals(catSeleccionada.getId()));

                    return coincideTexto && coincideCat;
                })
                .forEach(libro -> contenedorCards.add(crearCardVerticalGrande(libro)));
    }

    private VerticalLayout crearCardVerticalGrande(LibroEsquema libro) {
        VerticalLayout card = new VerticalLayout();
        card.setWidth("250px");
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "20px")
                .set("box-shadow", "0 10px 25px rgba(0,0,0,0.1)")
                .set("padding", "15px")
                .set("transition", "transform 0.3s ease");

        Image img = new Image(libro.getImagenUrl(), "Portada");
        img.setWidthFull();
        img.setHeight("340px");
        img.getStyle().set("object-fit", "cover").set("border-radius", "15px");

        Span titulo = new Span(libro.getTitulo());
        titulo.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "1.1em") // Un pelín más pequeña para que no rompa en 250px
                .set("text-align", "center")
                .set("margin-top", "10px")
                .set("height", "2.4em")
                .set("overflow", "hidden");

        Span autor = new Span(libro.getAutor());
        autor.getStyle().set("color", "gray").set("font-size", "0.9em");

        HorizontalLayout listaCategorias = new HorizontalLayout();
        listaCategorias.setJustifyContentMode(JustifyContentMode.CENTER);
        libro.getCategorias().forEach(c -> {
            Span badge = new Span(c.getCategoria());
            badge.getStyle()
                    .set("background-color", "#e7f3ff")
                    .set("color", "#007bff")
                    .set("padding", "2px 8px")
                    .set("border-radius", "10px")
                    .set("font-size", "0.7em")
                    .set("font-weight", "600");
            listaCategorias.add(badge);
        });

        Span pags = new Span(VaadinIcon.BOOK.create());
        pags.add(new Span(" " + libro.getPaginasTotales() + " pág."));
        pags.getStyle().set("font-size", "0.8em").set("color", "#555");

        Button añadirBtn = new Button("Añadir", VaadinIcon.HEART.create(), e -> {
            UsuarioEsquema yo = usuarioService.buscarPorNombre(username).get(0);
            LibroEsquema copia = new LibroEsquema();
            copia.setTitulo(libro.getTitulo());
            copia.setAutor(libro.getAutor());
            copia.setImagenUrl(libro.getImagenUrl());
            copia.setUsuario(yo);
            copia.setPaginasTotales(libro.getPaginasTotales());
            copia.setPaginasLeidas(0);
            copia.setCategorias(new ArrayList<>(libro.getCategorias()));

            libroService.save(copia);
            Notification.show("¡Guardado!");
            actualizarCatalogo();
        });
        añadirBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        añadirBtn.setWidthFull();

        card.add(img, titulo, autor, listaCategorias, pags, añadirBtn);
        card.setAlignItems(Alignment.CENTER);

        card.getElement().addEventListener("mouseenter", e -> card.getStyle().set("transform", "scale(1.03)"));
        card.getElement().addEventListener("mouseleave", e -> card.getStyle().set("transform", "scale(1.0)"));

        return card;
    }
}