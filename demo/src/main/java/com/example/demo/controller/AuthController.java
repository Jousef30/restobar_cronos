package com.example.demo.controller;

import com.example.demo.model.Usuario;

import com.example.demo.Security.JWTUtil;
import com.example.demo.Service.EmailService;
import com.example.demo.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    // Endpoint para login y generación de JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Usuario user = usuarioService.getUsuarioPorEmail(usuario.getEmail()).orElse(null);
        if (user != null && user.getPassword().equals(usuario.getPassword())) {
            // Crear el token
            String token = jwtUtil.generateToken(user.getEmail(), user.getRol().name());

            // Retornar un objeto JSON con el token y datos del usuario
            AuthResponse response = new AuthResponse(
                    token,
                    user.getIdUsuario(),
                    user.getEmail(),
                    user.getNombreCompleto(),
                    user.getRol().name());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Credenciales incorrectas");
        }
    }

    // Endpoint para registrar un usuario (con email y password)
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        // Crear el usuario en la base de datos
        Usuario savedUser = usuarioService.crearUsuario(usuario);

        // Enviar el correo de confirmación
        try {
            // Usar el correo del usuario registrado
            emailService.sendSimpleEmail(
                    usuario.getEmail(), // Este es el email del usuario
                    "Bienvenido a Restobar",
                    "Gracias por registrarte en nuestro sistema. Tu cuenta ha sido creada correctamente.");
        } catch (MailException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo.");
        }

        // Retornar la respuesta con el usuario creado
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

}
