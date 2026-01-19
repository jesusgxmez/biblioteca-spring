package com.example.demo.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@PageTitle("Login | Biblioteca")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        getStyle().set("background-image", "url('https://img2.wallspic.com/previews/2/2/1/5/3/135122/135122-estantes_de_libros_de_madera_marron_con_luces_encendidas_en_la_habitacion-x750.jpg')");
        getStyle().set("background-size", "cover");
        getStyle().set("background-position", "center");

        Div loginContainer = new Div();
        loginContainer.getStyle()
                .set("background", "rgba(10, 20, 35, 0.8)")
                .set("backdrop-filter", "blur(12px)")
                .set("padding", "40px")
                .set("border-radius", "25px")
                .set("border", "2px solid #00d4ff")
                .set("box-shadow", "0 0 20px rgba(0, 212, 255, 0.3)");

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("BIBLIOTECA PERSONAL");
        i18n.getForm().setUsername("Usuario");
        i18n.getForm().setPassword("Contraseña");
        i18n.getForm().setSubmit("Entrar");
        login.setI18n(i18n);

        login.getElement().getStyle().set("color", "white");

        login.getElement().executeJs(
                "this.shadowRoot.querySelector('vaadin-button').style.background = 'linear-gradient(45deg, #c2913e, #f1c40f)';" +
                        "this.shadowRoot.querySelector('vaadin-button').style.color = '#000';" +
                        "this.shadowRoot.querySelector('vaadin-button').style.fontWeight = 'bold';" +
                        "this.shadowRoot.querySelectorAll('vaadin-text-field, vaadin-password-field').forEach(f => {" +
                        "  f.style.color = 'white';" +
                        "});"
        );

        login.setAction("login");

        loginContainer.add(login);
        add(loginContainer);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
        }
    }
}