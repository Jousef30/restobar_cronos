package com.example.demo.Service;


import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscar al usuario por su email
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> 
            new UsernameNotFoundException("Usuario no encontrado con el email: " + email));

        // Retornar el UserDetails (aquí estamos usando la clase User de Spring Security)
        return new User(usuario.getEmail(), usuario.getPassword(), Collections.singletonList(() -> usuario.getRol().name()));
    }
}
