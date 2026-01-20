package com.example.demo.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
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

        getStyle().set("background-image", "url('https://images.unsplash.com/photo-1521587760476-6c12a4b040da?fm=jpg&q=60&w=3000&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8Zm9uZG8lMjBkZSUyMGxhJTIwYmlibGlvdGVjYXxlbnwwfHwwfHx8MA%3D%3D')");
        getStyle().set("background-size", "cover");
        getStyle().set("background-position", "center");

        Div loginContainer = new Div();
        loginContainer.getStyle()
                .set("background", "rgba(10, 20, 35, 0.85)")
                .set("backdrop-filter", "blur(15px)")
                .set("padding", "30px")
                .set("border-radius", "25px")
                .set("border", "2px solid #00d4ff")
                .set("box-shadow", "0 0 30px rgba(0, 212, 255, 0.4)")
                .set("width", "400px")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center");

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("BIBLIOTECA PERSONAL");
        i18n.getForm().setUsername("Usuario");
        i18n.getForm().setPassword("Contraseña");
        i18n.getForm().setSubmit("Entrar");
        login.setI18n(i18n);

        login.getElement().executeJs(
                "const style = document.createElement('style');" +
                        "style.textContent = ` " +
                        "  vaadin-login-form-wrapper { background: transparent !important; padding: 0 !important; } " +
                        "  [part='form-title'] { color: #00d4ff !important; text-align: center; } " +
                        "  label { color: #00d4ff !important; } " +
                        "  vaadin-button[part='submit-button'] { " +
                        "    background: linear-gradient(45deg, #00d4ff, #0055ff) !important; " +
                        "    color: white !important; " +
                        "    font-weight: bold !important; " +
                        "    margin-top: 20px !important; " +
                        "    box-shadow: 0 0 10px rgba(0, 212, 255, 0.5); " +
                        "  } " +
                        "`; " +
                        "this.shadowRoot.appendChild(style);"
        );

        login.setAction("login");

        Button btnRegistro = new Button("¿No tienes cuenta? Regístrate aquí", e -> UI.getCurrent().navigate("registro"));
        btnRegistro.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        btnRegistro.getStyle()
                .set("color", "#00d4ff")
                .set("font-size", "14px")
                .set("margin-top", "10px")
                .set("cursor", "pointer");

        loginContainer.add(login, btnRegistro);
        add(loginContainer);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
        }
    }
}