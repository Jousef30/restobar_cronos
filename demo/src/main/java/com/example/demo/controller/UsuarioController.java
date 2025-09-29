package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200") // ajuste en dev
public class UsuarioController {

    private final UsuarioRepository repo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository repo, BCryptPasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<Usuario> listar() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Usuario u) {
        // validar email único
        if (repo.findByEmail(u.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body("El email ya está registrado");
        }
        // encriptar contraseña
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        Usuario saved = repo.save(u);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Usuario datos) {
        return repo.findById(id).map(u -> {
            u.setNombre_completo(datos.getNombre_completo());
            u.setTelefono(datos.getTelefono());
            // si cambia el email, validar unicidad
            if (!u.getEmail().equals(datos.getEmail())) {
                if (repo.findByEmail(datos.getEmail()).isPresent()) {
                    return ResponseEntity.status(409).body("El email ya está en uso");
                }
                u.setEmail(datos.getEmail());
            }
            // si envían password nuevo (no vacío), encriptar y actualizar
            if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
                u.setPassword(passwordEncoder.encode(datos.getPassword()));
            }
            u.setRol(datos.getRol());
            Usuario saved = repo.save(u);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
