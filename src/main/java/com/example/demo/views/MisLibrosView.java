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
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
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
    private final FlexLayout contenedorCards = new FlexLayout();

    public MisLibrosView(LibroEsquemaService libroService, UsuarioEsquemaService usuarioService,
                         CategoriaEsquemaService categoriaService, SecurityService securityService) {
        this.libroService = libroService;
        this.usuarioService = usuarioService;
        this.username = securityService.getAuthenticatedUsername();

        setAlignItems(Alignment.CENTER);

        HorizontalLayout formCard = new HorizontalLayout();
        formCard.setWidthFull();
        formCard.setMaxWidth("1100px");
        formCard.setPadding(true);
        formCard.getStyle()
                .set("background", "#f9f9f9")
                .set("border-radius", "12px")
                .set("box-shadow", "inset 0 2px 4px rgba(0,0,0,0.05)")
                .set("margin-bottom", "30px");

        TextField t = new TextField("Título");
        TextField a = new TextField("Autor");
        TextField img = new TextField("URL Portada");
        NumberField pTotales = new NumberField("Pág. Totales");
        pTotales.setWidth("120px");

        ComboBox<CategoriaEsquema> cat = new ComboBox<>("Categoría");
        cat.setItems(categoriaService.findAll());
        cat.setItemLabelGenerator(CategoriaEsquema::getCategoria);

        Button save = new Button("Añadir", VaadinIcon.PLUS.create(), e -> {
            if(t.isEmpty() || a.isEmpty()) {
                Notification.show("Título y Autor son obligatorios");
                return;
            }
            UsuarioEsquema yo = usuarioService.buscarPorNombre(username).get(0);

            LibroEsquema l = new LibroEsquema();
            l.setTitulo(t.getValue());
            l.setAutor(a.getValue());
            l.setImagenUrl(img.getValue().isEmpty() ? "https://via.placeholder.com/150x200?text=Sin+Portada" : img.getValue());
            l.setUsuario(yo);

            int paginas = pTotales.getValue() != null ? pTotales.getValue().intValue() : 0;
            l.setPaginasTotales(paginas);
            l.setPaginasLeidas(0);

            l.setCategorias(new ArrayList<>());
            if(cat.getValue() != null) l.getCategorias().add(cat.getValue());

            libroService.save(l);
            actualizarLista();

            t.clear(); a.clear(); img.clear(); cat.clear(); pTotales.clear();
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        formCard.add(t, a, img, pTotales, cat, save);
        formCard.setVerticalComponentAlignment(Alignment.BASELINE, save);

        contenedorCards.setWidthFull();
        contenedorCards.setMaxWidth("1050px");
        contenedorCards.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        contenedorCards.getStyle().set("gap", "20px");
        contenedorCards.setJustifyContentMode(JustifyContentMode.START);

        add(new H2("Mi Progreso de Lectura"), formCard, contenedorCards);
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
        card.setWidth("490px");
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("padding", "15px")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.08)")
                .set("border", "1px solid #eee");

        Image mini = new Image(libro.getImagenUrl(), "Portada");
        mini.setWidth("90px");
        mini.setHeight("130px");
        mini.getStyle().set("object-fit", "cover").set("border-radius", "8px");

        VerticalLayout info = new VerticalLayout();
        info.setPadding(false);
        info.setSpacing(false);

        Span titulo = new Span(libro.getTitulo());
        titulo.getStyle().set("font-weight", "bold").set("font-size", "1.1em");

        Span autor = new Span(libro.getAutor());
        autor.getStyle().set("color", "gray").set("font-size", "0.9em").set("margin-bottom", "10px");

        int leidas = (libro.getPaginasLeidas() != null) ? libro.getPaginasLeidas() : 0;
        int totales = (libro.getPaginasTotales() != null && libro.getPaginasTotales() > 0) ? libro.getPaginasTotales() : 0;

        ProgressBar barraProgreso = new ProgressBar();
        if (totales > 0) {
            double percent = (double) leidas / totales;
            barraProgreso.setValue(Math.min(percent, 1.0));
            if (percent >= 1.0) barraProgreso.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);
        } else {
            barraProgreso.setValue(0);
        }
        barraProgreso.getStyle().set("margin-bottom", "10px");

        NumberField inputLeidas = new NumberField();
        inputLeidas.setValue((double) leidas);
        inputLeidas.setWidth("80px");
        inputLeidas.addThemeVariants(TextFieldVariant.LUMO_SMALL, TextFieldVariant.LUMO_ALIGN_CENTER);

        Span divisor = new Span(" / " + totales + " pág.");
        divisor.getStyle().set("font-size", "0.85em").set("color", "#666");

        Button btnUpdate = new Button(VaadinIcon.CHECK.create(), e -> {
            if (inputLeidas.getValue() != null) {
                int nuevaLeida = inputLeidas.getValue().intValue();
                if (totales > 0 && nuevaLeida > totales) nuevaLeida = totales;

                libro.setPaginasLeidas(nuevaLeida);
                libroService.save(libro);
                actualizarLista(); // Refrescar vista
                Notification.show("¡Progreso guardado!");
            }
        });
        btnUpdate.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout controlesProgreso = new HorizontalLayout(inputLeidas, divisor, btnUpdate);
        controlesProgreso.setAlignItems(Alignment.CENTER);
        controlesProgreso.setSpacing(true);

        info.add(titulo, autor, barraProgreso, controlesProgreso);

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