package com.example.demo.views;

import com.example.demo.entities.*;
import com.example.demo.services.*;
import com.example.demo.security.SecurityService;
import com.vaadin.flow.component.Html;
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
        contenedorCards.setMaxWidth("1300px");
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

        VerticalLayout infoClicable = new VerticalLayout();
        infoClicable.setPadding(false);
        infoClicable.setSpacing(false); // Más compacto
        infoClicable.setAlignItems(Alignment.CENTER);
        infoClicable.getStyle().set("cursor", "pointer");
        infoClicable.addClickListener(e -> abrirModalDetalle(libro));

        Image img = new Image(libro.getImagenUrl(), "Portada");
        img.setWidthFull();
        img.setHeight("340px");
        img.getStyle().set("object-fit", "cover").set("border-radius", "15px");

        Span titulo = new Span(libro.getTitulo());
        titulo.getStyle()
                .set("font-weight", "bold").set("font-size", "1.1em")
                .set("text-align", "center").set("margin-top", "10px")
                .set("display", "-webkit-box").set("-webkit-line-clamp", "2")
                .set("-webkit-box-orient", "vertical").set("overflow", "hidden")
                .set("line-height", "1.3em").set("min-height", "2.7em");

        Span autor = new Span(libro.getAutor());
        autor.getStyle().set("color", "gray").set("font-size", "0.9em");

        Span paginasLabel = new Span(VaadinIcon.BOOK.create());
        paginasLabel.add(new Span(" " + libro.getPaginasTotales() + " pág."));
        paginasLabel.getStyle().set("font-size", "0.85em").set("color", "#555").set("margin-bottom", "10px");

        infoClicable.add(img, titulo, autor, paginasLabel);

        Button añadirBtn = new Button("Añadir", VaadinIcon.HEART.create(), e -> {
            UsuarioEsquema yo = usuarioService.buscarPorNombre(username).get(0);
            LibroEsquema copia = new LibroEsquema();
            copia.setTitulo(libro.getTitulo());
            copia.setAutor(libro.getAutor());
            copia.setImagenUrl(libro.getImagenUrl());
            copia.setDescripcion(libro.getDescripcion()); // Sincroniza la sinopsis
            copia.setUsuario(yo);
            copia.setPaginasTotales(libro.getPaginasTotales());
            copia.setPaginasLeidas(0);
            copia.setCategorias(new ArrayList<>(libro.getCategorias()));

            libroService.save(copia);
            Notification.show("¡" + libro.getTitulo() + " guardado!");
            actualizarCatalogo();
        });
        añadirBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        añadirBtn.setWidthFull();
        añadirBtn.getStyle().set("background-color", "#00d4ff").set("color", "#0a1423");

        card.add(infoClicable, añadirBtn);
        card.setAlignItems(Alignment.CENTER);

        card.getElement().addEventListener("mouseenter", e -> card.getStyle().set("transform", "scale(1.03)"));
        card.getElement().addEventListener("mouseleave", e -> card.getStyle().set("transform", "scale(1.0)"));

        return card;
    }

    private void abrirModalDetalle(LibroEsquema libro) {
        com.vaadin.flow.component.dialog.Dialog modal = new com.vaadin.flow.component.dialog.Dialog();
        modal.setWidth("800px");
        modal.setDraggable(true);

        HorizontalLayout layoutPrincipal = new HorizontalLayout();
        layoutPrincipal.setSpacing(true);
        layoutPrincipal.setPadding(true);
        layoutPrincipal.setWidthFull();

        Image imgDetalle = new Image(libro.getImagenUrl(), "Portada");
        imgDetalle.setWidth("280px");
        imgDetalle.getStyle()
                .set("border-radius", "15px")
                .set("border", "2px solid #00d4ff")
                .set("box-shadow", "0 0 15px rgba(0, 212, 255, 0.3)");

        VerticalLayout info = new VerticalLayout();
        info.setSpacing(false);
        info.setPadding(false);

        H2 t = new H2(libro.getTitulo());
        t.getStyle().set("color", "#00d4ff").set("margin", "0");

        Span a = new Span("Escrito por " + libro.getAutor());
        a.getStyle().set("font-style", "italic").set("color", "gray").set("margin-bottom", "15px");

        HorizontalLayout badges = new HorizontalLayout();
        badges.getStyle().set("margin-bottom", "15px");
        libro.getCategorias().forEach(c -> {
            Span b = new Span(c.getCategoria());
            b.getStyle()
                    .set("background", "#0a1423")
                    .set("color", "#00d4ff")
                    .set("border", "1px solid #00d4ff")
                    .set("padding", "2px 10px")
                    .set("border-radius", "12px")
                    .set("font-size", "0.8em");
            badges.add(b);
        });

        String sinopsis = (libro.getDescripcion() != null && !libro.getDescripcion().isEmpty())
                ? libro.getDescripcion()
                : "Sin descripción disponible para este ejemplar.";

        Div descContenedor = new Div();
        descContenedor.setWidthFull();
        descContenedor.getStyle()
                .set("max-height", "300px")
                .set("overflow-y", "auto")
                .set("margin", "10px 0")
                .set("padding-right", "10px")
                .set("color", "#333");
        descContenedor.add(new Html("<div style='line-height: 1.6; text-align: justify;'>" + sinopsis + "</div>"));

        Span paginas = new Span(VaadinIcon.BOOK.create());
        paginas.add(new Span(" Longitud: " + libro.getPaginasTotales() + " páginas"));
        paginas.getStyle().set("font-weight", "600").set("color", "#2c3e50").set("margin-top", "auto");

        info.add(t, a, badges, descContenedor, paginas);
        layoutPrincipal.add(imgDetalle, info);
        layoutPrincipal.expand(info);

        Button cerrar = new Button("Volver al catálogo", e -> modal.close());
        cerrar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button añadirDesdeModal = new Button("Añadir a mi lista", VaadinIcon.HEART.create(), e -> {
            UsuarioEsquema yo = usuarioService.buscarPorNombre(username).get(0);
            LibroEsquema copia = new LibroEsquema();
            copia.setTitulo(libro.getTitulo());
            copia.setAutor(libro.getAutor());
            copia.setImagenUrl(libro.getImagenUrl());
            copia.setDescripcion(libro.getDescripcion());
            copia.setUsuario(yo);
            copia.setPaginasTotales(libro.getPaginasTotales());
            copia.setPaginasLeidas(0);
            copia.setCategorias(new ArrayList<>(libro.getCategorias()));
            libroService.save(copia);
            Notification.show("¡Añadido!");
            modal.close();
            actualizarCatalogo();
        });
        añadirDesdeModal.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        añadirDesdeModal.getStyle().set("background-color", "#00d4ff").set("color", "#0a1423");

        modal.getFooter().add(cerrar, añadirDesdeModal);
        modal.add(layoutPrincipal);
        modal.open();
    }
}