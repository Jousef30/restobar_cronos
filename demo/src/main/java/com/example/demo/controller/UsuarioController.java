package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.Service.UsuarioService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Endpoint para crear un usuario
    @PostMapping("/crear")
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(new java.sql.Timestamp(System.currentTimeMillis()));
        }
        return usuarioService.crearUsuario(usuario);
    }

    // Endpoint para obtener usuario por email
    @GetMapping("/buscar/{email}")
    public Usuario getUsuarioPorEmail(@PathVariable String email) {
        return usuarioService.getUsuarioPorEmail(email).orElse(null);
    }

    @GetMapping("/listar")
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioService.getUsuarioPorId(id);

        if (usuarioExistente.isPresent()) {
            Usuario usuarioActualizado = usuarioExistente.get();
            usuarioActualizado.setNombreCompleto(usuario.getNombreCompleto());
            usuarioActualizado.setEmail(usuario.getEmail());
            usuarioActualizado.setPassword(usuario.getPassword());
            usuarioActualizado.setTelefono(usuario.getTelefono());
            usuarioActualizado.setRol(usuario.getRol());

            Usuario updatedUser = usuarioService.crearUsuario(usuarioActualizado);

            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioExistente = usuarioService.getUsuarioPorId(id);

        if (usuarioExistente.isPresent()) {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.ok("Usuario eliminado exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }
    }

}
