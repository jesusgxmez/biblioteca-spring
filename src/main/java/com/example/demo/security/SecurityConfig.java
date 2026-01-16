package com.example.demo.security;

import com.example.demo.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurity {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Permitimos el acceso a los recursos públicos (estilos, imágenes)
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/images/*.png").permitAll());
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Usuarios de prueba (Asegúrate de crearlos luego en la tabla de Usuarios para que el filtro funcione)
        UserDetails user = User.withUsername("jesus")
                .password("{noop}1234")
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("paco")
                .password("{noop}1234")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}