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
import com.vaadin.flow.data.value.ValueChangeMode;
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
    private final CategoriaEsquemaService categoriaService;
    private final String username;

    private final FlexLayout contenedorCards = new FlexLayout();
    private final TextField buscador = new TextField();
    private final ComboBox<CategoriaEsquema> filtroCategoria = new ComboBox<>();

    public BibliotecaView(LibroEsquemaService libroService, UsuarioEsquemaService usuarioService,
                          CategoriaEsquemaService categoriaService, SecurityService securityService) {
        this.libroService = libroService;
        this.usuarioService = usuarioService;
        this.categoriaService = categoriaService;
        this.username = securityService.getAuthenticatedUsername();

        setAlignItems(Alignment.CENTER);
        setPadding(true);

        // --- BARRA DE FILTROS ---
        HorizontalLayout barraFiltros = new HorizontalLayout();
        barraFiltros.setWidthFull();
        barraFiltros.setMaxWidth("900px");
        barraFiltros.setJustifyContentMode(JustifyContentMode.CENTER);
        barraFiltros.getStyle().set("margin-bottom", "20px");

        buscador.setPlaceholder("Buscar por título o autor...");
        buscador.setPrefixComponent(VaadinIcon.SEARCH.create());
        buscador.setClearButtonVisible(true);
        buscador.setWidth("400px");
        // Filtra mientras escribes
        buscador.setValueChangeMode(ValueChangeMode.LAZY);
        buscador.addValueChangeListener(e -> actualizarCatalogo());

        filtroCategoria.setPlaceholder("Todas las categorías");
        filtroCategoria.setItems(categoriaService.findAll());
        filtroCategoria.setItemLabelGenerator(CategoriaEsquema::getCategoria);
        filtroCategoria.setClearButtonVisible(true);
        filtroCategoria.addValueChangeListener(e -> actualizarCatalogo());

        barraFiltros.add(buscador, filtroCategoria);

        // --- CONTENEDOR DE CARDS ---
        contenedorCards.setWidthFull();
        contenedorCards.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        contenedorCards.setJustifyContentMode(JustifyContentMode.CENTER);
        contenedorCards.getStyle().set("gap", "25px");

        add(new H2("Catálogo de Libros"), barraFiltros, contenedorCards);
        actualizarCatalogo();
    }

    private void actualizarCatalogo() {
        contenedorCards.removeAll();
        List<LibroEsquema> todos = libroService.findAll();

        // 1. Títulos que ya tiene el usuario para ocultarlos
        Set<String> misTitulos = todos.stream()
                .filter(l -> l.getUsuario() != null && l.getUsuario().getNombre().equalsIgnoreCase(username))
                .map(l -> l.getTitulo().toLowerCase().trim())
                .collect(Collectors.toSet());

        String textoBusqueda = buscador.getValue().toLowerCase().trim();
        CategoriaEsquema catSeleccionada = filtroCategoria.getValue();

        todos.stream()
                .filter(l -> l.getUsuario() == null) // Solo catálogo público
                .filter(l -> !misTitulos.contains(l.getTitulo().toLowerCase().trim())) // Evitar duplicados
                .filter(l -> {
                    // FILTRO DE TEXTO (Título o Autor)
                    boolean coincideTexto = textoBusqueda.isEmpty() ||
                            l.getTitulo().toLowerCase().contains(textoBusqueda) ||
                            l.getAutor().toLowerCase().contains(textoBusqueda);

                    // FILTRO DE CATEGORÍA (Comparamos por ID para que sea infalible)
                    boolean coincideCat = true;
                    if (catSeleccionada != null) {
                        coincideCat = l.getCategorias().stream()
                                .anyMatch(c -> c.getId().equals(catSeleccionada.getId()));
                    }

                    return coincideTexto && coincideCat;
                })
                .forEach(libro -> contenedorCards.add(crearCard(libro)));

        // Mensaje si no hay nada que mostrar
        if (contenedorCards.getComponentCount() == 0) {
            Span mensaje = new Span("No hay libros que coincidan con la búsqueda.");
            mensaje.getStyle().set("color", "gray").set("margin-top", "20px");
            contenedorCards.add(mensaje);
        }
    }

    private VerticalLayout crearCard(LibroEsquema libro) {
        // ... (El código de crearCard es el mismo que ya tenías, funciona perfecto) ...
        VerticalLayout card = new VerticalLayout();
        card.setWidth("240px");
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "15px")
                .set("box-shadow", "0 10px 20px rgba(0,0,0,0.12)")
                .set("overflow", "hidden")
                .set("padding", "0")
                .set("border", "1px solid #eee");

        Image portada = new Image(libro.getImagenUrl(), "Portada");
        portada.setWidthFull();
        portada.setHeight("300px");
        portada.getStyle().set("object-fit", "cover");

        VerticalLayout cuerpo = new VerticalLayout();
        cuerpo.setPadding(true);
        cuerpo.setSpacing(false);

        Span titulo = new Span(libro.getTitulo());
        titulo.getStyle().set("font-weight", "bold").set("font-size", "1.1em");

        Span autor = new Span(libro.getAutor());
        autor.getStyle().set("color", "#555").set("font-size", "0.9em");

        HorizontalLayout badges = new HorizontalLayout();
        libro.getCategorias().forEach(c -> {
            Span b = new Span(c.getCategoria());
            b.getStyle().set("background", "#e1f5fe").set("color", "#0288d1").set("font-size", "0.7em")
                    .set("padding", "2px 8px").set("border-radius", "10px").set("font-weight", "bold");
            badges.add(b);
        });

        Button btnAdd = new Button("Añadir", VaadinIcon.HEART.create(), e -> {
            UsuarioEsquema yo = usuarioService.buscarPorNombre(username).get(0);
            LibroEsquema copia = new LibroEsquema();
            copia.setTitulo(libro.getTitulo());
            copia.setAutor(libro.getAutor());
            copia.setImagenUrl(libro.getImagenUrl());
            copia.setUsuario(yo);
            copia.setCategorias(new ArrayList<>(libro.getCategorias()));
            libroService.save(copia);
            Notification.show("¡Añadido!");
            actualizarCatalogo();
        });
        btnAdd.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAdd.setWidthFull();

        cuerpo.add(titulo, autor, badges, btnAdd);
        card.add(portada, cuerpo);
        return card;
    }
}