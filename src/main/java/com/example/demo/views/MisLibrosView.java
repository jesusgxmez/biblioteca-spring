package com.example.demo.views;

import com.example.demo.entities.*;
import com.example.demo.services.*;
import com.example.demo.security.SecurityService;
import com.vaadin.flow.component.button.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Route(value = "mis-libros", layout = MainLayout.class)
@PermitAll
public class MisLibrosView extends VerticalLayout {

    private final LibroEsquemaService libroService;
    private final UsuarioEsquemaService usuarioService;
    private final String username;

    // CAMBIO: Usamos FlexLayout para que las tarjetas fluyan lateralmente
    private final FlexLayout contenedorCards = new FlexLayout();

    public MisLibrosView(LibroEsquemaService libroService, UsuarioEsquemaService usuarioService,
                         CategoriaEsquemaService categoriaService, SecurityService securityService) {
        this.libroService = libroService;
        this.usuarioService = usuarioService;
        this.username = securityService.getAuthenticatedUsername();

        setAlignItems(Alignment.CENTER);

        // --- FORMULARIO ---
        HorizontalLayout formCard = new HorizontalLayout();
        formCard.setWidthFull();
        formCard.setMaxWidth("1000px");
        formCard.setPadding(true);
        formCard.getStyle()
                .set("background", "#f9f9f9")
                .set("border-radius", "12px")
                .set("box-shadow", "inset 0 2px 4px rgba(0,0,0,0.05)")
                .set("margin-bottom", "30px");

        TextField t = new TextField("Título");
        TextField a = new TextField("Autor");
        TextField img = new TextField("URL Portada");
        ComboBox<CategoriaEsquema> cat = new ComboBox<>("Categoría");
        cat.setItems(categoriaService.findAll());
        cat.setItemLabelGenerator(CategoriaEsquema::getCategoria);

        Button save = new Button("Añadir", VaadinIcon.PLUS.create(), e -> {
            if(t.isEmpty() || a.isEmpty()) return;
            UsuarioEsquema yo = usuarioService.buscarPorNombre(username).get(0);
            LibroEsquema l = new LibroEsquema();
            l.setTitulo(t.getValue()); l.setAutor(a.getValue());
            l.setImagenUrl(img.getValue().isEmpty() ? "https://via.placeholder.com/150x200?text=Sin+Portada" : img.getValue());
            l.setUsuario(yo);
            l.setCategorias(new ArrayList<>());
            if(cat.getValue() != null) l.getCategorias().add(cat.getValue());
            libroService.save(l);
            actualizarLista();
            t.clear(); a.clear(); img.clear(); cat.clear();
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        formCard.add(t, a, img, cat, save);
        formCard.setVerticalComponentAlignment(Alignment.BASELINE, save);

        // --- CONFIGURACIÓN DEL CONTENEDOR FLEX ---
        contenedorCards.setWidthFull();
        contenedorCards.setMaxWidth("1050px"); // Un poco más ancho para que quepan dos de ~500px
        contenedorCards.setFlexWrap(FlexLayout.FlexWrap.WRAP); // Esto hace que bajen a la siguiente fila
        contenedorCards.getStyle().set("gap", "20px"); // Espacio entre tarjetas
        contenedorCards.setJustifyContentMode(JustifyContentMode.START); // Alineadas a la izquierda

        add(new H2("Mi Estantería Personal"), formCard, contenedorCards);
        actualizarLista();
    }

    private void actualizarLista() {
        contenedorCards.removeAll();
        libroService.findAll().stream()
                .filter(l -> l.getUsuario() != null && l.getUsuario().getNombre().equalsIgnoreCase(username))
                .forEach(libro -> contenedorCards.add(crearTarjetaDobleColumna(libro)));
    }

    private HorizontalLayout crearTarjetaDobleColumna(LibroEsquema libro) {
        HorizontalLayout card = new HorizontalLayout();

        // CAMBIO: El ancho es del 48% aprox para que quepan 2 por fila (dejando hueco para el gap)
        card.setWidth("490px");
        card.setMinWidth("300px"); // Para que en móvil no se rompa

        card.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("padding", "12px")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.08)")
                .set("border", "1px solid #f0f0f0");

        Image mini = new Image(libro.getImagenUrl(), "Portada");
        mini.setWidth("80px");
        mini.setHeight("110px");
        mini.getStyle().set("object-fit", "cover").set("border-radius", "8px");

        VerticalLayout info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(false);

        Span titulo = new Span(libro.getTitulo());
        titulo.getStyle().set("font-weight", "bold").set("font-size", "1.1em");

        Span autor = new Span(libro.getAutor());
        autor.getStyle().set("color", "#666").set("font-size", "0.9em");

        String cats = libro.getCategorias().stream()
                .map(CategoriaEsquema::getCategoria)
                .collect(Collectors.joining(", "));
        Span categoriaText = new Span(cats.isEmpty() ? "General" : cats);
        categoriaText.getStyle()
                .set("font-size", "0.75em")
                .set("color", "#007bff")
                .set("background", "#e7f3ff")
                .set("padding", "2px 8px")
                .set("border-radius", "10px")
                .set("margin-top", "8px");

        info.add(titulo, autor, categoriaText);

        Button del = new Button(VaadinIcon.TRASH.create(), e -> {
            libroService.delete(libro.getId());
            actualizarLista();
        });
        del.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);

        card.add(mini, info, del);
        card.expand(info);
        card.setAlignItems(Alignment.CENTER);

        return card;
    }
}