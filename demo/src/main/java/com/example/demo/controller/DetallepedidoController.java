package com.example.demo.controller;

import com.example.demo.model.Detallepedido;
import com.example.demo.Service.DetallepedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detallepedido")
@CrossOrigin(origins = "http://localhost:4200")
public class DetallepedidoController {
    @Autowired
    private DetallepedidoService detallepedidoService;

    // Endpoint para listar productos
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MOZO')")
    public List<Detallepedido> listarDetallepedido() {
        return detallepedidoService.listarDetallepedido();
    }

    // Endpoint para crear un nuevo producto
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MOZO')")
    public Detallepedido crearDetallepedido(@RequestBody Detallepedido detallepedido) {
        return detallepedidoService.crearDetallepedido(detallepedido);
    }

    // Endpoint para obtener un producto por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MOZO')")
    public ResponseEntity<Detallepedido> obtenerDetallepedidoPorId(@PathVariable Long id) {
        Optional<Detallepedido> detallepedido = detallepedidoService.obtenerDetallepedidoPorId(id);
        return detallepedido.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para actualizar un producto existente
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MOZO')")
    public ResponseEntity<Detallepedido> actualizarDetallepedido(@PathVariable Long id,
            @RequestBody Detallepedido detallepedido) {
        Detallepedido detallepedidoActualizado = detallepedidoService.actualizarDetallepedido(id, detallepedido);
        return ResponseEntity.ok(detallepedidoActualizado);
    }

    // Endpoint para eliminar un producto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // Solo admin puede eliminar
    public ResponseEntity<Void> eliminarDetallepedidp(@PathVariable Long id) {
        detallepedidoService.eliminarDetallepedido(id);
        return ResponseEntity.noContent().build();
    }
}
