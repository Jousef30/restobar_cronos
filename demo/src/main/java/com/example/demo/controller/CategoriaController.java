package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import com.example.demo.model.Categoria;
import com.example.demo.repository.CategoriaRepository;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "http://localhost:4200") 
public class CategoriaController {

    private final CategoriaRepository repo;
    public CategoriaController(CategoriaRepository repo) { this.repo = repo; }

    // Endpoint para listar categorías
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
    public List<Categoria> listar() {
        return repo.findAll();
    }

    // Endpoint para crear una nueva categoría
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
    public Categoria crear(@RequestBody Categoria c) {
        return repo.save(c);
    }

    // Endpoint para actualizar una categoría
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @RequestBody Categoria categoria) {
        return repo.findById(id)
                .map(c -> {
                    c.setNombre(categoria.getNombre());
                    return ResponseEntity.ok(repo.save(c));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para eliminar una categoría
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") 
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
