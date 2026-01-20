package com.example.demo.security;

import com.example.demo.entities.UsuarioEsquema;
import com.example.demo.services.UsuarioEsquemaService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UsuarioEsquemaService usuarioService;

    public UserDetailServiceImpl(UsuarioEsquemaService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuarios = usuarioService.buscarPorNombre(username);
        if (usuarios.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        UsuarioEsquema usuario = usuarios.get(0);

        return new User(
                usuario.getNombre(),
                usuario.getContraseña(),
                Collections.emptyList()
        );
    }
}